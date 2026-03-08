# RateServiceImpl

## Package
`net.canglong.fund.service.impl`

## Description
Implementation of `RateService` interface. Provides concrete business logic for return rate calculations and statistics generation, including monthly, yearly, and multi-period rates.

## Annotations
- `@Service` - Spring service bean
- `@Log4j2` - Lombok logging

## Implements
- `RateService`

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| periodRateRepo | PeriodRateRepo | `@Resource` | Period rate repository |
| monthRateRepo | MonthRateRepo | `@Resource` | Monthly rate repository |
| yearRateRepo | YearRateRepo | `@Resource` | Yearly rate repository |
| fundService | FundService | `@Resource` | Fund operations |
| priceService | PriceService | `@Resource` | Price operations |
| companyService | CompanyService | `@Resource` | Company operations |
| executor | ThreadPoolTaskExecutor | `@Resource(name = "statisticExecutor")` | Statistics thread pool |

## Instance Variables
- `startTime` - Job start timestamp

## Constructor
```java
public RateServiceImpl(PeriodRateRepo periodRateRepo)
```
Constructor injection for `PeriodRateRepo`.

## Key Method Implementations

### Retrieval Methods
- `getMonthRateById()` - Monthly rates for a fund/year
- `getYearRateById()` - All yearly rates for a fund
- `getYearRateByIdAndYear()` - Period rate for a fund
- `getYearRateByName()` - Search period rates by name
- `getPeriodRateDesc()` - Period rates by type
- `getYearRateByTypesAndYear()` - Yearly rates by types/year
- `getYearAverageRankByTypesAndPeriod()` - Ranked funds by average return

### Generation Methods

#### `generate(String fundId, boolean refreshAllData)`
Generates statistics for a single fund:
1. Checks if regeneration is needed (based on `statisticDueDate`)
2. Calls `generateYearRates()` for yearly rates
3. Calls `generatePeriodRate()` for multi-period rates
4. Calls `generateMonthPriceData()` for monthly rates (if needed)
5. Updates fund's statistic dates

#### `generate(List<String> types, boolean refreshAllData)`
Generates statistics for all funds of specified types using thread pool.

#### `generateYearRates(String fundId, LocalDate statisticDueDate)`
Calculates yearly return rates by comparing year-end prices.

#### `generateMonthPriceData(String fundId, LocalDate statisticDueDate)`
Calculates monthly return rates by comparing month-end prices.

#### `generatePeriodRate(String fundId)`
Calculates multi-period (1-10 years) return rates.

### Job Status
- `getStatisticJobStatus()` - Returns job status with thread pool metrics

## Usage
Autowired by controllers and `StatisticScheduler` for statistics operations.

## Design Pattern
- **Service Layer**: Business logic implementation
- **Thread Pool**: Concurrent statistics generation
- **Calculation Engine**: Complex financial calculations

## Calculation Logic
Rates are calculated as:
```
Rate = (CurrentAccumulatedPrice - PreviousAccumulatedPrice) / PreviousAccumulatedPrice
```
