package net.canglong.fund.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import net.canglong.fund.vo.FundPrice;

@Service
@Log4j2
public class RateServiceImpl implements RateService {

  @Resource
  private PeriodRateRepo periodRateRepo;
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
  @Resource(name = "statisticExecutor")
  private ThreadPoolTaskExecutor executor;
  private long startTime;

  public RateServiceImpl(PeriodRateRepo periodRateRepo) {
    this.periodRateRepo = periodRateRepo;
  }

  @Override
  public List<MonthRate> getMonthRateById(@NonNull String fundId, int year) {
    return monthRateRepo.findAllById(fundId, year);
  }

  @Override
  public Boolean generate(@NonNull List<String> types, boolean refreshAllData) {
    log.info("Start to generate statistic data...");
    startTime = System.currentTimeMillis();
    executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(20);
    executor.setThreadNamePrefix("Statistic job thread pool");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();
    List<Fund> funds = fundService.findAllByTypes(types);
    for (Fund fund : funds) {
      executor.submit(() -> generate(Objects.requireNonNull(fund.getId()), refreshAllData));
    }
    executor.shutdown();
    return true;
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
  public Boolean generate(@NonNull String fundId, boolean refreshAllData) {
    LocalDate statisticDueDate = LocalDate.of(1970, 1, 1);
    if (!refreshAllData) {
      statisticDueDate = fundService.findById(fundId)
          .map(Fund::getStatisticDueDate)
          .orElse(LocalDate.of(1970, 1, 1));
    }
    if (LocalDate.now().isAfter(statisticDueDate.plusMonths(1L))) {
      generateYearRates(fundId, statisticDueDate);
      generatePeriodRate(fundId);
      if ((LocalDate.now().getYear() == statisticDueDate.getYear()
          && LocalDate.now().getMonthValue() > statisticDueDate.plusMonths(1).getMonthValue())
          || refreshAllData) {
        generateMonthPriceData(fundId, statisticDueDate);
      }
    } else {
      log.debug(
          "Bypass fund statistic generation since less than 1 month's data need to be dealt with.");
    }
    fundService.findById(fundId).ifPresent(fund -> {
      fund.setStatisticDueDate(priceService.findLatestPriceDateById(Objects.requireNonNull(fund.getId())));
      fund.setStartDate(priceService.findEarliestPriceDateById(Objects.requireNonNull(fund.getId())));
      fund.setEndDate(priceService.findLatestPriceDateById(Objects.requireNonNull(fund.getId())));
      fundService.update(fund);
    });
    return true;
  }

  private void generateYearRates(@NonNull String fundId, LocalDate statisticDueDate) {
    Map<Integer, FundPrice> yearPrices = priceService.findYearPriceMapById(fundId);
    List<YearRate> yearRates = new ArrayList<>();
    for (Integer key : yearPrices.keySet()) {
      if (key > statisticDueDate.getYear()) {
        Fund fund = fundService.findById(fundId).orElseThrow();
        Company company = companyService.find(Objects.requireNonNull(fund.getCompanyId()));
        YearRate yearRate = new YearRate();
        yearRate.setYearRateIdentity(new YearRateIdentity(fundId, key));
        yearRate.setName(fund.getName());
        yearRate.setCompanyName(company.getName());
        yearRate.setType(fund.getType());
        if (yearPrices.containsKey(key - 1) && yearPrices.get(key - 1) != null 
            && yearPrices.get(key - 1).getAccumulatedPrice() != null
            && yearPrices.get(key) != null && yearPrices.get(key).getAccumulatedPrice() != null) {
          BigDecimal prevAccumulatedPrice = yearPrices.get(key - 1).getAccumulatedPrice();
          BigDecimal currentAccumulatedPrice = yearPrices.get(key).getAccumulatedPrice();
          BigDecimal prevPrice = yearPrices.get(key - 1).getCurrentPrice();
          
          if (prevAccumulatedPrice.compareTo(BigDecimal.ZERO) == 0 || prevPrice == null || prevPrice.compareTo(BigDecimal.ZERO) == 0) {
            yearRate.setRate(BigDecimal.ZERO);
          } else {
            yearRate.setRate(
                currentAccumulatedPrice.subtract(prevAccumulatedPrice)
                    .divide(prevPrice, 4, RoundingMode.HALF_UP));
          }
        } else {
          yearRate.setRate(BigDecimal.valueOf(0));
        }
        yearRates.add(yearRate);
      }
    }
    yearRateRepo.saveAllAndFlush(yearRates);
  }

  private void generateMonthPriceData(@NonNull String fundId, LocalDate statisticDueDate) {
    Fund fund = fundService.findById(fundId)
        .orElseThrow(() -> new RuntimeException("Fund not found with id: " + fundId));
    Company company = companyService.find(Objects.requireNonNull(fund.getCompanyId()));
    for (int year = statisticDueDate.getYear(); year <= LocalDate.now().getYear(); year++) {
      Map<Integer, FundPrice> monthPrices = priceService.findMonthPriceMapById(fundId, year);
      for (Integer key : monthPrices.keySet()) {
        MonthRate monthRate = new MonthRate();
        monthRate.setMonthRateIdentity(new MonthRateIdentity(fundId, year, key));
        monthRate.setName(fund.getName());
        monthRate.setCompanyName(company.getName());
        monthRate.setType(fund.getType());
        if (monthPrices.containsKey(key - 1) && monthPrices.get(key - 1) != null 
            && monthPrices.get(key - 1).getAccumulatedPrice() != null
            && monthPrices.get(key) != null && monthPrices.get(key).getAccumulatedPrice() != null) {
          BigDecimal prevAccumulatedPrice = monthPrices.get(key - 1).getAccumulatedPrice();
          BigDecimal currentAccumulatedPrice = monthPrices.get(key).getAccumulatedPrice();
          BigDecimal prevPrice = monthPrices.get(key - 1).getCurrentPrice();
          
          if (prevAccumulatedPrice.compareTo(BigDecimal.ZERO) == 0 || prevPrice == null || prevPrice.compareTo(BigDecimal.ZERO) == 0) {
            monthRate.setRate(BigDecimal.ZERO);
          } else {
            monthRate.setRate(
                currentAccumulatedPrice.subtract(prevAccumulatedPrice)
                    .divide(prevPrice, 4, RoundingMode.HALF_UP));
          }
        } else {
          Price price = null;
          if (key == 1) {
            LocalDate endOfLastYear = LocalDate.of(year - 1, 12, 31);
            price = priceService.findLatestPriceBeforeDate(fundId, Objects.requireNonNull(endOfLastYear));
          }
          if (price == null) {
            LocalDate startOfMonth = LocalDate.of(year, key, 1);
            price = priceService.findEarliestPriceAfterDate(fundId, Objects.requireNonNull(startOfMonth));
          }
          if (price == null) {
            monthRate.setRate(BigDecimal.valueOf(0L));
          } else {
            if (price.getAccumulatedPrice() == null || price.getPrice() == null 
              || price.getAccumulatedPrice().compareTo(BigDecimal.ZERO) == 0
              || price.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            monthRate.setRate(BigDecimal.ZERO);
          } else {
            monthRate.setRate(monthPrices.get(key).getAccumulatedPrice().subtract(price.getAccumulatedPrice())
                .divide(price.getPrice(), 4, RoundingMode.HALF_UP));
          }
          }
        }
        monthRateRepo.saveAndFlush(monthRate);
      }
    }
  }

  public PeriodRate generatePeriodRate(@NonNull String fundId) {
    Fund fund = fundService.findById(fundId)
        .orElseThrow(() -> new EntityNotFoundException("Fund not found with id: " + fundId));
    Company company = companyService.find(Objects.requireNonNull(fund.getCompanyId()));
    int currentYear = LocalDate.now().getYear() - 1;
    Map<Integer, FundPrice> yearPrices = priceService.findYearPriceMapById(fundId);
    BigDecimal[] rates = new BigDecimal[10];
    for (int i = 0; i < 10; i++) {
      if (yearPrices.containsKey(currentYear) && yearPrices.get(currentYear) != null
          && yearPrices.get(currentYear).getAccumulatedPrice() != null
          && yearPrices.containsKey(currentYear - i - 1)
          && yearPrices.get(currentYear - i - 1) != null
          && yearPrices.get(currentYear - i - 1).getAccumulatedPrice() != null
          && yearPrices.get(currentYear - i - 1).getCurrentPrice() != null
          && yearPrices.get(currentYear - i - 1).getAccumulatedPrice().compareTo(BigDecimal.ZERO) != 0) {
        BigDecimal currentAccumulatedPrice = yearPrices.get(currentYear).getAccumulatedPrice();
        BigDecimal prevAccumulatedPrice = yearPrices.get(currentYear - i - 1).getAccumulatedPrice();
        BigDecimal prevPrice = yearPrices.get(currentYear - i - 1).getCurrentPrice();
        
        if (prevPrice.compareTo(BigDecimal.ZERO) != 0) {
          BigDecimal rate = currentAccumulatedPrice
              .subtract(prevAccumulatedPrice)
              .divide(prevPrice, 4, RoundingMode.HALF_UP);
          rates[i] = rate;
        } else {
          rates[i] = BigDecimal.ZERO;
        }
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
  public Page<PeriodRate> getPeriodRateDesc(@NonNull String type, @NonNull Pageable pageable) {
    return periodRateRepo.findByType(type, pageable);
  }

  @Override
  public List<YearRate> getYearRateById(@NonNull String fundId) {
    return yearRateRepo.findAllById(fundId);
  }

  @Override
  public PeriodRate getYearRateByIdAndYear(@NonNull String fundId) {
    return periodRateRepo.findById(fundId).orElse(null);
  }

  @Override
  public List<PeriodRate> getYearRateByName(@NonNull String fundName) {
    return periodRateRepo.findByNameContaining(fundName);
  }

  @Override
  public Page<YearRate> getYearRateByTypesAndYear(@NonNull List<String> types, int year, @NonNull Pageable pageable) {
    return yearRateRepo.findAllByTypesAndYear(types, year, pageable);
  }

  @Override
  public List<YearAverageRate> getYearAverageRankByTypesAndPeriod(@NonNull List<String> types, int period,
      @NonNull Pageable pageable) {
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
  public Boolean generateStatisticData(@NonNull List<String> types, boolean refreshAllData) {
    return generate(types, refreshAllData);
  }

}
