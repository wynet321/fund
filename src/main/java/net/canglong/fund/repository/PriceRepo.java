package net.canglong.fund.repository;

import java.time.LocalDate;
import java.util.List;
import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.PriceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepo extends JpaRepository<Price, PriceIdentity> {

  @Query(nativeQuery = true, countProjection = "*",
      value = "select fund_id, date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and price_date>=:startDate")
  Page<Price> findPriceSet(@Param("fundId") String fundId, @Param("startDate") LocalDate startDate,
      Pageable pageable);

  @Query(nativeQuery = true, countProjection = "*",
      value = "select fund_id, price_date, price, accumulated_price,return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId")
  Page<Price> findLatestPriceBeforeDate(@Param("fundId") String fundId, Pageable pageable);

  @Query(nativeQuery = true,
      value = "select fund_id, cast(avg(accumulated_price) as decimal(7,4)) as averagePrice, year(date)*100+month(date) as month from fund_price where fund_id=:fundId and price_date>:startDate group by year(date), month(date) order by month asc")
  List<MonthAveragePrice> findAllMonthAveragePriceByFundId(@Param("fundId") String fundId,
      @Param("startDate") LocalDate startDate);

  @Query(nativeQuery = true, value = "select fund_id, price_date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and price_date=(select max(price_date) from fund_price where fund_id=:fundId and price_date<=:targetDate)")
  Price findLatestPriceBeforeDate(@Param("fundId") String fundId,
      @Param("targetDate") LocalDate targetDate);

  @Query(nativeQuery = true, value = "select fund_id, price_date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and price_date=(select max(price_date) from fund_price where fund_id=:fundId)")
  Price findLatestPrice(@Param("fundId") String fundId);

  @Query(nativeQuery = true, value = "select fund_id, price_date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and price_date=(select min(price_date) from fund_price where fund_id=:fundId and price_date>=:targetDate)")
  Price findEarliestPriceAfterDate(@Param("fundId") String fundId,
      @Param("targetDate") LocalDate targetDate);

  @Query(nativeQuery = true, value = "select fund_id, price_date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and price_date=(select min(price_date) from fund_price where fund_id=:fundId)")
  Price findPriceAtCreationById(@Param("fundId") String fundId);

}
