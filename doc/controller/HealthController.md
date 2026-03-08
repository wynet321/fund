# HealthController

## Package
`net.canglong.fund.controller`

## Description
REST controller providing a health check endpoint for monitoring application status.

## Annotations
- `@RestController` - Spring REST controller
- `@RequestMapping(value = "/api/health")` - Base URL path

## Endpoints

### `GET /api/health`
Returns the health status of the application.
- **Returns**: Map with "health" key and boolean value

## Response Example
```json
{
  "health": true
}
```

## Usage
Used by:
- Load balancers for health checks
- Monitoring systems to verify application availability
- Deployment pipelines to confirm successful startup

## Design Pattern
- **REST Controller**: HTTP request handling
- **Health Check Pattern**: Simple availability verification

## Note
This is a basic health check that only verifies the application is running. It does not check database connectivity or other dependencies.
