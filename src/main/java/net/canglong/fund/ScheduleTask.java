package net.canglong.fund;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.JobService;
import net.canglong.fund.service.RateService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Log4j2
public class ScheduleTask {

    @Resource
    private JobService jobService;

    @Resource
    private RateService rateService;

    private boolean terminated = false;

    @Scheduled(fixedDelay = 86400000)
    @Async
    public void refresh() {
        terminated = jobService.startPriceRetrievalJob(10);
        if (terminated) {
            log.info("Bypass fund retrieval and import job since less than 1 month's data need to be retrieved.");
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Async
    public void reportImportStatus() {
        Status status = jobService.getPriceRetrievalJobStatus();
        if (!terminated) {
            if (status.isTerminated()) {
                log.info("*******************");
                log.info("Retrieval job was terminated.");
                log.info("*******************");
            } else if (status.getTaskCount() > 0) {
                log.info("*******************");
                log.info(String.format("Left companies: %s", status.getLeftCount()));
                log.info(String.format("Elapse Time: %s", status.getElapseTime()));
                log.info(String.format("Active Threads: %s", status.getAliveThreadCount()));
                log.info("*******************");
            }
            terminated = status.isTerminated();
        }
    }

    @Scheduled(fixedDelay = 108000000)
    @Async
    public void generateStatisticData() {
        rateService.generate(List.of("混合型", "股票型"),false);
    }

}
