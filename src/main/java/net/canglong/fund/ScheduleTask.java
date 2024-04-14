package net.canglong.fund;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.JobService;
import net.canglong.fund.service.RateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ScheduleTask {

  @Resource
  private JobService jobService;

  @Resource
  private RateService rateService;

  private boolean terminated = false;

  @Value("${spring.threadCount}")
  private int threadCount;

  @Scheduled(fixedDelay = 86400000)
  @Async
  public void refresh() {
    terminated = jobService.startPriceRetrievalJob(threadCount);
    if (terminated) {
      log.info(
          "Bypass fund retrieval and import job since less than 1 month's data need to be retrieved.");
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
        log.info(String.format("Total fund count: %s", status.getTotalFundCount()));
        log.info(String.format("Left fund count: %s", status.getLeftFundCount()));
        log.info(String.format("Elapse Time: %s", status.getElapseTime()));
        log.info(String.format("Active Threads: %s", status.getAliveThreadCount()));
        log.info("*******************");
      }
      terminated = status.isTerminated();
    }
  }

  @Scheduled(fixedDelay = 108000000)
  @Async
  public void generateStatisticData() throws Exception {
    rateService.generate(List.of("混合型", "股票型", "债券型", "QDII", "短期理财债券型"), false);
  }

}
