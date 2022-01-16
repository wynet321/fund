package net.soryu.fund.controller;

import net.soryu.fund.service.RateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/statistic", headers = "Accept=application/json")
public class StatisticController {

    @Resource
    private RateService rateService;

    @GetMapping(value="/month/{fundId}")
    public Object getMonthRateByFundId(@PathVariable("id") String fundId) throws Exception {
        return rateService.getMonthRateByFundId(fundId);
    }
    
    @GetMapping(value="/year/{fundId}")
    public Object getYearRateByFundId(String fundId) throws Exception {
        return rateService.getYearRateByFundId(fundId);
    }

}
