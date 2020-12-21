package net.cheetahead.fund.controller;

import net.cheetahead.fund.service.PriceService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/api/price")
public class PriceController {

    @Resource
    private PriceService service;

    @GetMapping(value = "/{id}")
    public Object get(@PathVariable("id") String id, Pageable pageable) throws Exception {
        return service.findByFundId(id, pageable);
    }

    @GetMapping(value = "/{id}/{date}")
    public Object get(@PathVariable("id") String id, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) throws Exception {
        return service.findAllMonthAveragePriceByFundId(id, startDate);
    }

    @PostMapping(value = "/{id}")
    public Object create(@PathVariable("id") String id, Pageable pageable) throws Exception {
        return service.create(id);
    }

}
