package net.cheetahead.fund.service.impl;

import net.cheetahead.fund.entity.CurrencyPrice;
import net.cheetahead.fund.entity.Fund;
import net.cheetahead.fund.entity.FundPrice;
import net.cheetahead.fund.entity.MonthAveragePrice;
import net.cheetahead.fund.entity.PriceIdentity;
import net.cheetahead.fund.repository.CurrencyPriceRepo;
import net.cheetahead.fund.repository.FundPriceRepo;
import net.cheetahead.fund.repository.FundRepo;
import net.cheetahead.fund.service.DataService;
import net.cheetahead.fund.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

@Service
public class PriceServiceImpl implements PriceService {

    @Resource
    private FundPriceRepo fundPriceRepo;

    @Resource
    private CurrencyPriceRepo currencyPriceRepo;

    @Resource
    private FundRepo fundRepository;

    @Resource
    private DataService dataService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<FundPrice> findByFundName(String name, Pageable pageable) throws Exception {
        Fund fund = new Fund();
        fund.setName(name);
        Optional<Fund> optional = fundRepository.findOne(Example.of(fund));
        if (optional.isPresent()) {
            FundPrice price = new FundPrice();
            price.setPriceIdentity(new PriceIdentity(optional.get().getId(), null));
            return fundPriceRepo.findAll(Example.of(price), pageable);
        }
        return null;
    }

    @Override
    public Page<FundPrice> findByFundId(String id, Pageable pageable) throws Exception {
        FundPrice price = new FundPrice();
        price.setPriceIdentity(new PriceIdentity(id, null));
        return fundPriceRepo.findAllByFundId(id, pageable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer create(String fundId) throws Exception {
        int page = 0;
        int count = 0;
        Optional<Fund> optional = fundRepository.findById(fundId);
        if (optional.isPresent()) {
            Fund fund = optional.get();
            page = fund.getCurrentPage();
            boolean isCurrencyFund = fund.getType().equals("货币型");
            if (isCurrencyFund) {
                while (true) {
                    List<?> prices = dataService.fetchCurrencyPrice(fundId, page++);
                    if (prices.isEmpty()) {
                        break;
                    }
                    currencyPriceRepo.saveAll((List<CurrencyPrice>) prices);
                    count += prices.size();
                    if (count % 1000 == 0) {
                        logger.info(fund.getName() + " completed " + count + " records.");
                    }
                }
            } else {
                while (true) {
                    List<?> prices = dataService.fetchFundPrice(fundId, page++);
                    if (prices.isEmpty()) {
                        break;
                    }
                    fundPriceRepo.saveAll((List<FundPrice>) prices);
                    count += prices.size();
                    if (count % 1000 == 0) {
                        logger.info(fund.getName() + " completed " + count + " records.");
                    }
                }
            }
            fund.setCurrentPage(page - 1);
            fundRepository.save(fund);
        }
        return count;
    }

    @Override
    public List<FundPrice> findByFundId(String id, LocalDate startDate) throws Exception {
        return fundPriceRepo.findAllByIdentity(id, startDate);
    }

    @Override
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception {
        return fundPriceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
    }

}
