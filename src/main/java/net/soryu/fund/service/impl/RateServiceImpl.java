package net.soryu.fund.service.impl;

import net.soryu.fund.entity.MonthRate;
import net.soryu.fund.entity.YearRate;
import net.soryu.fund.repository.MonthRateRepo;
import net.soryu.fund.repository.YearRateRepo;
import net.soryu.fund.service.FundService;
import net.soryu.fund.service.PriceService;
import net.soryu.fund.service.RateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

@Service
public class RateServiceImpl implements RateService {

    @Resource
    private MonthRateRepo monthRateRepo;

    @Resource
    private YearRateRepo yearRateRepo;

    @Resource
    private PriceService priceService;

    @Resource
    private FundService fundService;

    @Override
    public List<MonthRate> getMonthRateByFundId(String fundId) throws Exception {
        return monthRateRepo.findAllById(fundId);
    }

    @Override
    public boolean generate(String fundId, boolean refreshPreviousData) throws Exception {
        if (refreshPreviousData) {
            generateStatisticData(fundId, LocalDate.of(1970, 1, 1));
        } else {
            LocalDate statisticDueDate = fundService.findById(fundId).getStatisticDueDate();
            if (LocalDate.now().compareTo(statisticDueDate) > 0) {
                generateStatisticData(fundId, statisticDueDate);
            }
        }
        return true;
    }

    private void generateStatisticData(String fundId, LocalDate statisticDueDate) throws Exception {
        // List<FundPrice> fundPrices = priceService.findByFundId(fundId, statisticDueDate);
        // for(FundPrice price:fundPrices) {
        //
        // }
    }

    @Override
    public boolean generate(boolean refreshPreviousData) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<YearRate> getYearRateByFundId(String fundId) throws Exception {
        return yearRateRepo.findAllById(fundId);
    }

}
