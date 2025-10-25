package net.canglong.fund.controller;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import jakarta.annotation.Resource;
import net.canglong.fund.service.RateService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rate")
public class RateController {

  @Resource
  private RateService rateService;

  @GetMapping(value = "/month/{id}/{year}")
  public Object getMonthRateById(@PathVariable("id") String fundId,
      @PathVariable int year) {
    return rateService.getMonthRateById(fundId, year);
  }

  @GetMapping(value = "/year/{id}")
  public Object getYearRateById(@PathVariable("id") String fundId) {
    return rateService.getYearRateById(fundId);
  }

  @GetMapping(value = "/period/{id}")
  public Object getYearRateByIdAndYear(@PathVariable("id") String fundId) {
    return rateService.getYearRateByIdAndYear(fundId);
  }

  @GetMapping(value = "/year/name/{name}")
  public Object getYearRateByName(@PathVariable("name") String fundName) {
    return rateService.getYearRateByName(fundName);
  }

  @GetMapping(value = "/year/order/{types}/{year}")
  public Object getYearRateByTypesAndYear(@PathVariable List<String> types,
      @PathVariable int year, Pageable pageable) {
    return rateService.getYearRateByTypesAndYear(types, year, pageable);
  }

  @GetMapping(value = "/year/rank/{types}/{period}")
  public Object getYearAverageRankByTypesAndPeriod(@PathVariable List<String> types,
      @PathVariable int period, Pageable pageable) {
    return rateService.getYearAverageRankByTypesAndPeriod(types, period, pageable);
  }

  @PostMapping(value = "/year/{id}")
  public Object generateYearRate(@PathVariable("id") String fundId,
      @RequestParam boolean fromCreationDate) throws Exception {
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

  @GetMapping(value = "/periodrate/{type}")
  public Object getPeriodRateDesc(@PathVariable String type,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "1") int size,
      @RequestParam(defaultValue = "one_year_rate,desc") String[] sort) {
    // Parse the sort parameter correctly
    String sortField = sort[0];
    String sortDirection = sort.length > 1 ? sort[1] : "desc";
    // Validate sort field against allowed list
    Set<String> allowedSortFields = new HashSet<>(List.of(
        "one_year_rate", "two_year_rate", "three_year_rate", "four_year_rate", "five_year_rate",
        "six_year_rate", "seven_year_rate", "eight_year_rate", "nine_year_rate", "ten_year_rate",
        "name", "company_name", "type"
    ));
    if (!allowedSortFields.contains(sortField)) {
      sortField = "one_year_rate"; // default safe field
    }
    // Convert snake_case field names to camelCase for Spring Data JPA sorting
    String camelCaseField = convertToCamelCase(sortField);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), camelCaseField));
    return rateService.getPeriodRateDesc(type, pageable);
  }
  
  private String convertToCamelCase(String snakeCase) {
    String[] parts = snakeCase.split("_");
    StringBuilder camelCase = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      camelCase.append(parts[i].substring(0, 1).toUpperCase())
               .append(parts[i].substring(1));
    }
    return camelCase.toString();
  }

}
