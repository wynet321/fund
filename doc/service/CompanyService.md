# CompanyService

## Package
`net.canglong.fund.service`

## Description
Service interface defining operations for company management. Provides methods for CRUD operations and querying company information.

## Interface Definition
```java
public interface CompanyService
```

## Methods

### Retrieval Methods

#### `Page<Company> find(Pageable pageable)`
Retrieves a paginated list of all companies.

#### `Company find(String id)`
Finds a company by its ID.

### Persistence Methods

#### `Company create(Company company)`
Creates a new company.

#### `List<Company> create(List<Company> companies)`
Creates multiple companies in batch.

#### `Company update(Company company)`
Updates an existing company.

#### `List<Company> update(List<Company> companies)`
Updates multiple companies in batch.

#### `void delete(String id)`
Deletes a company by ID, including all associated funds and their data.

## Usage
Implemented by `CompanyServiceImpl`. Used by controllers and other services for company-related business logic.

## Important Note on Deletion
The `delete` operation is complex - it cascades to delete:
1. All funds belonging to the company
2. All price records for those funds
3. All monthly rate records for those funds
4. All yearly rate records for those funds

This is implemented in `CompanyServiceImpl` with batch processing to handle large datasets.

## Design Pattern
Follows the Service Layer pattern, defining a contract for company business operations.
