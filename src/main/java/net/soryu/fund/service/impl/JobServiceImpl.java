package net.soryu.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.soryu.fund.entity.Company;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.Status;
import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.FundService;
import net.soryu.fund.service.JobService;
import net.soryu.fund.service.PriceService;
import net.soryu.fund.service.WebsiteDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

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
    @Value("${server.thread-count}")
    private int threadCount;
    private final static int GET_FUND_LIST = 0;
    private final static int GET_FUND_PRICE = 1;

    @Override
    public Status startPriceRetrievalJob() throws Exception {
        log.info("Website retrieval thread count is " + threadCount);
        if (executor == null) {
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
                executor.execute(new Task(GET_FUND_LIST, id));
            }
        }
        return getPriceRetrievalJobStatus();
    }

    @Override
    public Status getPriceRetrievalJobStatus() throws Exception {
        Status status = new Status();
        status.setLeftCount(executor.getThreadPoolExecutor().getQueue().size());
        status.setAliveThreadCount(executor.getActiveCount());
        status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
        status.setStopped(executor.getThreadPoolExecutor().isTerminated());
        return status;
    }

    @Override
    public boolean stopPriceRetrievalJob() throws Exception {
        executor.shutdown();
        executor.getThreadPoolExecutor().getQueue().clear();
        log.info("executor is terminating...");
        return true;
    }

    class Task implements Runnable {

        private String id;
        private int jobType;

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
                        executor.execute(new Task(GET_FUND_PRICE, fund.getId()));
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
