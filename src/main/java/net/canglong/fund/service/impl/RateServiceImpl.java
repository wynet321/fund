package net.canglong.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.*;
import net.canglong.fund.repository.MonthRateRepo;
import net.canglong.fund.repository.YearRateRepo;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.RateService;
import net.canglong.fund.vo.DatePriceIdentity;
import net.canglong.fund.vo.MonthPrice;
import net.canglong.fund.vo.YearPrice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Log4j2
public class RateServiceImpl implements RateService {

    @Resource
    private MonthRateRepo monthRateRepo;
    @Resource
    private YearRateRepo yearRateRepo;
    @Resource
    private FundService fundService;

    @Resource
    private PriceService priceService;

    @Override
    public List<MonthRate> getMonthRateById(String fundId) {
        return monthRateRepo.findAllById(fundId);
    }

    @Override
    public Boolean generate(String fundId, boolean refreshPreviousData) {
        if (refreshPreviousData) {
            generateYearPriceData(fundId, LocalDate.of(1970, 1, 1));
            generateMonthPriceData(fundId, LocalDate.of(1970, 1, 1));
        } else {
            LocalDate statisticDueDate = fundService.findById(fundId).getStatisticDueDate();
            if (statisticDueDate == null) {
                statisticDueDate = LocalDate.of(1970, 1, 1);
            }
            if (LocalDate.now().isAfter(statisticDueDate.plusMonths(1))) {
                generateYearPriceData(fundId, statisticDueDate);
                generateMonthPriceData(fundId, statisticDueDate);
            }
        }
        Fund fund = fundService.findById(fundId);
        fund.setStatisticDueDate(LocalDate.now());
        fundService.update(fund);
        return null;
    }

    private List<YearRate> generateYearPriceData(String fundId, LocalDate statisticDueDate) {
        YearPrice yearPrice = priceService.findYearPriceById(fundId);
        List<DatePriceIdentity> datePriceIdentities = yearPrice.getPriceList();
        Map<Integer, BigDecimal> datePrices = new HashMap<Integer, BigDecimal>();
        for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
            datePrices.put(datePriceIdentity.getPriceDate().getYear(), datePriceIdentity.getAccumulatedPrice());
        }
        List<YearRate> yearRates = new ArrayList<YearRate>();
        for (Integer key : datePrices.keySet()) {
            if (key > statisticDueDate.getYear()) {
                YearRate yearRate = new YearRate();
                yearRate.setYearRateIdentity(new YearRateIdentity(fundId, key));
                if (datePrices.keySet().contains(key - 1) && datePrices.get(key - 1) != null) {
                    if (datePrices.get(key - 1).intValue() == 0) {
                        yearRate.setRate(BigDecimal.valueOf(0));
                    } else {
                        yearRate.setRate(datePrices.get(key).subtract(datePrices.get(key - 1)).divide(datePrices.get(key - 1), 4, RoundingMode.HALF_UP));
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
            MonthPrice monthPrice = priceService.findMonthPriceById(fundId, year);
            List<DatePriceIdentity> datePriceIdentities = monthPrice.getPriceList();
            Map<Integer, BigDecimal> datePrices = new HashMap<Integer, BigDecimal>();
            for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
                datePrices.put(datePriceIdentity.getPriceDate().getMonthValue(), datePriceIdentity.getAccumulatedPrice());
            }
            for (Integer key : datePrices.keySet()) {
                MonthRate monthRate = new MonthRate();
                monthRate.setMonthRateIdentity(new MonthRateIdentity(fundId, year, key));
                if (datePrices.keySet().contains(key - 1) && datePrices.get(key - 1) != null) {
                    if (datePrices.get(key - 1).intValue() == 0) {
                        monthRate.setRate(BigDecimal.valueOf(0));
                    } else {
                        monthRate.setRate(datePrices.get(key).subtract(datePrices.get(key - 1)).divide(datePrices.get(key - 1), 4, RoundingMode.HALF_UP));
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
                            monthRate.setRate(datePrices.get(key).subtract(price.getAccumulatedPrice()).divide(price.getAccumulatedPrice(), 4, RoundingMode.HALF_UP));
                        }
                    }
                }
                monthRateRepo.saveAndFlush(monthRate);
            }
        }
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
    public List<YearRate> getYearRateById(String fundId) {
        return yearRateRepo.findAllById(fundId);
    }

}
