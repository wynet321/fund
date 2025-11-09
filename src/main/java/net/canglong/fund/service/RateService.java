package net.canglong.fund.service;

import java.util.List;
import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.PeriodRate;
import net.canglong.fund.entity.Status;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.vo.YearAverageRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface RateService {

  List<YearRate> getYearRateById(@NonNull String fundId);

  PeriodRate getYearRateByIdAndYear(@NonNull String fundId);

  List<PeriodRate> getYearRateByName(@NonNull String fundName);

  Boolean generate(@NonNull String fundId, boolean refreshAllData) throws Exception;

  List<MonthRate> getMonthRateById(@NonNull String fundId, int year);

  Boolean generate(@NonNull List<String> types, boolean refreshAllData) throws Exception;

  Status getStatisticJobStatus();

  Page<PeriodRate> getPeriodRateDesc(@NonNull String type, @NonNull Pageable pageable);

  PeriodRate generatePeriodRate(@NonNull String fundId) throws Exception;

  Page<YearRate> getYearRateByTypesAndYear(@NonNull List<String> types, int year, @NonNull Pageable pageable);

  List<YearAverageRate> getYearAverageRankByTypesAndPeriod(@NonNull List<String> types, int period,
      @NonNull Pageable pageable);

  Boolean generateStatisticData(@NonNull List<String> types, boolean refreshAllData);

}
