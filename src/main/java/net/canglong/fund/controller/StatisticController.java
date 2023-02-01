package net.canglong.fund.controller;

import net.canglong.fund.service.RateService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @PostMapping(value = "/generate")
    public Object generateRate() {
        return rateService.generate(true);
    }

    @PostMapping(value = "/generate/periodrate/{id}")
    public Object generatePeriodRate(@PathVariable("id") String fundId) {
        return rateService.generatePeriodRate(fundId);
    }

    @GetMapping(value = "/oneyearrate/{types}")
    public Object getOneYearRateDesc(@PathVariable("types") List<String> types, Pageable pageable) {
        return rateService.getOneYearRateDesc(types, pageable);
    }

    @GetMapping(value = "/threeyearrate/{types}")
    public Object getThreeYearRateDesc(@PathVariable("types") List<String> types, Pageable pageable) {
        return rateService.getThreeYearRateDesc(types, pageable);
    }

    @GetMapping(value = "/fiveyearrate/{types}")
    public Object getFiveYearRateDesc(@PathVariable("types") List<String> types, Pageable pageable) {
        return rateService.getFiveYearRateDesc(types, pageable);
    }

    @GetMapping(value = "/eightyearrate/{types}")
    public Object getEightYearRateDesc(@PathVariable("types") List<String> types, Pageable pageable) {
        return rateService.getEightYearRateDesc(types, pageable);
    }

    @GetMapping(value = "/tenyearrate/{types}")
    public Object getTenYearRateDesc(@PathVariable("types") List<String> types, Pageable pageable) {
        return rateService.getTenYearRateDesc(types, pageable);
    }
}
