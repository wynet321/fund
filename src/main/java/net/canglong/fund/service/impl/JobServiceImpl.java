package net.canglong.fund.service.impl;

import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.JobService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.WebsiteDataService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JobServiceImpl implements JobService {

  private final static int GET_FUND_LIST = 0;
  private final static int GET_FUND_PRICE = 1;
  private ThreadPoolTaskExecutor executor;
  private long startTime;
  private int fundCount;
  @Resource
  private WebsiteDataService websiteDataService;
  @Resource
  private PriceService priceService;
  @Resource
  private FundService fundService;
  @Resource
  private CompanyService companyService;

  @Override
  public Boolean startPriceRetrievalJob(int threadCount) {
    Price price = priceService.findLatestPrice("000001");
    if (LocalDate.now().isAfter(price.getPriceIdentity().getPriceDate().plusMonths(0))) {
      log.info("Start to retrieve fund information...");
      log.info("Website retrieval thread count is " + threadCount);
      if (executor == null || executor.getThreadPoolExecutor().isShutdown()) {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setThreadNamePrefix("Website retrieval thread pool");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
      }
      if (executor.getThreadPoolExecutor().getQueue().isEmpty()) {
        List<String> companyIds = websiteDataService.getCompanyIds();
        log.info("Totally found " + companyIds.size() + " companies.");
        startTime = System.currentTimeMillis();
        for (String id : companyIds) {
          if (!executor.getThreadPoolExecutor().isShutdown()) {
            executor.execute(new Task(GET_FUND_LIST, id));
          }
        }
        fundCount = executor.getThreadPoolExecutor().getQueue().size();
        log.info("Totally found " + fundCount + " funds.");
        executor.shutdown();
      }
      return false;
    }
    return true;
  }

  @Override
  public Status getPriceRetrievalJobStatus() {
    Status status = new Status();
    if (executor != null) {
      status.setTotalFundCount(fundCount);
      status.setLeftFundCount(executor.getThreadPoolExecutor().getQueue().size());
      status.setAliveThreadCount(executor.getActiveCount());
      status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
      status.setTerminated(executor.getThreadPoolExecutor().isTerminated());
      status.setTaskCount(executor.getThreadPoolExecutor().getTaskCount());
    }
    return status;
  }

  @Override
  public boolean stopPriceRetrievalJob() {
    try {
      executor.getThreadPoolExecutor().getQueue().clear();
      executor.shutdown();
      log.info("executor is terminating...");
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  class Task implements Runnable {

    private final String id;
    private final int jobType;

    public Task(int jobType, String id) {
      this.jobType = jobType;
      this.id = id;
    }

    @Override
    public void run() {
      try {
        if (jobType == GET_FUND_PRICE) {
          priceService.create(id);
//          Thread.sleep(10000);
        } else if (jobType == GET_FUND_LIST) {
          Company savedCompany = companyService.create(websiteDataService.getCompany(id));
          List<Fund> fundList = fundService.create(
              websiteDataService.getFunds(id, savedCompany.getAbbr()));
          log.info("Imported fund list for " + savedCompany.getName() + ". Total funds: "
              + fundList.size());
          for (Fund fund : fundList) {
            if (!executor.getThreadPoolExecutor().isShutdown()) {
              executor.execute(new Task(GET_FUND_PRICE, fund.getId()));
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
