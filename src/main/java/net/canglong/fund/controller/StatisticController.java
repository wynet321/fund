package net.canglong.fund.controller;

import net.canglong.fund.service.RateService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/statistic", headers = "Accept=application/json")
public class StatisticController {
    @Resource
    private RateService rateService;

    @GetMapping(value = "/month/{id}")
    public Object getMonthRateById(@PathVariable("id") String fundId) {
        return rateService.getMonthRateById(fundId);
    }

    @GetMapping(value = "/year/{id}")
    public Object getYearRateById(@PathVariable("id") String fundId) {
        return rateService.getYearRateById(fundId);
    }

    @PostMapping(value = "/year/{id}")
    public Object createYearRateById(@PathVariable("id") String fundId, @RequestParam("fromCreationDate") boolean fromCreationDate) {
        return rateService.generate(fundId, fromCreationDate);
    }

    @GetMapping(value = "/oneyearrate/{type}")
    public Object getOneYearRateDesc(@PathVariable("type") String type, Pageable pageable) {
        return rateService.getOneYearRateDesc(type, pageable);
    }

    @GetMapping(value = "/threeyearrate/{type}")
    public Object getThreeYearRateDesc(@PathVariable("type") String type, Pageable pageable) {
        return rateService.getThreeYearRateDesc(type, pageable);
    }

    @GetMapping(value = "/fiveyearrate/{type}")
    public Object getFiveYearRateDesc(@PathVariable("type") String type, Pageable pageable) {
        return rateService.getFiveYearRateDesc(type, pageable);
    }

    @GetMapping(value = "/eightyearrate/{type}")
    public Object getEightYearRateDesc(@PathVariable("type") String type, Pageable pageable) {
        return rateService.getEightYearRateDesc(type, pageable);
    }

    @GetMapping(value = "/tenyearrate/{type}")
    public Object getTenYearRateDesc(@PathVariable("type") String type, Pageable pageable) {
        return rateService.getTenYearRateDesc(type, pageable);
    }
}
