package net.cheetahead.fund.service;

import net.cheetahead.fund.entity.FundPrice;
import net.cheetahead.fund.entity.MonthAveragePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

public interface PriceService {

    Page<FundPrice> findByFundName(String Name, Pageable pageable) throws Exception;

    Page<FundPrice> findByFundId(String id, Pageable pageable) throws Exception;

    List<FundPrice> findByFundId(String id,  LocalDate startDate) throws Exception;

    List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception;
    
    @Transactional
    Integer create(String fundId) throws Exception;

}
