# DatePriceIdentity

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a price record with its associated date. Used for transferring dated price data in API responses and internal processing.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor
- `@NoArgsConstructor` - Lombok: generates no-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| priceDate | LocalDate | The date of the price record |
| accumulatedPrice | BigDecimal | Accumulated net asset value on that date |
| currentPrice | BigDecimal | Current net asset value per unit on that date |

## Usage
Used in:
- `YearPrice` - Contains a list of DatePriceIdentity objects for yearly price data
- `MonthPrice` - Contains a list of DatePriceIdentity objects for monthly price data
- Chart data generation
- Price history responses

## Design Pattern
This is a Value Object (VO) that combines a date with its corresponding price information, making it easy to work with time-series price data.
