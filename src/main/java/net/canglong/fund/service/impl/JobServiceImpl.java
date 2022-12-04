package net.canglong.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Log4j2
@Service
public class JobServiceImpl implements JobService {

    private ThreadPoolTaskExecutor executor;
    private long startTime;
    @Resource
    private WebsiteDataService dataService;
    @Resource
    private PriceService priceService;
    @Resource
    private FundService fundService;
    @Resource
    private CompanyService companyService;
    private final static int GET_FUND_LIST = 0;
    private final static int GET_FUND_PRICE = 1;

    @Override
    public Status startPriceRetrievalJob(int threadCount) throws Exception {
        log.info("Website retrieval thread count is " + threadCount);
        if (executor == null || executor.getThreadPoolExecutor().isShutdown()) {
            executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(threadCount);
            executor.setThreadNamePrefix("Website retrieval thread pool");
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.initialize();
        }
        if (executor.getThreadPoolExecutor().getQueue().isEmpty()) {
            List<String> companyIds = dataService.getCompanyIds();
            log.info("Totally found " + companyIds.size() + " companies.");
            startTime = System.currentTimeMillis();
            for (String id : companyIds) {
                if (!executor.getThreadPoolExecutor().isShutdown()) {
                    executor.execute(new Task(GET_FUND_LIST, id));
                }
            }
        }
        return getPriceRetrievalJobStatus();
    }

    @Override
    public Status getPriceRetrievalJobStatus() {
        Status status = new Status();
        if (executor != null) {
            status.setLeftCount(executor.getThreadPoolExecutor().getQueue().size());
            status.setAliveThreadCount(executor.getActiveCount());
            status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
            status.setTerminated(executor.getThreadPoolExecutor().isTerminated());
            status.setTaskCount(executor.getThreadPoolExecutor().getTaskCount());
        }
        return status;
    }

    @Override
    public boolean stopPriceRetrievalJob() {
        executor.getThreadPoolExecutor().getQueue().clear();
        executor.shutdown();
        log.info("executor is terminating...");
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
                } else if (jobType == GET_FUND_LIST) {
                    Company savedCompany = companyService.create(dataService.getCompany(id));
                    List<Fund> fundList = fundService.create(dataService.getFunds(id, savedCompany.getAbbr()));
                    log.info("Imported fund list for " + savedCompany.getName() + ". Total funds: " + fundList.size());
                    for (Fund fund : fundList) {
                        if (!executor.getThreadPoolExecutor().isShutdown()) {
                            executor.execute(new Task(GET_FUND_PRICE, fund.getId()));
                        }
                    }
                    executor.shutdown();
                    log.info("executor terminated.");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
