# YearAverageRate

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a fund's average return rate and standard deviation over a multi-year period. Used for fund ranking and performance analysis.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| fundId | String | Fund ID |
| name | String | Fund name |
| companyName | String | Company name |
| type | String | Fund type |
| average | BigDecimal | Average annual return rate over the period |
| stddev | BigDecimal | Standard deviation of annual returns (volatility measure) |

## Usage
Used in:
- `RateService.getYearAverageRankByTypesAndPeriod()` - Returns ranked funds by average return
- Fund ranking and comparison features
- Performance analysis with risk metrics (standard deviation)

## Calculation
- **Average**: Mean of annual return rates over the specified period
- **Stddev**: Standard deviation of annual return rates, measuring volatility

## Design Pattern
This VO encapsulates statistical metrics for fund performance analysis, combining return metrics with risk metrics.
