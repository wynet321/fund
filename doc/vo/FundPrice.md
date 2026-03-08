# FundPrice

## Package
`net.canglong.fund.vo`

## Description
Value Object representing a fund's price information at a specific point in time. Used for transferring price data between service layers and for API responses.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@AllArgsConstructor` - Lombok: generates all-args constructor
- `@NoArgsConstructor` - Lombok: generates no-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| accumulatedPrice | BigDecimal | Accumulated net asset value (累计净值) |
| currentPrice | BigDecimal | Current net asset value per unit (单位净值) |

## Usage
Used in:
- `PriceService.findYearPriceMapById()` - Returns a map of year to FundPrice
- `PriceService.findMonthPriceMapById()` - Returns a map of month to FundPrice
- Internal data transfer between service methods

## Design Pattern
This is a simple Value Object (VO) / Data Transfer Object (DTO) pattern, used to encapsulate related price data for transfer between layers.
