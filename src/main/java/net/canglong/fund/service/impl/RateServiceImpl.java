package net.canglong.fund.service.impl;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.repository.MonthRateRepo;
import net.canglong.fund.repository.YearRateRepo;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.RateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


@Service
public class RateServiceImpl implements RateService {

    @Resource
    private MonthRateRepo monthRateRepo;
    @Resource
    private YearRateRepo yearRateRepo;
    @Resource
    private FundService fundService;

    @Override
    public List<MonthRate> getMonthRateByFundId(String fundId) {
        return monthRateRepo.findAllById(fundId);
    }

    @Override
    public void generate(String fundId, boolean refreshPreviousData) {
        if (refreshPreviousData) {
            generateStatisticData(fundId, LocalDate.of(1970, 1, 1));
        } else {
            LocalDate statisticDueDate = fundService.findById(fundId).getStatisticDueDate();
            if (LocalDate.now().isAfter(statisticDueDate)) {
                generateStatisticData(fundId, statisticDueDate);
            }
        }
    }

    private void generateStatisticData(String fundId, LocalDate statisticDueDate) {
        // TODO Auto-generated method stub
    }

    @Override
    public void generate(boolean refreshPreviousData) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<YearRate> getYearRateByFundId(String fundId) {
        return yearRateRepo.findAllById(fundId);
    }

}
