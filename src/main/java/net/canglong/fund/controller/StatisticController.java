package net.canglong.fund.controller;

import net.canglong.fund.service.RateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/statistic", headers = "Accept=application/json")
public class StatisticController {

    @Resource
    private RateService rateService;

    @GetMapping(value="/month/{id}")
    public Object getMonthRateById(@PathVariable("id") String fundId) {
        return rateService.getMonthRateById(fundId);
    }
    
    @GetMapping(value="/year/{id}")
    public Object getYearRateById(@PathVariable("id") String fundId) {
        return rateService.getYearRateById(fundId);
    }

    @PostMapping(value="/year/{id}")
    public Object createYearRateById(@PathVariable("id") String fundId, @RequestParam("fromCreationDate") boolean fromCreationDate){
        return rateService.generate(fundId, fromCreationDate);
    }

}
