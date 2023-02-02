package net.canglong.fund.controller;

import net.canglong.fund.service.PriceService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/price")
public class PriceController {

    @Resource
    private PriceService priceService;

    @GetMapping(value = "/{id}")
    public Object get(@PathVariable("id") String id, Pageable pageable) {
        return priceService.findByFundId(id, pageable);
    }

    @GetMapping(value = "/month_avg/{id}/start/{date}")
    public Object getMonthAveragePrice(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return priceService.findAllMonthAveragePriceByFundId(id, startDate);
    }

    @PostMapping(value = "/{id}")
    public Object create(@PathVariable("id") String id) throws Exception {
        return priceService.create(id);
    }

    @GetMapping(value = "/{id}/{date}")
    public Object getByFundIdAndDate(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return priceService.findLatestPriceAfterDate(id, date);
    }

    @GetMapping(value = "/{id}/start/{date}")
    public Object getPriceFromDate(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   Pageable pageable) {
        return priceService.find(id, startDate, pageable);
    }

    @GetMapping(value = "/{id}/start/{startDate}/end/{endDate}")
    public Object find(@PathVariable("id") String id, @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws Exception{
        return priceService.findPercentageByDate(id, startDate, endDate);
    }

    @GetMapping(value="/{id}/startDate")
    public Object findStartDate(@PathVariable("id") String id){
        return priceService.findStartDateById(id);
    }

    @GetMapping(value="/{id}/priceAtYearStart")
    public Object findYearPriceById(@PathVariable("id") String id){
        return priceService.findYearPriceById(id);
    }
}
