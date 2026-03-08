# Fund Project Documentation

This directory contains documentation for all classes in the Fund Management project.

## Project Overview

The Fund project is a Spring Boot application for managing and analyzing investment fund data. It provides functionality for:

- Fund and company information management
- Historical price data retrieval and storage
- Return rate calculations (monthly, yearly, multi-period)
- Fund performance ranking and analysis
- Data visualization support

## Architecture

The project follows a layered architecture:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (JPA/Hibernate)
```

## Directory Structure

### [entity/](entity/)
JPA entity classes that map to database tables:
- `Fund.md` - Fund entity
- `Company.md` - Company entity
- `Price.md` - Price entity
- `PriceIdentity.md` - Composite key for Price
- `MonthRate.md` - Monthly return rate entity
- `MonthRateIdentity.md` - Composite key for MonthRate
- `YearRate.md` - Annual return rate entity
- `YearRateIdentity.md` - Composite key for YearRate
- `PeriodRate.md` - Multi-period return rate entity
- `Status.md` - Job status tracking
- `ResponseObject.md` - API response wrapper
- `MonthAveragePrice.md` - Interface projection for queries

### [vo/](vo/)
Value Objects for data transfer:
- `FundPrice.md` - Price data VO
- `DatePriceIdentity.md` - Dated price VO
- `FundPercentage.md` - Return percentage VO
- `MonthPrice.md` - Monthly price aggregation VO
- `YearPrice.md` - Yearly price aggregation VO
- `YearAverageRate.md` - Average rate statistics VO

### [repository/](repository/)
Spring Data JPA repositories:
- `FundRepo.md` - Fund data access
- `CompanyRepo.md` - Company data access
- `PriceRepo.md` - Price data access
- `MonthRateRepo.md` - Monthly rate data access
- `YearRateRepo.md` - Yearly rate data access
- `PeriodRateRepo.md` - Period rate data access

### [service/](service/)
Service interfaces:
- `FundService.md` - Fund operations
- `CompanyService.md` - Company operations
- `PriceService.md` - Price operations
- `RateService.md` - Rate calculation operations
- `WebsiteDataService.md` - External data retrieval

### [service/impl/](service/impl/)
Service implementations:
- `FundServiceImpl.md` - Fund service implementation
- `CompanyServiceImpl.md` - Company service implementation
- `PriceServiceImpl.md` - Price service implementation
- `RateServiceImpl.md` - Rate service implementation
- `WebsiteDataServiceImpl.md` - Web scraping implementation

### [controller/](controller/)
REST controllers:
- `FundController.md` - Fund API endpoints
- `CompanyController.md` - Company API endpoints
- `PriceController.md` - Price API endpoints
- `RateController.md` - Rate API endpoints
- `ChartController.md` - Chart data endpoints
- `HealthController.md` - Health check endpoint

### [schedule/](schedule/)
Scheduled tasks:
- `StatisticScheduler.md` - Automated statistics generation

### [config/](config/)
Configuration classes:
- `ThreadPoolConfig.md` - Thread pool configuration
- `BusinessException.md` - Custom exception
- `GlobalExceptionHandler.md` - Exception handling

### [exception/](exception/)
Alternative exception handling:
- `GlobalExceptionHandler.md` - ResponseObject-based exception handler

### Root Level
- `FundApplication.md` - Main application class
- `CrossOriginConfig.md` - CORS configuration
- `ResponseHandler.md` - AOP response wrapper (disabled)

## Key Technologies

- **Spring Boot** - Application framework
- **Spring Data JPA** - Data access layer
- **Spring Scheduling** - Background task execution
- **Spring Async** - Asynchronous processing
- **H2/Database** - Data storage (configurable)
- **Unirest** - HTTP client for web scraping
- **Jsoup** - HTML parsing
- **Lombok** - Boilerplate code reduction
- **Log4j2** - Logging framework

## Data Flow

1. **Data Ingestion**: `WebsiteDataService` scrapes fund data from CSRC website
2. **Storage**: Entities are persisted via JPA repositories
3. **Processing**: Services calculate return rates and statistics
4. **API**: Controllers expose data via REST endpoints
5. **Scheduling**: Automated tasks run periodically for updates

## External Data Source

The application retrieves fund data from the China Securities Regulatory Commission (CSRC) website:
- Base URL: `http://eid.csrc.gov.cn/`
- Company listings
- Fund listings per company
- Historical price data

## Thread Pools

Three thread pools are configured for concurrent processing:
- **priceExecutor**: For price data processing
- **fundExecutor**: For fund data processing
- **statisticExecutor**: For statistics generation

Default thread count: 20 (configurable via `fund.threadCount`)

## API Base Path

All API endpoints are prefixed with `/api/`:
- `/api/fund` - Fund operations
- `/api/company` - Company operations
- `/api/price` - Price operations
- `/api/rate` - Rate operations
- `/api/chart` - Chart data
- `/api/health` - Health check
