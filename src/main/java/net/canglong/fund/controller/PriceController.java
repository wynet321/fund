package net.canglong.fund.controller;

import java.time.LocalDate;
import javax.annotation.Resource;
import net.canglong.fund.service.PriceService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/price", headers = "Accept=application/json")
public class PriceController {

  @Resource
  private PriceService priceService;

  @GetMapping(value = "/name/{name}")
  public Object getByName(@PathVariable("name") String name, Pageable pageable) {
    return priceService.findByName(name, pageable);
  }

  @GetMapping(value = "/{id}")
  public Object get(@PathVariable("id") String id, Pageable pageable) {
    return priceService.findByFundId(id, pageable);
  }

  @GetMapping(value = "/fund/month_avg/{id}/start/{date}")
  public Object getMonthAveragePrice(@PathVariable("id") String id,
      @PathVariable("date") @DateTimeFormat(
          iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
    return priceService.findAllMonthAveragePriceByFundId(id, startDate);
  }

  @PostMapping(value = "/fund/{id}")
  public Object create(@PathVariable("id") String id) throws Exception {
    return priceService.create(id);
  }

  @GetMapping(value = "/fund/{id}/{date}")
  public Object getByFundIdAndDate(@PathVariable("id") String id,
      @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return priceService.findLatestPriceBeforeDate(id, date);
  }

  @GetMapping(value = "/fund/{id}/start/{date}")
  public Object getPriceFromDate(@PathVariable("id") String id,
      @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      Pageable pageable) {
    return priceService.find(id, startDate, pageable);
  }

  @GetMapping(value = "/fund/{id}/start/{startDate}/end/{endDate}")
  public Object find(@PathVariable("id") String id,
      @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
      throws Exception {
    return priceService.findPercentageByDate(id, startDate, endDate);
  }

  @GetMapping(value = "/fund/{id}/startDate")
  public Object findStartDate(@PathVariable("id") String id) {
    return priceService.findPriceAtCreationById(id);
  }

  @GetMapping(value = "/fund/{id}/priceAtYearStart")
  public Object findYearPriceById(@PathVariable("id") String id) {
    return priceService.findYearPriceById(id);
  }

  @PostMapping(value = "/ingestion/{threadCount}")
  public Object startIngestionJob(
      @PathVariable(name = "threadCount", required = false) Integer threadCount) {
    return priceService.startPriceRetrievalJob();
  }

  @GetMapping(value = "/ingestion")
  public Object getIngestionJobStatus() {
    return priceService.getPriceRetrievalJobStatus();
  }

  @DeleteMapping(value = "/ingestion")
  public Object stopIngestionJob() {
    return priceService.stopPriceRetrievalJob();
  }

}
