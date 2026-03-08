# PriceService

## Package
`net.canglong.fund.service`

## Description
Service interface defining operations for fund price data management. Provides methods for retrieving, calculating, and ingesting price data.

## Interface Definition
```java
public interface PriceService
```

## Methods

### Retrieval Methods

#### `Page<Price> findByName(String name, Pageable pageable)`
Finds price records by fund name with pagination.

#### `Page<Price> findByFundId(String id, Pageable pageable)`
Finds price records by fund ID with pagination.

#### `Price findLatestPriceBeforeDate(String id, LocalDate date)`
Finds the most recent price record on or before a specific date.

#### `Page<Price> find(String id, LocalDate startDate, Pageable pageable)`
Finds price records for a fund from a start date with pagination.

#### `List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate)`
Calculates monthly average prices for a fund from a start date.

#### `Price findPriceAtCreationById(String id)`
Finds the price record at fund creation.

#### `YearPrice findYearPriceById(String id)`
Retrieves year-end price data for a fund.

#### `Map<Integer, FundPrice> findYearPriceMapById(String id)`
Returns a map of year to price data for a fund.

#### `Map<Integer, FundPrice> findMonthPriceMapById(String id, int year)`
Returns a map of month to price data for a fund in a specific year.

#### `MonthPrice findMonthPriceById(String id, int year)`
Retrieves monthly price data for a fund in a specific year.

#### `Price findEarliestPriceAfterDate(String id, LocalDate targetDate)`
Finds the earliest price record on or after a specific date.

#### `LocalDate findLatestPriceDateById(String id)`
Returns the date of the most recent price record.

#### `LocalDate findEarliestPriceDateById(String id)`
Returns the date of the earliest price record.

#### `List<Price> findByFundIdAndDateRange(String fundId, LocalDate startDate, LocalDate endDate)`
Finds all price records within a date range.

### Calculation Methods

#### `FundPercentage findPercentageByDate(String id, LocalDate startDate, LocalDate endDate)`
Calculates the return percentage between two dates.

### Data Ingestion Methods

#### `Integer create(String fundId)`
Retrieves and saves price data for a fund from external sources.
- Annotated with `@Transactional`

#### `Boolean startPriceRetrievalJob(int threadCount)`
Starts a background job to retrieve prices for all funds.

#### `Status getPriceRetrievalJobStatus()`
Returns the status of the price retrieval job.

#### `boolean stopPriceRetrievalJob()`
Stops the price retrieval job.

## Usage
Implemented by `PriceServiceImpl`. Used by controllers and other services for price-related operations.

## Design Pattern
Follows the Service Layer pattern, defining a contract for price business operations including complex calculations and background job management.
