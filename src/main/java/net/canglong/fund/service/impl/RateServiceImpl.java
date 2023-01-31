package net.canglong.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.*;
import net.canglong.fund.repository.MonthRateRepo;
import net.canglong.fund.repository.PeriodRateRepo;
import net.canglong.fund.repository.YearRateRepo;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.RateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Log4j2
public class RateServiceImpl implements RateService {
    private final PeriodRateRepo periodRateRepo;

    public RateServiceImpl(PeriodRateRepo periodRateRepo) {
        this.periodRateRepo = periodRateRepo;
    }

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

    @Override
    public List<MonthRate> getMonthRateById(String fundId) {
        return monthRateRepo.findAllById(fundId);
    }

    @Override
    public Boolean generate(String fundId, boolean refreshPreviousData) {
        if (refreshPreviousData) {
            generateYearPriceData(fundId, LocalDate.of(1970, 1, 1));
            generateMonthPriceData(fundId, LocalDate.of(1970, 1, 1));
            generatePeriodRate(fundId);
        } else {
            LocalDate statisticDueDate = fundService.findById(fundId).getStatisticDueDate();
            if (statisticDueDate == null) {
                statisticDueDate = LocalDate.of(1970, 1, 1);
            }
            if (LocalDate.now().isAfter(statisticDueDate.plusMonths(1))) {
                generateYearPriceData(fundId, statisticDueDate);
                generateMonthPriceData(fundId, statisticDueDate);

            }
            if (LocalDate.now().getYear() != statisticDueDate.getYear()) {
                generatePeriodRate(fundId);
            }
        }
        Fund fund = fundService.findById(fundId);
        fund.setStatisticDueDate(LocalDate.now());
        fundService.update(fund);
        return true;
    }

    private List<YearRate> generateYearPriceData(String fundId, LocalDate statisticDueDate) {
        Map<Integer, BigDecimal> yearPrices = priceService.findYearPriceMapById(fundId);
        List<YearRate> yearRates = new ArrayList<YearRate>();
        for (Integer key : yearPrices.keySet()) {
            if (key > statisticDueDate.getYear()) {
                YearRate yearRate = new YearRate();
                yearRate.setYearRateIdentity(new YearRateIdentity(fundId, key));
                if (yearPrices.keySet().contains(key - 1) && yearPrices.get(key - 1) != null) {
                    if (yearPrices.get(key - 1).intValue() == 0) {
                        yearRate.setRate(BigDecimal.valueOf(0));
                    } else {
                        yearRate.setRate(yearPrices.get(key).subtract(yearPrices.get(key - 1)).divide(yearPrices.get(key - 1), 4, RoundingMode.HALF_UP));
                    }
                } else {
                    yearRate.setRate(BigDecimal.valueOf(0));
                }
                yearRates.add(yearRate);
            }
        }
        return yearRateRepo.saveAllAndFlush(yearRates);
    }

    private void generateMonthPriceData(String fundId, LocalDate statisticDueDate) {
        Price priceAtCreation = priceService.findStartDateById(fundId);
        for (int year = statisticDueDate.getYear(); year <= LocalDate.now().getYear(); year++) {
            Map<Integer, BigDecimal> monthPrices = priceService.findMonthPriceMapById(fundId, year);
            for (Integer key : monthPrices.keySet()) {
                MonthRate monthRate = new MonthRate();
                monthRate.setMonthRateIdentity(new MonthRateIdentity(fundId, year, key));
                if (monthPrices.keySet().contains(key - 1) && monthPrices.get(key - 1) != null) {
                    if (monthPrices.get(key - 1).intValue() == 0) {
                        monthRate.setRate(BigDecimal.valueOf(0));
                    } else {
                        monthRate.setRate(monthPrices.get(key).subtract(monthPrices.get(key - 1)).divide(monthPrices.get(key - 1), 4, RoundingMode.HALF_UP));
                    }
                } else {
                    Price price = null;
                    if (key == 1) {
                        price = priceService.findLatestPrice(fundId, LocalDate.of(year - 1, 12, 31));
                    }
                    if (price == null) {
                        price = priceService.findEarliestPrice(fundId, LocalDate.of(year, key, 1));
                    }
                    if (price == null) {
                        monthRate.setRate(BigDecimal.valueOf(0L));
                    } else {
                        if (price.getAccumulatedPrice().intValue() == 0) {
                            monthRate.setRate(BigDecimal.valueOf(0));
                        } else {
                            monthRate.setRate(monthPrices.get(key).subtract(price.getAccumulatedPrice()).divide(price.getAccumulatedPrice(), 4, RoundingMode.HALF_UP));
                        }
                    }
                }
                monthRateRepo.saveAndFlush(monthRate);
            }
        }
    }

    private void generatePeriodRate(String fundId) {
        Fund fund = fundService.findById(fundId);
        Company company = companyService.find(fund.getCompanyId());
        int currentYear = LocalDate.now().getYear() - 1;
        Map<Integer, BigDecimal> yearPrices = priceService.findYearPriceMapById(fundId);
        BigDecimal[] rates = new BigDecimal[10];
        for (int i = 0; i < 10; i++) {
            if (yearPrices.get(currentYear) != null && yearPrices.get(currentYear - i - 1) != null && yearPrices.get(currentYear - i - 1).intValue() != 0) {
                BigDecimal rate = yearPrices.get(currentYear).subtract(yearPrices.get(currentYear - i - 1)).divide(yearPrices.get(currentYear - i - 1), 4, RoundingMode.HALF_UP);
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
        periodRateRepo.save(periodRate);
    }

    @Override
    public Boolean generate(boolean refreshPreviousData) {
        List<Fund> allStockFunds = fundService.findAllExcludesType("货币型");
        int size = allStockFunds.size();
        int count = 0;
        for (Fund fund : allStockFunds) {
            generate(fund.getId(), refreshPreviousData);
            if (++count % 100 == 0) {
                log.info("Completed generating statistic data for " + count + " of " + size + " funds.");
            }
        }
        return true;
    }

    @Override
    public Page<PeriodRate> getOneYearRateDesc(String type, Pageable pageable) {
        return periodRateRepo.getPeriodRateOrderByOneYearRateDesc(type, pageable);
    }

    @Override
    public Page<PeriodRate> getThreeYearRateDesc(String type, Pageable pageable) {
        return periodRateRepo.getPeriodRateOrderByThreeYearRateDesc(type, pageable);
    }

    @Override
    public Page<PeriodRate> getFiveYearRateDesc(String type, Pageable pageable) {
        return periodRateRepo.getPeriodRateOrderByFiveYearRateDesc(type, pageable);
    }

    @Override
    public Page<PeriodRate> getEightYearRateDesc(String type, Pageable pageable) {
        return periodRateRepo.getPeriodRateOrderByEightYearRateDesc(type, pageable);
    }

    @Override
    public Page<PeriodRate> getTenYearRateDesc(String type, Pageable pageable) {
        return periodRateRepo.getPeriodRateOrderByTenYearRateDesc(type, pageable);
    }

    @Override
    public List<YearRate> getYearRateById(String fundId) {
        return yearRateRepo.findAllById(fundId);
    }

}
