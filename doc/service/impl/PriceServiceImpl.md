# PriceServiceImpl

## Package
`net.canglong.fund.service.impl`

## Description
Implementation of `PriceService` interface. Provides concrete business logic for price data management, including complex calculations, background job management, and data ingestion from external sources.

## Annotations
- `@Log4j2` - Lombok logging
- `@Service` - Spring service bean

## Implements
- `PriceService`

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| priceRepo | PriceRepo | `@Resource` | Price data repository |
| websiteDataService | WebsiteDataService | `@Resource` | External data retrieval |
| fundService | FundService | `@Resource` | Fund operations |
| priceExecutor | ThreadPoolTaskExecutor | `@Resource(name = "priceExecutor")` | Price processing thread pool |
| fundExecutor | ThreadPoolTaskExecutor | `@Resource(name = "fundExecutor")` | Fund processing thread pool |
| companyService | CompanyService | `@Resource` | Company operations |
| threadCount | int | `@Value("${fund.threadCount}")` | Configurable thread count |

## Instance Variables
- `startTime` - Job start timestamp
- `fundCount` - Atomic counter for processed funds

## Key Method Implementations

### Data Retrieval
- `findByName()`, `findByFundId()`, `find()` - Basic retrieval with pagination
- `findLatestPriceBeforeDate()` - Latest price on/before date
- `findAllMonthAveragePriceByFundId()` - Monthly averages
- `findPriceAtCreationById()` - Initial price
- `findYearPriceById()` - Year-end prices
- `findMonthPriceById()` - Month-end prices

### Calculations
- `findPercentageByDate()` - Return percentage between dates
- `findYearPriceMapById()` - Year to price mapping
- `findMonthPriceMapById()` - Month to price mapping

### Data Ingestion
- `create(String fundId)` - Retrieves prices from external source
- `startPriceRetrievalJob()` - Starts background job for all funds
- `getPriceRetrievalJobStatus()` - Returns job status
- `stopPriceRetrievalJob()` - Stops the job

### Scheduled Tasks
- `reportStatusOfRetrievePriceForAll()` - Logs job status every 60 seconds

## Usage
Autowired by controllers for price operations and data ingestion.

## Design Pattern
- **Service Layer**: Business logic implementation
- **Thread Pool Management**: Custom executors for concurrent processing
- **Scheduled Tasks**: Automatic status reporting
- **Async Processing**: Background data retrieval

## Thread Safety
Uses `AtomicInteger` for thread-safe counting and separate thread pools for different operations.
