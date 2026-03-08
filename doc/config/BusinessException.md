# BusinessException

## Package
`net.canglong.fund.config`

## Description
Custom runtime exception for business logic errors. Used to distinguish application-specific errors from system errors.

## Extends
- `RuntimeException`

## Constructors

### `BusinessException(String message)`
Creates a business exception with a message.

### `BusinessException(String message, Throwable cause)`
Creates a business exception with a message and underlying cause.

## Usage
Throw when:
- Business rules are violated
- Validation fails
- Expected conditions are not met
- User input is invalid

## Handling
Caught by `GlobalExceptionHandler` and returned as HTTP 400 (Bad Request) with the exception message.

## Example
```java
if (fund == null) {
    throw new BusinessException("Fund not found with id: " + id);
}
```

## Design Pattern
- **Custom Exception**: Domain-specific error type
- **Unchecked Exception**: Extends RuntimeException for flexibility

## Note
Unlike generic exceptions, BusinessException messages are safe to expose to clients as they describe business conditions, not system errors.
