package net.soryu.fund.service.impl;

import net.soryu.fund.entity.CurrencyFundPrice;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.MonthAveragePrice;
import net.soryu.fund.entity.NonCurrencyFundPrice;
import net.soryu.fund.entity.PriceIdentity;
import net.soryu.fund.repository.CurrencyFundPriceRepo;
import net.soryu.fund.repository.FundRepo;
import net.soryu.fund.repository.NonCurrencyFundPriceRepo;
import net.soryu.fund.service.DataService;
import net.soryu.fund.service.PriceService;
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
    private NonCurrencyFundPriceRepo fundPriceRepo;

    @Resource
    private CurrencyFundPriceRepo currencyPriceRepo;

    @Resource
    private FundRepo fundRepository;

    @Resource
    private DataService dataService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<NonCurrencyFundPrice> findByFundName(String name, Pageable pageable) throws Exception {
        Fund fund = new Fund();
        fund.setName(name);
        Optional<Fund> optional = fundRepository.findOne(Example.of(fund));
        if (optional.isPresent()) {
            NonCurrencyFundPrice price = new NonCurrencyFundPrice();
            price.setPriceIdentity(new PriceIdentity(optional.get().getId(), null));
            return fundPriceRepo.findAll(Example.of(price), pageable);
        }
        return null;
    }

    @Override
    public Page<NonCurrencyFundPrice> findByFundId(String id, Pageable pageable) throws Exception {
        NonCurrencyFundPrice price = new NonCurrencyFundPrice();
        price.setPriceIdentity(new PriceIdentity(id, null));
        return fundPriceRepo.findAllByFundId(id, pageable);
    }

    // TODO implement currency and non currency into one class.
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
                    List<?> prices = dataService.getFundPriceFromWebsite(fund, page++);
                    if (prices.isEmpty()) {
                        break;
                    }
                    currencyPriceRepo.saveAll((List<CurrencyFundPrice>) prices);
                    count += prices.size();
                    if (count % 1000 == 0) {
                        logger.info(fund.getName() + " completed " + count + " records.");
                    }
                }
            } else {
                while (true) {
                    List<?> prices = dataService.getFundPriceFromWebsite(fund, page++);
                    if (prices.isEmpty()) {
                        break;
                    }
                    fundPriceRepo.saveAll((List<NonCurrencyFundPrice>) prices);
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
    public List<NonCurrencyFundPrice> findByFundId(String id, LocalDate startDate) throws Exception {
        return fundPriceRepo.findAllByIdentity(id, startDate);
    }

    @Override
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception {
        return fundPriceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
    }

}
