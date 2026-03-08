# FundPercentage

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a fund's return percentage over a specific date range. Used for calculating and displaying fund performance between two dates.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| id | String | Fund ID |
| name | String | Fund name |
| percentage | String | Return percentage as a formatted string (e.g., "15.50%") |
| startDate | LocalDate | Start date of the period |
| endDate | LocalDate | End date of the period |

## Usage
Used in:
- `PriceService.findPercentageByDate()` - Calculates and returns the return percentage between two dates
- API responses for period-based performance queries

## Calculation
The percentage is calculated as:
```
(accumulatedPriceAtEnd - accumulatedPriceAtStart) / accumulatedPriceAtStart
```

## Design Pattern
This VO encapsulates the result of a percentage calculation, including the input parameters (dates) and the calculated result.
