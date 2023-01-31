package net.canglong.fund.service;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.PeriodRate;
import net.canglong.fund.entity.YearRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RateService {
    List<MonthRate> getMonthRateById(String fundId);
    List<YearRate> getYearRateById(String fundId);
    Boolean generate(String fundId, boolean refreshPreviousData);
    Boolean generate(boolean refreshPreviousData);
    Page<PeriodRate> getOneYearRateDesc(String type,Pageable pageable);
    Page<PeriodRate> getThreeYearRateDesc(String type,Pageable pageable);
    Page<PeriodRate> getFiveYearRateDesc(String type,Pageable pageable);
    Page<PeriodRate> getEightYearRateDesc(String type,Pageable pageable);
    Page<PeriodRate> getTenYearRateDesc(String type,Pageable pageable);
}
