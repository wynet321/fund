# YearPrice

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a fund's yearly price data across all years. Contains a list of price records for year-end dates.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| fundId | String | Fund ID |
| fundName | String | Fund name |
| priceList | List<DatePriceIdentity> | List of price records for year-end dates |

## Usage
Used in:
- `PriceService.findYearPriceById()` - Returns yearly price data for a fund
- `PriceService.findYearPriceMapById()` - Converts YearPrice to a Map for easier access
- Annual return rate calculations
- Long-term performance analysis

## Data Points
The priceList typically contains:
- Year-end prices (December 31st) for each year
- The price at fund creation (if not on year-end)

## Design Pattern
This VO aggregates yearly price data for a fund, providing a convenient structure for working with multi-year price information.
