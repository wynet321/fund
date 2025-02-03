package net.canglong.fund.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.MonthRateIdentity;
import net.canglong.fund.entity.PeriodRate;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.Status;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.entity.YearRateIdentity;
import net.canglong.fund.repository.MonthRateRepo;
import net.canglong.fund.repository.PeriodRateRepo;
import net.canglong.fund.repository.YearRateRepo;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.RateService;
import net.canglong.fund.vo.YearAverageRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class RateServiceImpl implements RateService {

  private final PeriodRateRepo periodRateRepo;
  @Resource
  private MonthRateRepo monthRateRepo;
  @Resource
  private YearRateRepo yearRateRepo;
  @Resource
  private FundService fundService;
  @Resource
  private PriceService priceService;
  @Resource
  private CompanyService companyService;
  private ThreadPoolTaskExecutor executor;
  private long startTime;

  public RateServiceImpl(PeriodRateRepo periodRateRepo) {
    this.periodRateRepo = periodRateRepo;
  }

  @Override
  public List<MonthRate> getMonthRateById(String fundId, int year) {
    return monthRateRepo.findAllById(fundId, year);
  }

  @Override
  public Boolean generate(List<String> types, boolean refreshAllData) {
    Date latestPriceDate = priceService.findLatestPriceDate();
    if (refreshAllData
        || LocalDate.now().isAfter(LocalDate.parse(latestPriceDate.toString()).plusMonths(1))) {
      log.info("Start to generate statistic data...");
      startTime = System.currentTimeMillis();
      executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(20);
      executor.setThreadNamePrefix("Statistic job thread pool");
      executor.setWaitForTasksToCompleteOnShutdown(true);
      executor.initialize();
      List<Fund> allStockFunds = fundService.findAllByTypes(types);
      for (Fund stockFund : allStockFunds) {
        executor.submit(() -> generate(stockFund.getId(), refreshAllData));
      }
      executor.shutdown();
      return true;
    }
    log.info(
        "Bypass fund statistic generation since less than 1 month's data need to be delt with.");
    return false;
  }

  @Override
  public Status getStatisticJobStatus() {
    Status status = new Status();
    if (executor != null) {
      status.setLeftFundCount(executor.getThreadPoolExecutor().getQueue().size());
      status.setAliveThreadCount(executor.getActiveCount());
      status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
      status.setTerminated(executor.getThreadPoolExecutor().isTerminated());
      status.setTaskCount(executor.getThreadPoolExecutor().getTaskCount());
    }
    return status;
  }

  @Override
  public Boolean generate(String fundId, boolean refreshAllData) {
    Date latestPriceDate = priceService.findLatestPriceDate();
    LocalDate statisticDueDate = LocalDate.of(1970, 1, 1);
    if (!refreshAllData) {
      statisticDueDate = fundService.findById(fundId).getStatisticDueDate();
      statisticDueDate = (statisticDueDate == null) ? LocalDate.of(1970, 1, 1) : statisticDueDate;
    }
    generateYearRates(fundId, statisticDueDate);
    generatePeriodRate(fundId);
    if ((LocalDate.now().getYear() == statisticDueDate.getYear()
        && LocalDate.now().getMonthValue() > statisticDueDate.plusMonths(1).getMonthValue())
        || refreshAllData) {
      generateMonthPriceData(fundId, statisticDueDate);
    }
    Fund fund = fundService.findById(fundId);
    fund.setStatisticDueDate(LocalDate.parse(latestPriceDate.toString()));
    fundService.update(fund);
    return true;
  }

  private void generateYearRates(String fundId, LocalDate statisticDueDate) {
    Map<Integer, BigDecimal> yearPrices = priceService.findYearPriceMapById(fundId);
    List<YearRate> yearRates = new ArrayList<>();
    for (Integer key : yearPrices.keySet()) {
      if (key > statisticDueDate.getYear()) {
        Fund fund = fundService.findById(fundId);
        Company company = companyService.find(fund.getCompanyId());
        YearRate yearRate = new YearRate();
        yearRate.setYearRateIdentity(new YearRateIdentity(fundId, key));
        yearRate.setName(fund.getName());
        yearRate.setCompanyName(company.getName());
        yearRate.setType(fund.getType());
        if (yearPrices.containsKey(key - 1) && yearPrices.get(key - 1) != null) {
          if (yearPrices.get(key - 1).intValue() == 0) {
            yearRate.setRate(BigDecimal.valueOf(0));
          } else {
            yearRate.setRate(yearPrices.get(key).subtract(yearPrices.get(key - 1))
                .divide(yearPrices.get(key - 1), 4, RoundingMode.HALF_UP));
          }
        } else {
          yearRate.setRate(BigDecimal.valueOf(0));
        }
        yearRates.add(yearRate);
      }
    }
    yearRateRepo.saveAllAndFlush(yearRates);
  }

  private void generateMonthPriceData(String fundId, LocalDate statisticDueDate) {
    Fund fund = fundService.findById(fundId);
    Company company = companyService.find(fund.getCompanyId());
    for (int year = statisticDueDate.getYear(); year <= LocalDate.now().getYear(); year++) {
      Map<Integer, BigDecimal> monthPrices = priceService.findMonthPriceMapById(fundId, year);
      for (Integer key : monthPrices.keySet()) {
        MonthRate monthRate = new MonthRate();
        monthRate.setMonthRateIdentity(new MonthRateIdentity(fundId, year, key));
        monthRate.setName(fund.getName());
        monthRate.setCompanyName(company.getName());
        monthRate.setType(fund.getType());
        if (monthPrices.containsKey(key - 1) && monthPrices.get(key - 1) != null) {
          if (monthPrices.get(key - 1).intValue() == 0) {
            monthRate.setRate(BigDecimal.valueOf(0));
          } else {
            monthRate.setRate(monthPrices.get(key).subtract(monthPrices.get(key - 1))
                .divide(monthPrices.get(key - 1), 4, RoundingMode.HALF_UP));
          }
        } else {
          Price price = null;
          if (key == 1) {
            price = priceService.findLatestPriceBeforeDate(fundId, LocalDate.of(year - 1, 12, 31));
          }
          if (price == null) {
            price = priceService.findEarliestPriceAfterDate(fundId, LocalDate.of(year, key, 1));
          }
          if (price == null) {
            monthRate.setRate(BigDecimal.valueOf(0L));
          } else {
            if (price.getAccumulatedPrice().intValue() == 0) {
              monthRate.setRate(BigDecimal.valueOf(0));
            } else {
              monthRate.setRate(monthPrices.get(key).subtract(price.getAccumulatedPrice())
                  .divide(price.getAccumulatedPrice(), 4, RoundingMode.HALF_UP));
            }
          }
        }
        monthRateRepo.saveAndFlush(monthRate);
      }
    }
  }

  public PeriodRate generatePeriodRate(String fundId) {
    Fund fund = fundService.findById(fundId);
    Company company = companyService.find(fund.getCompanyId());
    int currentYear = LocalDate.now().getYear() - 1;
    Map<Integer, BigDecimal> yearPrices = priceService.findYearPriceMapById(fundId);
    BigDecimal[] rates = new BigDecimal[10];
    for (int i = 0; i < 10; i++) {
      if (yearPrices.containsKey(currentYear)
          && yearPrices.containsKey(currentYear - i - 1)
          && yearPrices.get(currentYear - i - 1) != null
          && yearPrices.get(currentYear - i - 1).compareTo(BigDecimal.ZERO) != 0) {
        BigDecimal rate = yearPrices.get(currentYear).subtract(yearPrices.get(currentYear - i - 1))
            .divide(yearPrices.get(currentYear - i - 1), 4, RoundingMode.HALF_UP);
        rates[i] = rate;
      } else {
        rates[i] = BigDecimal.valueOf(0);
      }
    }
    PeriodRate periodRate = new PeriodRate();
    periodRate.setId(fundId);
    periodRate.setName(fund.getName());
    periodRate.setCompanyName(company.getName());
    periodRate.setType(fund.getType());
    periodRate.setOneYearRate(rates[0]);
    periodRate.setTwoYearRate(rates[1]);
    periodRate.setThreeYearRate(rates[2]);
    periodRate.setFourYearRate(rates[3]);
    periodRate.setFiveYearRate(rates[4]);
    periodRate.setSixYearRate(rates[5]);
    periodRate.setSevenYearRate(rates[6]);
    periodRate.setEightYearRate(rates[7]);
    periodRate.setNineYearRate(rates[8]);
    periodRate.setTenYearRate(rates[9]);
    return periodRateRepo.saveAndFlush(periodRate);
  }

  @Override
  public Page<PeriodRate> getOneYearRateDesc(List<String> types, Pageable pageable) {
    return periodRateRepo.getPeriodRateOrderByOneYearRateDesc(types, pageable);
  }

  @Override
  public Page<PeriodRate> getThreeYearRateDesc(List<String> types, Pageable pageable) {
    return periodRateRepo.getPeriodRateOrderByThreeYearRateDesc(types, pageable);
  }

  @Override
  public Page<PeriodRate> getFiveYearRateDesc(List<String> types, Pageable pageable) {
    return periodRateRepo.getPeriodRateOrderByFiveYearRateDesc(types, pageable);
  }

  @Override
  public Page<PeriodRate> getEightYearRateDesc(List<String> types, Pageable pageable) {
    return periodRateRepo.getPeriodRateOrderByEightYearRateDesc(types, pageable);
  }

  @Override
  public Page<PeriodRate> getTenYearRateDesc(List<String> types, Pageable pageable) {
    return periodRateRepo.getPeriodRateOrderByTenYearRateDesc(types, pageable);
  }

  @Override
  public List<YearRate> getYearRateById(String fundId) {
    return yearRateRepo.findAllById(fundId);
  }

  @Override
  public Page<YearRate> getYearRateByTypesAndYear(List<String> types, int year, Pageable pageable) {
    return yearRateRepo.findAllByTypesAndYear(types, year, pageable);
  }

  @Override
  public List<YearAverageRate> getYearAverageRankByTypesAndPeriod(List<String> types, int period,
      Pageable pageable) {
    List<YearAverageRate> yearAverageRates = new ArrayList<>();
    Page<Object[]> yearAverageRatesResult = yearRateRepo.findAverageRankByTypesAndYear(types,
        LocalDate.now().getYear() - period - 1,
        pageable);
    yearAverageRatesResult.forEach(item -> yearAverageRates.add(
        new YearAverageRate(item[0].toString(), item[1].toString(), item[2].toString(),
            item[3].toString(), BigDecimal.valueOf((double) item[4]),
            BigDecimal.valueOf((double) item[5]))));
    return yearAverageRates;
  }

  @Override
  @Scheduled(fixedDelay = 108000000)
  @Async
  public Boolean generateStatisticData() {
    return generate(List.of("混合型", "股票型", "债券型", "QDII", "短期理财债券型"), false);
  }

  @Override
  public Boolean generateStatisticData(List<String> types, boolean refreshAllData) {
    return generate(types, refreshAllData);
  }

  @Override
  @Async
  @Scheduled(fixedDelay = 60000)
  public void reportStatusOfGenerateStatisticForAll() {
    Status status = getStatisticJobStatus();
    if (status.isTerminated() && executor != null) {
      log.info("\n*******************\nStatistic job was completed.\n*******************");
      executor = null;
    } else if (status.getAliveThreadCount() > 0) {
      log.info(
          "\n*******************\nStatistic generation\nLeft fund count: {}\nElapse Time: {}\nActive Threads: {}\n*******************",
          status.getLeftFundCount(), status.getElapseTime(), status.getAliveThreadCount());
    }
  }
}
