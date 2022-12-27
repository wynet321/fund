package net.canglong.fund.service;

import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

public interface PriceService {

    Page<Price> findByName(String Name, Pageable pageable);

    Page<Price> findByFundId(String id, Pageable pageable);

    Price findByFundIdDate(String id, LocalDate date);

    Page<Price> find(String id, LocalDate startDate, Pageable pageable);

    List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate);
    
    @Transactional
    Integer create(String fundId) throws Exception;

}
