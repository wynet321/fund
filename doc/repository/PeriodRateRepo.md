# PeriodRateRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `PeriodRate` entity. Provides CRUD operations and queries for multi-period return rate data.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<PeriodRate, String>` - Provides standard JPA CRUD operations with String (fund ID) as the ID type

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(PeriodRate entity)` - Save a period rate record
- `saveAndFlush(PeriodRate entity)` - Save and flush immediately
- `findById(String id)` - Find period rate by fund ID
- `findAll()` - Find all period rate records
- `deleteById(String id)` - Delete by fund ID

### Custom Query Methods

#### `findByType(String type, Pageable pageable)`
Finds period rate records for a specific fund type with pagination.

#### `findByNameContaining(String fundName)`
Finds period rate records where the fund name contains the given string.

## Usage
Used by `RateService` for multi-period return rate operations, including:
- Retrieving period rates for display
- Fund ranking by long-term performance
- Performance comparison across different time horizons

## Notes
- Uses fund ID as the primary key (one record per fund)
- Supports pagination for large result sets
- Contains rates for 1-10 year periods
