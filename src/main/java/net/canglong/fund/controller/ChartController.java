package net.canglong.fund.controller;

import java.time.LocalDate;
import jakarta.annotation.Resource;
import net.canglong.fund.service.PriceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/chart")
public class ChartController {

  @Resource
  private PriceService priceService;

  @GetMapping(value = "/data/{id}")
  public Object getChartData(
      @PathVariable("id") String fundId,
      @RequestParam(value = "period", defaultValue = "daily") String period,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    
    // Set default dates if not provided
    if (endDate == null) {
      endDate = LocalDate.now();
    }
    if (startDate == null) {
      startDate = endDate.minusMonths(1);
    }
    
    // Get price data for the date range
    return priceService.findByFundIdAndDateRange(fundId, startDate, endDate);
  }
}
