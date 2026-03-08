# WebsiteDataService

## Package
`net.canglong.fund.service`

## Description
Service interface defining operations for retrieving fund data from external websites (CSRC - China Securities Regulatory Commission). Provides methods for web scraping and data extraction.

## Interface Definition
```java
public interface WebsiteDataService
```

## Methods

### Company Data Methods

#### `List<String> getCompanyIds()`
Retrieves a list of all fund company IDs from the external website.

#### `Company getCompany(String companyId)`
Retrieves detailed information for a specific company.

### Fund Data Methods

#### `List<Fund> getFunds(String companyId, String companyAbbr)`
Retrieves all funds managed by a specific company.
- `@NonNull` annotation on parameters

### Price Data Methods

#### `String getPriceWebPage(Fund fund, int page)`
Retrieves the HTML content of a price data page for a fund.

#### `boolean containsPrice(String html)`
Checks if the HTML content contains price data.

#### `List<Price> getPrices(String html, Fund fund, int page)`
Parses price data from HTML content.

## Data Source
The service interacts with the CSRC website (eid.csrc.gov.cn) to retrieve:
- Company listings
- Fund listings per company
- Historical price data per fund

## Usage
Implemented by `WebsiteDataServiceImpl`. Used by `PriceService` for data ingestion from external sources.

## Design Pattern
Follows the Service Layer pattern, abstracting external data source interactions behind an interface.

## Note
This service handles web scraping and may be affected by changes to the external website structure.
