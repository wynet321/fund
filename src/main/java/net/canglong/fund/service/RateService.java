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
    Boolean generate(String fundId, boolean refreshAllData);
    Boolean generate(boolean refreshAllData);
    Page<PeriodRate> getOneYearRateDesc(List<String> types,Pageable pageable);
    Page<PeriodRate> getThreeYearRateDesc(List<String> types,Pageable pageable);
    Page<PeriodRate> getFiveYearRateDesc(List<String> types,Pageable pageable);
    Page<PeriodRate> getEightYearRateDesc(List<String> types,Pageable pageable);
    Page<PeriodRate> getTenYearRateDesc(List<String> types,Pageable pageable);
    PeriodRate generatePeriodRate(String fundId);
}
