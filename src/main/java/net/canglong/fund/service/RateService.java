package net.canglong.fund.service;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.YearRate;

import java.util.List;

public interface RateService {

    List<MonthRate> getMonthRateById(String fundId);
    
    List<YearRate> getYearRateById(String fundId);

    Boolean generate(String fundId, boolean refreshPreviousData);

    Boolean generate(boolean refreshPreviousData);
}
