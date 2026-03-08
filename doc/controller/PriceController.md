# PriceController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing HTTP endpoints for price-related operations. Handles price queries, data ingestion, and job management.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/price")` - Base URL path for all endpoints

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| priceService | PriceService | `@Resource` | Service for price operations |

## Endpoints

### Price Retrieval

#### `GET /api/price/name/{name}`
Finds prices by fund name.
- **Path Variable**: `name` (String)
- **Query Parameters**: Pageable
- **Returns**: Page of Price objects

#### `GET /api/price/{id}`
Finds prices by fund ID.
- **Path Variable**: `id` (String)
- **Query Parameters**: Pageable
- **Returns**: Page of Price objects

#### `GET /api/price/fund/month_avg/{id}/start/{date}`
Gets monthly average prices.
- **Path Variables**: `id` (String), `date` (LocalDate)
- **Returns**: List of MonthAveragePrice projections

#### `GET /api/price/fund/{id}/{date}`
Finds latest price on or before a date.
- **Path Variables**: `id` (String), `date` (LocalDate)
- **Returns**: Price object

#### `GET /api/price/fund/{id}/start/{date}`
Finds prices from a start date.
- **Path Variables**: `id` (String), `date` (LocalDate)
- **Query Parameters**: Pageable
- **Returns**: Page of Price objects

#### `GET /api/price/fund/{id}/start/{startDate}/end/{endDate}`
Calculates return percentage between dates.
- **Path Variables**: `id`, `startDate`, `endDate` (LocalDate)
- **Returns**: FundPercentage object

#### `GET /api/price/fund/{id}/startDate`
Finds price at fund creation.
- **Path Variable**: `id` (String)
- **Returns**: Price object

#### `GET /api/price/fund/{id}/priceAtYearStart`
Finds year-end prices for a fund.
- **Path Variable**: `id` (String)
- **Returns**: YearPrice object

### Data Ingestion

#### `POST /api/price/fund/{id}`
Retrieves and saves prices for a fund from external source.
- **Path Variable**: `id` (String)
- **Returns**: Integer (count of records created)

#### `POST /api/price/ingestion/{threadCount}`
Starts background price retrieval job.
- **Path Variable**: `threadCount` (Integer, optional)
- **Returns**: Boolean (success status)

#### `GET /api/price/ingestion`
Gets status of price retrieval job.
- **Returns**: Status object

#### `DELETE /api/price/ingestion`
Stops price retrieval job.
- **Returns**: Boolean (success status)

## Usage
Called by frontend applications for price data and admin tools for data ingestion.

## Design Pattern
- **REST Controller**: HTTP request handling
- **CRUD Operations**: Create, Read endpoints
- **Job Management**: Background task control
- **Date Formatting**: `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)` for date parameters
