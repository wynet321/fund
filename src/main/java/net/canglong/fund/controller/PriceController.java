package net.canglong.fund.controller;

import net.canglong.fund.service.PriceService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/price")
public class PriceController {

    @Resource
    private PriceService priceService;

    @GetMapping(value = "/{id}")
    public Object get(@PathVariable("id") String id, Pageable pageable) throws Exception {
        return priceService.findByFundId(id, pageable);
    }

    @GetMapping(value = "/month_avg/{id}/start/{date}")
    public Object getMonthAveragePrice(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE) LocalDate startDate)
            throws Exception {
        return priceService.findAllMonthAveragePriceByFundId(id, startDate);
    }

    @PostMapping(value = "/{id}")
    public Object create(@PathVariable("id") String id) throws Exception {
        return priceService.create(id);
    }

    @GetMapping(value = "/{id}/{date}")
    public Object getByFundIdAndDate(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws Exception {
        return priceService.findByFundIdDate(id, date);
    }

    @GetMapping(value = "/{id}/start/{date}")
    public Object getPriceFromDate(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            Pageable pageable)
            throws Exception {
        return priceService.find(id, startDate, pageable);
    }
}
