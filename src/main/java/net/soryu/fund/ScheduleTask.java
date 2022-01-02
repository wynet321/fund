package net.soryu.fund;

import net.soryu.fund.service.DataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ScheduleTask {

    @Resource
    private DataService dataService;

    @Scheduled(fixedRate = 86400000)
    public void refresh() throws Exception {
        dataService.generateFundPriceRetrievalJob();
    }
}
