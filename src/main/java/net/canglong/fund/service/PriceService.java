package net.canglong.fund.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import jakarta.transaction.Transactional;
import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.Status;
import net.canglong.fund.vo.FundPercentage;
import net.canglong.fund.vo.MonthPrice;
import net.canglong.fund.vo.YearPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import net.canglong.fund.vo.FundPrice;

public interface PriceService {

  Page<Price> findByName(@NonNull String Name, @NonNull Pageable pageable);

  Page<Price> findByFundId(@NonNull String id, @NonNull Pageable pageable);

  Price findLatestPriceBeforeDate(@NonNull String id, @NonNull LocalDate date);

  Page<Price> find(@NonNull String id, @NonNull LocalDate startDate, @NonNull Pageable pageable);

  List<MonthAveragePrice> findAllMonthAveragePriceByFundId(@NonNull String fundId, @NonNull LocalDate startDate);

  @Transactional
  Integer create(@NonNull String fundId) throws Exception;

  FundPercentage findPercentageByDate(@NonNull String id, @NonNull LocalDate startDate, @NonNull LocalDate endDate)
      throws Exception;

  Price findPriceAtCreationById(@NonNull String id);

  YearPrice findYearPriceById(@NonNull String id);

  Map<Integer, FundPrice> findYearPriceMapById(@NonNull String id);

  Map<Integer, FundPrice> findMonthPriceMapById(@NonNull String id, int year);

  MonthPrice findMonthPriceById(@NonNull String id, int year);

  Price findEarliestPriceAfterDate(@NonNull String id, @NonNull LocalDate targetDate);

  LocalDate findLatestPriceDateById(@NonNull String id);

  LocalDate findEarliestPriceDateById(@NonNull String id);

  Boolean startPriceRetrievalJob(int threadCount);

  Status getPriceRetrievalJobStatus();

  boolean stopPriceRetrievalJob();

  List<Price> findByFundIdAndDateRange(String fundId, LocalDate startDate, LocalDate endDate);

}
