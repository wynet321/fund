package net.soryu.fund.service;

import net.soryu.fund.entity.Price;
import net.soryu.fund.entity.MonthAveragePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

public interface PriceService {

    Page<Price> findByName(String Name, Pageable pageable) throws Exception;

    Page<Price> findByFundId(String id, Pageable pageable) throws Exception;

    Price findByFundIdDate(String id, LocalDate date) throws Exception;

    Page<Price> find(String id, LocalDate startDate, Pageable pageable) throws Exception;

    List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception;
    
    @Transactional
    Integer create(String fundId) throws Exception;

}
