# CompanyRepo

## Package
`net.canglong.fund.repository`

## Description
Spring Data JPA repository interface for `Company` entity. Provides CRUD operations for company data access.

## Annotations
- `@Repository` - Spring annotation marking this as a repository bean

## Extends
- `JpaRepository<Company, String>` - Provides standard JPA CRUD operations with String as the ID type

## Methods

### Standard JPA Methods
Inherited from `JpaRepository`:
- `save(Company entity)` - Save a company
- `saveAll(Iterable<Company> entities)` - Save multiple companies
- `findById(String id)` - Find company by ID
- `findAll()` - Find all companies
- `findAll(Pageable pageable)` - Find all companies with pagination
- `deleteById(String id)` - Delete company by ID
- `existsById(String id)` - Check if company exists

## Usage
Used by `CompanyService` and its implementation for all company-related database operations.

## Note
This is a basic repository interface that relies on the standard JPA methods provided by the parent interface. No custom query methods are defined.
