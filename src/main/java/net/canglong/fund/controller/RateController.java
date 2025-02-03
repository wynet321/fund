package net.canglong.fund.controller;

import java.util.List;
import javax.annotation.Resource;
import net.canglong.fund.service.RateService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rate", headers = "Accept=application/json")
public class RateController {

  @Resource
  private RateService rateService;

  @GetMapping(value = "/month/{id}/{year}")
  public Object getMonthRateById(@PathVariable("id") String fundId,
      @PathVariable("year") int year) {
    return rateService.getMonthRateById(fundId, year);
  }

  @GetMapping(value = "/year/{id}")
  public Object getYearRateById(@PathVariable("id") String fundId) {
    return rateService.getYearRateById(fundId);
  }

  @GetMapping(value = "/year/order/{types}/{year}")
  public Object getYearRateByTypesAndYear(@PathVariable("types") List<String> types,
      @PathVariable("year") int year, Pageable pageable) {
    return rateService.getYearRateByTypesAndYear(types, year, pageable);
  }

  @GetMapping(value = "/year/rank/{types}/{period}")
  public Object getYearAverageRankByTypesAndPeriod(@PathVariable("types") List<String> types,
      @PathVariable("period") int period, Pageable pageable) {
    return rateService.getYearAverageRankByTypesAndPeriod(types, period, pageable);
  }

  @PostMapping(value = "/year/{id}")
  public Object generateYearRate(@PathVariable("id") String fundId,
      @RequestParam("fromCreationDate") boolean fromCreationDate) throws Exception {
    return rateService.generate(fundId, fromCreationDate);
  }

  @PostMapping(value = "/generate")
  public Object generateStatisticData(@RequestBody List<String> types) {
    return rateService.generateStatisticData(types, true);
  }

  @PostMapping(value = "/generate/periodrate/{id}")
  public Object generatePeriodRate(@PathVariable("id") String fundId) throws Exception {
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
