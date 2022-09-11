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

    @Scheduled(fixedRate = 86400000)
    public void refresh() throws Exception {
//        jobService.startPriceRetrievalJob();
    }

    @Scheduled(fixedRate = 60000)
    public void reportStatus() throws Exception {
        Status status = jobService.getPriceRetrievalJobStatus();
        log.info("*******************");
        log.info(String.format("Left companies: %s", status.getLeftCount()));
        log.info(String.format("Elapse Time: %s", status.getElapseTime()));
        log.info(String.format("Active Threads: %s", status.getAliveThreadCount()));
        log.info("*******************");
    }
}
