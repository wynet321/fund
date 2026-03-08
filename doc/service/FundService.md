# FundService

## Package
`net.canglong.fund.service`

## Description
Service interface defining operations for fund management. Provides methods for CRUD operations, searching, and querying fund information.

## Interface Definition
```java
public interface FundService
```

## Methods

### Retrieval Methods

#### `Page<Fund> get(Pageable pageable)`
Retrieves a paginated list of all funds.

#### `Optional<Fund> findById(String id)`
Finds a fund by its ID.

#### `Page<Fund> findByCompanyId(String companyId, Pageable pageable)`
Finds all funds belonging to a specific company with pagination.

#### `Fund findByName(String name)`
Finds a fund by its exact name.

#### `List<Fund> findAll()`
Retrieves all funds.

### Search Methods

#### `List<Fund> findAllByType(String type)`
Finds all funds of a specific type.

#### `List<Fund> findAllExcludesType(String type)`
Finds all funds except those of the specified type.

#### `List<Fund> findAllByTypes(List<String> types)`
Finds all funds with types in the provided list.

#### `List<String> findAllTypes()`
Retrieves all distinct fund types.

#### `List<Fund> searchByNameContaining(String keyword, int limit)`
Searches funds by name containing keyword, with result limit.

#### `List<Fund> searchByNameOrIdContaining(String keyword, int limit)`
Searches funds by name or ID containing keyword, with result limit.

### Persistence Methods

#### `Fund create(Fund fund)`
Creates a new fund.
- Annotated with `@Transactional`

#### `Fund update(Fund fund)`
Updates an existing fund.
- Annotated with `@Transactional`

#### `List<Fund> create(List<Fund> funds)`
Creates multiple funds in batch.
- Annotated with `@Transactional`

## Usage
Implemented by `FundServiceImpl`. Used by controllers and other services for fund-related business logic.

## Design Pattern
Follows the Service Layer pattern, defining a contract for fund business operations.
