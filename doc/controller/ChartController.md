# ChartController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing HTTP endpoints for chart data. Handles retrieval of price data for visualization.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/chart")` - Base URL path for all endpoints

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| priceService | PriceService | `@Resource` | Service for price operations |

## Endpoints

### `GET /api/chart/data/{id}`
Retrieves price data for chart visualization.
- **Path Variable**: `id` (String) - Fund ID
- **Query Parameters**:
  - `period` (String, default: "daily") - Data granularity (daily, weekly, monthly)
  - `startDate` (LocalDate, optional) - Start date
  - `endDate` (LocalDate, optional) - End date
- **Returns**: List of Price objects

## Default Values
- `endDate`: Current date (if not provided)
- `startDate`: One month before end date (if not provided)

## Usage
Called by frontend charting components to retrieve price history for visualization.

## Response Format
Returns a list of Price objects containing:
- `priceDate` - Date of the price
- `price` - Unit net value
- `accumulatedPrice` - Accumulated net value
- `returnOfTenKilo` - Return per 10,000 units (for currency funds)
- `sevenDayAnnualizedRateOfReturn` - 7-day annualized return (for currency funds)

## Design Pattern
- **REST Controller**: HTTP request handling
- **Default Parameters**: Sensible defaults for optional parameters
- **Date Range Handling**: Automatic date range calculation

## Note
The `period` parameter is accepted but currently only daily data is returned. Future enhancements may support weekly and monthly aggregation.
