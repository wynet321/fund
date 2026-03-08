# GlobalExceptionHandler (exception package)

## Package
`net.canglong.fund.exception`

## Description
Alternative global exception handler using `ResponseObject` for responses. Provides structured error responses compatible with the application's API format.

## Exception Handlers

### `handleTypeMismatch(MethodArgumentTypeMismatchException ex)`
Handles type conversion errors in request parameters.
- **Response**: HTTP 400 (Bad Request)
- **Body**:
  ```json
  {
    "success": false,
    "message": "Invalid parameter: parameterName",
    "data": null
  }
  ```

### `handleValidation(MethodArgumentNotValidException ex)`
Handles validation errors.
- **Response**: HTTP 400 (Bad Request)
- **Body**:
  ```json
  {
    "success": false,
    "message": "Validation failed",
    "data": null
  }
  ```

### `handleIllegalArgument(IllegalArgumentException ex)`
Handles illegal argument exceptions.
- **Response**: HTTP 400 (Bad Request)
- **Body**:
  ```json
  {
    "success": false,
    "message": "Error message from exception",
    "data": null
  }
  ```

### `handleGeneric(Exception ex)`
Handles all other exceptions.
- **Response**: HTTP 500 (Internal Server Error)
- **Body**:
  ```json
  {
    "success": false,
    "message": "Internal server error",
    "data": null
  }
  ```

## Usage
This handler is in the `exception` package and may be used as an alternative to the one in `config` package. The application should typically use only one global exception handler.

## Design Pattern
- **Controller Advice**: Cross-cutting exception handling
- **Consistent Response Format**: Uses ResponseObject for all errors
- **Security**: Generic messages for server errors

## Note
This handler is currently not annotated with `@ControllerAdvice`, so it may not be active. Check the application configuration to determine which handler is in use.
