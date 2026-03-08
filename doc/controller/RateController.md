# RateController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing HTTP endpoints for return rate operations. Handles rate queries, statistics generation, and fund ranking.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/rate")` - Base URL path for all endpoints

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| rateService | RateService | `@Resource` | Service for rate operations |

## Endpoints

### Rate Retrieval

#### `GET /api/rate/month/{id}/{year}`
Gets monthly rates for a fund in a year.
- **Path Variables**: `id` (String), `year` (int)
- **Returns**: List of MonthRate objects

#### `GET /api/rate/year/{id}`
Gets all yearly rates for a fund.
- **Path Variable**: `id` (String)
- **Returns**: List of YearRate objects

#### `GET /api/rate/period/{id}`
Gets period rates for a fund.
- **Path Variable**: `id` (String)
- **Returns**: PeriodRate object

#### `GET /api/rate/year/name/{name}`
Searches period rates by fund name.
- **Path Variable**: `name` (String)
- **Returns**: List of PeriodRate objects

#### `GET /api/rate/year/order/{types}/{year}`
Gets yearly rates for types in a year (ordered by rate).
- **Path Variables**: `types` (List<String>), `year` (int)
- **Query Parameters**: Pageable
- **Returns**: Page of YearRate objects

#### `GET /api/rate/year/rank/{types}/{period}`
Gets funds ranked by average return over a period.
- **Path Variables**: `types` (List<String>), `period` (int)
- **Query Parameters**: Pageable
- **Returns**: List of YearAverageRate objects

#### `GET /api/rate/period_rate/{type}`
Gets period rates by type with sorting.
- **Path Variable**: `type` (String)
- **Query Parameters**:
  - `page` (int, default: 0)
  - `size` (int, default: 1)
  - `sort` (String[], default: "one_year_rate,desc")
- **Returns**: Page of PeriodRate objects
- **Sorting**: Converts snake_case to camelCase for JPA compatibility

### Statistics Generation

#### `POST /api/rate/year/{id}`
Generates statistics for a fund.
- **Path Variable**: `id` (String)
- **Query Parameter**: `fromCreationDate` (boolean)
- **Returns**: Boolean (success status)

#### `POST /api/rate/generate`
Generates statistics for fund types (background job).
- **Request Body**: `types` (List<String>)
- **Returns**: Boolean (success status)

#### `POST /api/rate/generate/period_rate/{id}`
Generates period rates for a fund.
- **Path Variable**: `id` (String)
- **Returns**: PeriodRate object

## Private Methods

### `String convertToCamelCase(String snakeCase)`
Converts snake_case field names to camelCase for Spring Data JPA sorting.

## Usage
Called by frontend applications for rate data and admin tools for statistics generation.

## Design Pattern
- **REST Controller**: HTTP request handling
- **Path Variables**: Complex URL patterns with multiple parameters
- **Sorting Conversion**: Automatic case conversion for JPA compatibility
- **Validation**: Allowed sort fields whitelist
