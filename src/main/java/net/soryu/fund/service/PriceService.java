package net.soryu.fund.service;

import net.soryu.fund.entity.NonCurrencyFundPrice;
import net.soryu.fund.entity.MonthAveragePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

public interface PriceService {

    Page<NonCurrencyFundPrice> findByFundName(String Name, Pageable pageable) throws Exception;

    Page<NonCurrencyFundPrice> findByFundId(String id, Pageable pageable) throws Exception;

    List<NonCurrencyFundPrice> findByFundId(String id,  LocalDate startDate) throws Exception;

    List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception;
    
    @Transactional
    Integer create(String fundId) throws Exception;

}
