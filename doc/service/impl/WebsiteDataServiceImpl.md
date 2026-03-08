# WebsiteDataServiceImpl

## Package
`net.canglong.fund.service.impl`

## Description
Implementation of `WebsiteDataService` interface. Provides concrete logic for web scraping and data extraction from the CSRC (China Securities Regulatory Commission) website.

## Annotations
- `@Log4j2` - Lombok logging
- `@Service` - Spring service bean

## Implements
- `WebsiteDataService`

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| fundService | FundService | `@Resource` | Fund operations for creating parent-child relationships |

## HTTP Client
Uses **Unirest** for HTTP requests:
- Base URL: `http://eid.csrc.gov.cn/`
- Concurrency: 200 connections, 50 per route

## Constructor
Initializes Unirest configuration with default base URL and concurrency settings.

## Method Implementations

### `List<String> getCompanyIds()`
Retrieves all fund company IDs from CSRC API.
- Endpoint: `/fund/disclose/fund_compay_affiche.do`
- Returns: List of company codes

### `Company getCompany(String companyId)`
Retrieves company details from CSRC API.
- Endpoint: `/fund/disclose/fund_compay_detail.do`
- Extracts: name, abbreviation, founding date, address
- Date parsing with fallback to "1980-01-01"

### `List<Fund> getFunds(String companyId, String companyAbbr)`
Retrieves all funds for a company from CSRC API.
- Paginated retrieval (20 items per page)
- Constructs complex query parameters
- Returns: List of Fund objects

### `String getPriceWebPage(Fund fund, int page)`
Retrieves price data HTML page.
- Endpoint: `/fund/web/list_net.daily_report`
- Parameters: fund code, pagination (20 items per page)
- Retry logic: 10-minute sleep on failure, then retry once

### `boolean containsPrice(String html)`
Checks if HTML contains price data by looking for `class="dd"`.

### `List<Price> getPrices(String html, Fund fund, int page)`
Parses price data from HTML using Jsoup.

#### Currency Funds (货币型):
- Parses: returnOfTenKilo, sevenDayAnnualizedRateOfReturn
- Handles parent-child fund relationships

#### Non-Currency Funds:
- Parses: price, accumulatedPrice
- Dynamic column detection from header
- Handles parent-child fund relationships

### `int[] getFieldIndex(Elements elements)`
Maps column headers to indices for flexible parsing.

### `void addParentFund(Elements elementTds)`
Creates or updates parent-child fund relationships when parsing structured funds.

## Usage
Autowired by `PriceService` for external data ingestion.

## Design Pattern
- **Service Layer**: External data access abstraction
- **Web Scraping**: HTML parsing with Jsoup
- **HTTP Client**: REST API consumption with Unirest
- **Retry Pattern**: Automatic retry with delay on failures

## Error Handling
- Connection failures trigger 10-minute sleep and retry
- Missing parent funds are logged as errors
- Null checks for all parsed values

## Note
This service is dependent on the external website structure. Changes to the CSRC website may require updates to the parsing logic.
