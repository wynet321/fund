# ResponseObject

## Package
`net.canglong.fund.entity`

## Description
A generic wrapper class for API responses. Provides a standardized structure for all API responses with success status, message, and data payload.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString

## Fields

| Field | Type | Description |
|-------|------|-------------|
| success | boolean | Indicates whether the operation was successful |
| message | String | Human-readable message (typically for errors) |
| data | Object | The actual response payload (can be any type) |

## Usage
Used as a standardized response format for REST API endpoints. Provides:
- Consistent response structure across all endpoints
- Clear success/failure indication
- Error message propagation
- Flexible data payload

## Example Response
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": "000001",
    "name": "Example Fund"
  }
}
```

## Note
While defined in the entity package, this is not a JPA entity. It's a DTO (Data Transfer Object) for API responses.
