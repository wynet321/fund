package net.canglong.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.PriceIdentity;
import net.canglong.fund.repository.PriceRepo;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.WebsiteDataService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

@Log4j2
@Service
public class PriceServiceImpl implements PriceService {

    @Resource
    private PriceRepo priceRepo;
    @Resource
    private WebsiteDataService dataService;
    @Resource
    private FundService fundService;
    @Resource
    private CompanyService companyService;

    @Override
    public Page<Price> findByName(String name, Pageable pageable) throws Exception {
        Fund fund = fundService.findByName(name);
        if (fund != null) {
            Price price = new Price();
            price.setPriceIdentity(new PriceIdentity(fund.getId(), null));
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
        Fund fund = fundService.findById(fundId);
        if (fund != null) {
            page = fund.getCurrentPage();
            log.debug("Start to collect fund " + fund.getName());
            List<Price> prices;
            do {
                prices = dataService.getPrices(fund, page++);
                prices = priceRepo.saveAll(prices);
                count += prices.size();
                if (count % 1000 == 0) {
                    log.debug(fund.getName() + " completed " + count + " records.");
                }
            } while (!prices.isEmpty());
            log.info(fund.getName() + " total " + count + " records.");
            fund.setCurrentPage(page - 1);
            fundService.create(fund);
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
