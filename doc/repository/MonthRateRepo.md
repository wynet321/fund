# MonthRateRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `MonthRate` entity. Provides CRUD operations and queries for monthly return rate data.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<MonthRate, MonthRateIdentity>` - Provides standard JPA CRUD operations with composite key

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(MonthRate entity)` - Save a monthly rate record
- `saveAll(Iterable<MonthRate> entities)` - Save multiple records
- `findById(MonthRateIdentity id)` - Find rate by composite ID
- `findAll()` - Find all monthly rate records
- `deleteById(MonthRateIdentity id)` - Delete rate by ID

### Custom Query Methods

#### `findAllById(String fundId, int year)`
Finds all monthly rate records for a fund in a specific year, ordered by month ascending.

#### `deleteByFundId(String fundId)`
Deletes all monthly rate records for a specific fund.
- Annotated with `@Modifying` and `@Transactional`

## Usage
Used by `RateService` for monthly return rate operations, including:
- Retrieving monthly rates for display
- Generating monthly statistics
- Cleaning up data when a fund is deleted

## Notes
- Uses composite key `MonthRateIdentity` (fundId + year + month)
- Supports bulk deletion for data cleanup
