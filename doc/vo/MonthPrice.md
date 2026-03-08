# MonthPrice

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a fund's monthly price data for a specific year. Contains a list of price records for each month.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| fundId | String | Fund ID |
| fundName | String | Fund name |
| year | int | Year of the price data |
| priceList | List<DatePriceIdentity> | List of price records for each month |

## Usage
Used in:
- `PriceService.findMonthPriceById()` - Returns monthly price data for a fund in a specific year
- Chart data generation for monthly views
- Monthly return rate calculations

## Design Pattern
This VO aggregates monthly price data for a fund, providing a convenient structure for working with year-based price information.
