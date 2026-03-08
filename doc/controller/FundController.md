# FundController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing HTTP endpoints for fund-related operations. Handles fund queries, searches, and type retrieval.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/fund")` - Base URL path for all endpoints

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| fundService | FundService | `@Resource` | Service for fund operations |

## Endpoints

### `GET /api/fund/{id}`
Finds a fund by ID.
- **Path Variable**: `id` (String) - Fund identifier
- **Returns**: Fund object or null

### `GET /api/fund/types`
Retrieves all distinct fund types.
- **Returns**: List of type strings

### `GET /api/fund/search`
Searches funds by name or ID.
- **Query Parameters**:
  - `keyword` (String, required) - Search term
  - `limit` (int, default: 10) - Maximum results
- **Returns**: List of matching funds

## Usage
Called by frontend applications to retrieve fund information and perform searches.

## Response Format
Returns raw objects (not wrapped in ResponseObject) - direct entity or collection responses.

## Design Pattern
- **REST Controller**: HTTP request handling
- **Dependency Injection**: Service layer integration
- **Path/Query Parameters**: Flexible request handling
