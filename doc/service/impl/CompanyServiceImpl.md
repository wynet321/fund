# CompanyServiceImpl

## Package
`net.canglong.fund.service.impl`

## Description
Implementation of `CompanyService` interface. Provides concrete business logic for company management operations, including complex cascading deletion.

## Annotations
- `@Service` - Spring annotation marking this as a service bean

## Implements
- `CompanyService`

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| companyRepo | CompanyRepo | `@Resource` | Repository for company data |
| fundRepo | FundRepo | `@Resource` | Repository for fund data |
| priceRepo | PriceRepo | `@Resource` | Repository for price data |
| monthRateRepo | MonthRateRepo | `@Resource` | Repository for monthly rates |
| yearRateRepo | YearRateRepo | `@Resource` | Repository for yearly rates |

## Method Implementations

### `Page<Company> find(Pageable pageable)`
Delegates to `companyRepo.findAll(pageable)`.

### `Company create(Company company)`
Delegates to `companyRepo.save(company)`.

### `List<Company> create(List<Company> companies)`
Delegates to `companyRepo.saveAll(companies)`.

### `Company update(Company company)`
Delegates to `companyRepo.save(company)`.

### `List<Company> update(List<Company> companies)`
Delegates to `companyRepo.saveAll(companies)`.

### `Company find(String id)`
Retrieves company by ID, returns null if not found.

### `void delete(String id)`
**Complex cascading deletion** with batch processing:

1. **Find all funds** belonging to the company (paginated, 100 at a time)
2. **For each fund**:
   - Delete prices in batches (paginated deletion)
   - Delete monthly rates
   - Delete yearly rates
   - Delete the fund
3. **Delete the company**

Uses `PageRequest` for batch processing to handle large datasets without memory issues.

## Usage
Autowired by controllers and other services requiring company operations.

## Design Pattern
- **Service Layer**: Implements business logic
- **Repository Pattern**: Delegates data access to repositories
- **Batch Processing**: Uses pagination for large dataset operations
- **Transaction Management**: Cascading deletion is `@Transactional`

## Important Notes
- The delete operation is resource-intensive for companies with many funds
- Uses batch processing to prevent memory overflow
- All related data (prices, rates) must be deleted before the company
