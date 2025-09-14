package net.canglong.fund.service;

import java.util.List;
import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.PeriodRate;
import net.canglong.fund.entity.Status;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.vo.YearAverageRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RateService {

  List<YearRate> getYearRateById(String fundId);

  PeriodRate getYearRateByIdAndYear(String fundId);

  List<PeriodRate> getYearRateByName(String fundName);

  Boolean generate(String fundId, boolean refreshAllData) throws Exception;

  List<MonthRate> getMonthRateById(String fundId, int year);

  Boolean generate(List<String> types, boolean refreshAllData) throws Exception;

  Status getStatisticJobStatus();

  Page<PeriodRate> getPeriodRateDesc(String type, Pageable pageable);

  PeriodRate generatePeriodRate(String fundId) throws Exception;

  Page<YearRate> getYearRateByTypesAndYear(List<String> types, int year, Pageable pageable);

  List<YearAverageRate> getYearAverageRankByTypesAndPeriod(List<String> types, int period,
      Pageable pageable);

  Boolean generateStatisticData(List<String> types, boolean refreshAllData);

}
