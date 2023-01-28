package net.canglong.fund.service.impl;

import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.PriceIdentity;
import net.canglong.fund.repository.PriceRepo;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.WebsiteDataService;
import net.canglong.fund.vo.DatePriceIdentity;
import net.canglong.fund.vo.FundPercentage;
import net.canglong.fund.vo.PriceAtYearStart;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

@Log4j2
@Service
public class PriceServiceImpl implements PriceService {

    @Resource
    private PriceRepo priceRepo;
    @Resource
    private WebsiteDataService websiteDataService;
    @Resource
    private FundService fundService;

    @Override
    public Page<Price> findByName(String name, Pageable pageable) {
        Fund fund = fundService.findByName(name);
        if (fund != null) {
            Price price = new Price();
            price.setPriceIdentity(new PriceIdentity(fund.getId(), null));
            return priceRepo.findAll(Example.of(price), pageable);
        }
        return null;
    }

    @Override
    public Page<Price> findByFundId(String id, Pageable pageable) {
        return priceRepo.find(id, pageable);
    }

    @Override
    public Integer create(String fundId) throws Exception {
        int page;
        int count = 0;
        Fund fund = fundService.findById(fundId);
        if (fund != null) {
            page = fund.getCurrentPage();
            log.debug("Start to collect fund " + fund.getName());
            List<Price> prices;
            String priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
            do {
                prices = websiteDataService.getPrices(priceWebPage, fund, page - 1);
                prices = priceRepo.saveAll(prices);
                count += prices.size();
                if (count % 1000 == 0) {
                    log.debug(fund.getName() + " completed " + count + " records.");
                }
                priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
            } while (websiteDataService.containsPrice(priceWebPage));
            log.debug(fund.getName() + " total " + count + " records.");
            log.info(fund.getName() + " page " + (page - 1) + " done.");
            fund.setCurrentPage(page - 1);
            fundService.create(fund);
        } else {
            throw new Exception("can't find the fund in DB.");
        }
        return count;
    }

    @Override
    public Object findPercentageByDate(String id, LocalDate startDate, LocalDate endDate) throws Exception {
        Price priceAtStartDate = priceRepo.find(id, startDate);
        Price priceAtEndDate = priceRepo.find(id, endDate);
        BigDecimal ratio = priceAtEndDate.getAccumulatedPrice().subtract(priceAtStartDate.getAccumulatedPrice()).divide(priceAtStartDate.getAccumulatedPrice(), 2, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("0.00%");
        String percentage = df.format(ratio);
        Fund fund = fundService.findById(id);
        FundPercentage fundPercentage = new FundPercentage(id, fund.getName(), percentage, startDate, endDate);
        return fundPercentage;
    }

    @Override
    public Object findStartDateById(String id) {
        return priceRepo.findStartDateById(id);
    }

    @Override
    public Object findPriceAtYearStartById(String id) {
        Price priceAtFundCreation = priceRepo.findStartDateById(id);
        LocalDate fundCreationDate = priceAtFundCreation.getPriceIdentity().getPriceDate();
        LocalDate today = LocalDate.now();
        int years = today.getYear() - fundCreationDate.getYear();
        List<DatePriceIdentity> priceList = new ArrayList<DatePriceIdentity>();
        priceList.add(new DatePriceIdentity(priceAtFundCreation.getPriceIdentity().getPriceDate(), priceAtFundCreation.getAccumulatedPrice()));
        for (int i = 0; i < years; i++) {
            LocalDate requestDate = fundCreationDate.with(firstDayOfYear()).plusYears(1 + i);
            Price requestPrice = priceRepo.find(id, requestDate);
            priceList.add(new DatePriceIdentity(requestPrice.getPriceIdentity().getPriceDate(), requestPrice.getAccumulatedPrice()));
        }
        Fund fund = fundService.findById(id);
        PriceAtYearStart priceAtYearStart = new PriceAtYearStart(id, fund.getName(), priceList);
        return priceAtYearStart;
    }

    @Override
    public Page<Price> find(String id, LocalDate startDate, Pageable pageable) {
        return priceRepo.findPriceSet(id, startDate, pageable);
    }

    @Override
    public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate) {
        return priceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
    }

    @Override
    public Price findByFundIdDate(String id, LocalDate date) {
        return priceRepo.find(id, date);
    }

}
