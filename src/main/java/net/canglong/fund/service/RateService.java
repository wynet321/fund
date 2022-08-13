package net.canglong.fund.service;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.YearRate;

import java.util.List;

public interface RateService {

    List<MonthRate> getMonthRateByFundId(String fundId) throws Exception;
    
    List<YearRate> getYearRateByFundId(String fundId) throws Exception;

    boolean generate(String fundId, boolean refreshPreviousData) throws Exception;

    boolean generate(boolean refreshPreviousData) throws Exception;
}
