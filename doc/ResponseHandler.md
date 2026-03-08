# ResponseHandler

## Package
`net.canglong.fund`

## Description
Aspect-oriented response handler that wraps controller responses in a standardized `ResponseObject` format. Currently disabled but kept for reference.

## Annotations
- `@Log4j2` - Lombok logging
- `// @Aspect` - Intentionally commented out to disable

## Pointcut

### `httpResponse()`
Defines the pointcut for all controller methods.
- **Expression**: `within(net.canglong.fund.controller.*)`
- **Target**: All methods in controller package

## Methods

### `handleController(ProceedingJoinPoint proceedingJoinPoint)`
Wraps controller method responses in ResponseObject.
- **Status**: Currently commented out (`// @Around`)
- **Logic**:
  - Null response → `success: false, data: null`
  - Boolean response → `success: boolean value`
  - Map response → `success: !map.isEmpty()`
  - Other response → `success: true, data: object`
  - Exception → `success: false, message: "Internal server error"`

### `handleException(Throwable throwable)`
Creates error response for exceptions.
- Returns ResponseObject with `success: false`
- Logs error details
- Generic message for security

## Usage
Currently disabled. To enable:
1. Uncomment `@Aspect` annotation
2. Uncomment `@Around` annotation on `handleController`

## Design Pattern
- **Aspect-Oriented Programming (AOP)**: Cross-cutting response wrapping
- **Response Wrapper**: Consistent API response format
- **Exception Handling**: Centralized error response generation

## Response Format
When enabled, all responses would be wrapped as:
```json
{
  "success": true/false,
  "message": "error message if applicable",
  "data": { ... }
}
```

## Note
This class is kept for reference but not active. Controllers currently return raw objects. To standardize responses, either:
1. Enable this aspect
2. Manually wrap responses in controllers
3. Use the `exception.GlobalExceptionHandler` which uses ResponseObject
