package net.canglong.fund.schedule;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Status;
import net.canglong.fund.service.RateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class StatisticScheduler {

  private final RateService rateService;

  public StatisticScheduler(RateService rateService) {
    this.rateService = rateService;
  }

  // Run roughly daily
  @Scheduled(fixedDelay = 108000000)
  public void runStatisticGeneration() {
    // Generate statistics for default types without refreshing all data
    rateService.generateStatisticData(List.of("混合型", "股票型", "债券型", "QDII", "短期理财债券型"), false);
  }

  // Report job status periodically
  @Scheduled(fixedDelay = 60000)
  public void reportStatisticJobStatus() {
    Status status = rateService.getStatisticJobStatus();
    if (status.isTerminated()) {
      log.info("\n*******************\nStatistic job was completed.\n*******************");
    } else if (status.getAliveThreadCount() > 0) {
      log.info(
          "\n*******************\nStatistic generation\nLeft fund count: {}\nElapse Time: {}\nActive Threads: {}\n*******************",
          status.getLeftFundCount(), status.getElapseTime(), status.getAliveThreadCount());
    }
  }
}
