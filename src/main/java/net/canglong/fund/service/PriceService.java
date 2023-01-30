package net.canglong.fund.service;

import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.vo.FundPercentage;
import net.canglong.fund.vo.MonthPrice;
import net.canglong.fund.vo.YearPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

public interface PriceService {

    Page<Price> findByName(String Name, Pageable pageable);

    Page<Price> findByFundId(String id, Pageable pageable);

    Price findLatestPrice(String id, LocalDate date);

    Page<Price> find(String id, LocalDate startDate, Pageable pageable);

    List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate);
    
    @Transactional
    Integer create(String fundId) throws Exception;

    FundPercentage findPercentageByDate(String id, LocalDate startDate, LocalDate endDate) throws Exception;

    Price findStartDateById(String id);

    YearPrice findYearPriceById(String id);

    MonthPrice findMonthPriceById(String id, int year);

    Price findEarliestPrice(String id, LocalDate targetDate);
}
