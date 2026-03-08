# FundRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `Fund` entity. Provides CRUD operations and custom queries for fund data access.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<Fund, String>` - Provides standard JPA CRUD operations with String as the ID type

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(Fund entity)` - Save a fund
- `findById(String id)` - Find fund by ID
- `findAll()` - Find all funds
- `deleteById(String id)` - Delete fund by ID
- `existsById(String id)` - Check if fund exists

### Custom Query Methods

#### `findAllByCompanyId(String companyId, Pageable pageable)`
Finds all funds belonging to a specific company with pagination.

#### `findByName(String name)`
Finds a fund by its exact name.

#### `findAllByType(String type)`
Finds all funds of a specific type.

#### `findAllExcludesType(String type)`
Finds all funds except those of the specified type.

#### `findAllByTypes(List<String> types)`
Finds all funds with types in the provided list.

#### `findAllTypes()`
Returns a list of all distinct fund types in the database (native query).

#### `findByNameContaining(String keyword)`
Finds funds whose names contain the keyword (case-insensitive).

#### `findByNameOrIdContaining(String keyword)`
Finds funds whose names OR IDs contain the keyword.

## Usage
Used by `FundService` and its implementation for all fund-related database operations.
