# CompanyController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing HTTP endpoints for company-related operations. Handles company queries and data import from external sources.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/company")` - Base URL path for all endpoints

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| companyService | CompanyService | `@Resource` | Service for company operations |
| dataService | WebsiteDataService | `@Resource` | Service for external data retrieval |

## Endpoints

### `GET /api/company`
Retrieves all companies with pagination.
- **Query Parameters**: Pageable (page, size, sort)
- **Returns**: Page of Company objects

### `GET /api/company/{id}`
Finds a company by ID.
- **Path Variable**: `id` (String) - Company identifier
- **Returns**: Company object or null

### `POST /api/company/ids`
Imports all companies from external CSRC website.
- **Returns**: List of created Company objects
- **Process**:
  1. Retrieves company IDs from external source
  2. Fetches details for each company
  3. Saves all companies to database

## Usage
Called by frontend applications and admin tools to manage company data.

## Response Format
Returns raw objects (not wrapped in ResponseObject) - direct entity or collection responses.

## Design Pattern
- **REST Controller**: HTTP request handling
- **Dependency Injection**: Service layer integration
- **External Data Import**: Integration with web scraping service

## Note
The `importAll()` endpoint may take significant time as it fetches data for all companies from the external website.
