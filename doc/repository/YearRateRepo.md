# YearRateRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `YearRate` entity. Provides CRUD operations and complex queries for annual return rate data, including ranking and aggregation.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<YearRate, YearRateIdentity>` - Provides standard JPA CRUD operations with composite key

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(YearRate entity)` - Save an annual rate record
- `saveAllAndFlush(Iterable<YearRate> entities)` - Save multiple records and flush
- `findById(YearRateIdentity id)` - Find rate by composite ID
- `findAll()` - Find all annual rate records

### Custom Query Methods

#### `findAllById(String fundId)`
Finds all annual rate records for a specific fund.

#### `findByIdAndYear(String fundId, int year)`
Finds the annual rate record for a fund in a specific year.

#### `findByNameContaining(String fundName)`
Finds annual rate records where the fund name contains the given string.

#### `findByNameAndYear(String fundName, int year)`
Finds the annual rate record for a fund by name and year.

#### `findAllByTypesAndYear(List<String> types, int year, Pageable pageable)`
Finds annual rate records for specific fund types in a given year, ordered by rate descending.

#### `findAverageRankByTypesAndYear(List<String> types, int year, Pageable pageable)`
Calculates average return and standard deviation for funds by type over a period (from year to current year).
Returns `Object[]` with: fundId, name, companyName, type, average, stddev.

#### `deleteByFundId(String fundId)`
Deletes all annual rate records for a specific fund.
- Annotated with `@Modifying` and `@Transactional`

## Usage
Used by `RateService` for annual return rate operations, including:
- Retrieving yearly rates for display
- Fund ranking by performance
- Statistical analysis (average, standard deviation)
- Data cleanup when funds are deleted

## Notes
- Uses composite key `YearRateIdentity` (fundId + year)
- Supports complex aggregation queries for ranking
- Returns projection data as `Object[]` for native queries
