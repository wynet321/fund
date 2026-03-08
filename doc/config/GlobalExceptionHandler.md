# GlobalExceptionHandler (config package)

## Package
`net.canglong.fund.config`

## Description
Global exception handler for the application. Catches and handles exceptions thrown by controllers, converting them to appropriate HTTP responses.

## Annotations
- `@ControllerAdvice` - Spring advice for all controllers
- `@Log4j2` - Lombok logging

## Exception Handlers

### `handleGlobalException(Exception ex, WebRequest request)`
Handles all uncaught exceptions.
- **Annotation**: `@ExceptionHandler(Exception.class)`
- **Response**: HTTP 500 (Internal Server Error)
- **Body**:
  ```json
  {
    "timestamp": "2024-01-01T12:00:00",
    "message": "Internal server error occurred",
    "details": "uri=/api/fund/123"
  }
  ```
- **Logging**: ERROR level with stack trace

### `handleBusinessException(BusinessException ex, WebRequest request)`
Handles business logic exceptions.
- **Annotation**: `@ExceptionHandler(BusinessException.class)`
- **Response**: HTTP 400 (Bad Request)
- **Body**:
  ```json
  {
    "timestamp": "2024-01-01T12:00:00",
    "message": "Business error message",
    "details": "uri=/api/fund/123"
  }
  ```
- **Logging**: WARN level

## Usage
Automatically applied to all controllers. No explicit configuration needed.

## Design Pattern
- **Controller Advice**: Cross-cutting exception handling
- **Exception Translation**: Converts exceptions to HTTP responses
- **Logging**: Different levels based on exception type

## Note
This handler provides generic error messages for security. Detailed error information is logged but not exposed to clients.
