package net.canglong.fund;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.JobService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Log4j2
public class ScheduleTask {

    @Resource
    private JobService jobService;

    private boolean terminated = false;

    @Scheduled(fixedRate = 86400000)
    public void refresh() {
//        jobService.startPriceRetrievalJob(10);
    }

    @Scheduled(fixedRate = 60000)
    public void reportStatus() {
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
}
