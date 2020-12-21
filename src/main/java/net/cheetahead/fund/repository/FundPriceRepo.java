package net.cheetahead.fund.repository;

import net.cheetahead.fund.entity.FundPrice;
import net.cheetahead.fund.entity.MonthAveragePrice;
import net.cheetahead.fund.entity.PriceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FundPriceRepo extends JpaRepository<FundPrice, PriceIdentity> {

    @Query(value = "select entity from FundPrice entity where fundId=?1 and date>=?2")
    public List<FundPrice> findAllByIdentity(String fundId, LocalDate startDate);

    @Query(value = "select priceIdentity.fundId, priceIdentity.date, price, accumulatedPrice from FundPrice where FundPrice.priceIdentity.fundId=?1")
    public Page<FundPrice> findAllByFundId(String fundId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select fund_id as id, cast(avg(accumulated_price) as decimal(7,4)) as averagePrice, year(date)*100+month(date) as month from fund_price where fund_id=?1 and date>?2 group by year(date), month(date) order by month asc")
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate);
}
