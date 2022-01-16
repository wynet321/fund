package net.soryu.fund.repository;

import net.soryu.fund.entity.Price;
import net.soryu.fund.entity.MonthAveragePrice;
import net.soryu.fund.entity.PriceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceRepo extends JpaRepository<Price, PriceIdentity> {

    @Query(nativeQuery = true, countProjection = "*",
            value = "select fund_id, date, price, accumulated_price, return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and date>=:startDate")
    public Page<Price> find(@Param("fundId") String fundId, @Param("startDate") LocalDate startDate, Pageable pageable);

    @Query(nativeQuery = true, countProjection = "*",
            value = "select fund_id, date, price, accumulated_price,return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId")
    public Page<Price> find(@Param("fundId") String fundId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select fund_id as id, cast(avg(accumulated_price) as decimal(7,4)) as averagePrice, year(date)*100+month(date) as month from fund_price where fund_id=:fundId and date>:startDate group by year(date), month(date) order by month asc")
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(@Param("fundId") String fundId, @Param("startDate") LocalDate startDate);

    @Query(nativeQuery = true,
            value = "select fund_id as id, date, price, accumulated_price,return_of_ten_kilo, seven_day_annualized_rate_of_return from fund_price where fund_id=:fundId and date=:startDate")
    public Price find(@Param("fundId") String fundId, @Param("startDate") LocalDate date);
}