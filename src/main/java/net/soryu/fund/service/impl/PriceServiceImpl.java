package net.soryu.fund.service.impl;

import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.MonthAveragePrice;
import net.soryu.fund.entity.Price;
import net.soryu.fund.entity.PriceIdentity;
import net.soryu.fund.repository.FundRepo;
import net.soryu.fund.repository.PriceRepo;
import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.FundService;
import net.soryu.fund.service.PriceService;
import net.soryu.fund.service.WebsiteDataService;
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
    private PriceRepo priceRepo;
    @Resource
    private FundRepo fundRepo;
    @Resource
    private WebsiteDataService dataService;
    @Resource
    private FundService fundService;
    @Resource
    private CompanyService companyService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<Price> findByFundName(String name, Pageable pageable) throws Exception {
        Fund fund = new Fund();
        fund.setName(name);
        Optional<Fund> optional = fundRepo.findOne(Example.of(fund));
        if (optional.isPresent()) {
            Price price = new Price();
            price.setPriceIdentity(new PriceIdentity(optional.get().getId(), null));
            return priceRepo.findAll(Example.of(price), pageable);
        }
        return null;
    }

    @Override
    public Page<Price> findByFundId(String id, Pageable pageable) throws Exception {
        return priceRepo.find(id, pageable);
    }

    @Override
    public Integer create(String fundId) throws Exception {
        int page = 0;
        int count = 0;
        Optional<Fund> optional = fundRepo.findById(fundId);
        if (optional.isPresent()) {
            Fund fund = optional.get();
            page = fund.getCurrentPage();
            logger.debug("Start to collect fund " + fund.getName());
            List<Price> prices;
            do {
                prices = dataService.getPrices(fund, page++);
                prices = priceRepo.saveAll(prices);
                count += prices.size();
                if (count % 1000 == 0) {
                    logger.debug(fund.getName() + " completed " + count + " records.");
                }
            } while (!prices.isEmpty());
            logger.info(fund.getName() + " total " + count + " records.");
            fund.setCurrentPage(page - 1);
            fundRepo.save(fund);
        }
        return count;
    }

    @Override
    public Page<Price> find(String id, LocalDate startDate, Pageable pageable) throws Exception {
        return priceRepo.find(id, startDate, pageable);
    }

    @Override
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) throws Exception {
        return priceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
    }

    @Override
    public Price findByFundIdDate(String id, LocalDate date) throws Exception {
        return priceRepo.find(id, date);
    }

}
