package net.canglong.fund.service;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.YearRate;

import java.util.List;

public interface RateService {

    List<MonthRate> getMonthRateByFundId(String fundId);
    
    List<YearRate> getYearRateByFundId(String fundId);

    void generate(String fundId, boolean refreshPreviousData);

    void generate(boolean refreshPreviousData);
}
