# RateService

## Package
`net.canglong.fund.service`

## Description
Service interface defining operations for return rate calculations and statistics generation. Provides methods for generating and retrieving monthly, yearly, and multi-period return rates.

## Interface Definition
```java
public interface RateService
```

## Methods

### Retrieval Methods

#### `List<YearRate> getYearRateById(String fundId)`
Retrieves all annual return rates for a fund.

#### `PeriodRate getYearRateByIdAndYear(String fundId)`
Retrieves the multi-period rate record for a fund.

#### `List<PeriodRate> getYearRateByName(String fundName)`
Searches period rates by fund name.

#### `List<MonthRate> getMonthRateById(String fundId, int year)`
Retrieves monthly return rates for a fund in a specific year.

#### `Page<PeriodRate> getPeriodRateDesc(String type, Pageable pageable)`
Retrieves period rates for a fund type with pagination.

#### `Page<YearRate> getYearRateByTypesAndYear(List<String> types, int year, Pageable pageable)`
Retrieves annual rates for specific fund types in a year, ordered by rate.

#### `List<YearAverageRate> getYearAverageRankByTypesAndPeriod(List<String> types, int period, Pageable pageable)`
Retrieves funds ranked by average return over a period.

### Generation Methods

#### `Boolean generate(String fundId, boolean refreshAllData)`
Generates statistics for a single fund.

#### `Boolean generate(List<String> types, boolean refreshAllData)`
Generates statistics for all funds of specified types.

#### `Boolean generateStatisticData(List<String> types, boolean refreshAllData)`
Generates statistics for specified fund types (background job).

#### `PeriodRate generatePeriodRate(String fundId)`
Generates multi-period rates for a fund.

### Job Status Methods

#### `Status getStatisticJobStatus()`
Returns the status of the statistics generation job.

## Usage
Implemented by `RateServiceImpl`. Used by controllers and scheduled tasks for statistics operations.

## Statistics Generated
1. **Monthly Rates**: Calculated from month-end price differences
2. **Yearly Rates**: Calculated from year-end price differences
3. **Period Rates**: Multi-year (1-10 years) cumulative returns

## Design Pattern
Follows the Service Layer pattern, defining a contract for statistics business operations with support for background processing.
