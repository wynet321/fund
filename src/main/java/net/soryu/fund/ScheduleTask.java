package net.soryu.fund;

import net.soryu.fund.service.JobService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ScheduleTask {

    @Resource
    private JobService jobService;

    @Scheduled(fixedRate = 86400000)
    public void refresh() throws Exception {
//        jobService.startPriceRetrievalJob();
    }
}
