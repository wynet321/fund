# PriceRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `Price` entity. Provides CRUD operations and complex queries for fund price data access.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<Price, PriceIdentity>` - Provides standard JPA CRUD operations with composite key

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(Price entity)` - Save a price record
- `saveAll(Iterable<Price> entities)` - Save multiple price records
- `findById(PriceIdentity id)` - Find price by composite ID
- `findAll()` - Find all price records
- `deleteById(PriceIdentity id)` - Delete price by ID

### Custom Query Methods

#### `findPriceSet(String fundId, LocalDate startDate, Pageable pageable)`
Finds price records for a fund from a start date with pagination (native query).

#### `findLatestPriceBeforeDate(String fundId, Pageable pageable)`
Finds the latest price records before a date with pagination (native query).

#### `findAllMonthAveragePriceByFundId(String fundId, LocalDate startDate)`
Calculates monthly average prices for a fund from a start date (native query, returns `MonthAveragePrice` projection).

#### `findLatestPriceBeforeDate(String fundId, LocalDate targetDate)`
Finds the most recent price record for a fund on or before a specific date.

#### `findLatestPriceDateById(String fundId)`
Returns the date of the most recent price record for a fund.

#### `findEarliestPriceDateById(String fundId)`
Returns the date of the earliest price record for a fund.

#### `findEarliestPriceAfterDate(String fundId, LocalDate targetDate)`
Finds the earliest price record for a fund on or after a specific date.

#### `findPriceAtCreationById(String fundId)`
Finds the price record at fund creation (earliest record).

#### `findByFundIdAndDateRange(String fundId, LocalDate startDate, LocalDate endDate)`
Finds all price records for a fund within a date range.

#### `findByFundId(String fundId, Pageable pageable)`
Finds price records for a fund with pagination.

#### `deleteByFundId(String fundId)`
Deletes all price records for a specific fund (modifying query).

## Usage
Used by `PriceService` for all price-related database operations, including historical data retrieval, chart data generation, and statistics calculations.
