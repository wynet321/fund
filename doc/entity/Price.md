# Price

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund's daily price/net value record. Maps to the `fund_price` table in the database. Stores historical price data for funds.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "fund_price")` - Specifies the database table name

## Fields

| Field | Type | Column Name | Description |
|-------|------|-------------|-------------|
| priceIdentity | PriceIdentity | - | Composite primary key (fundId + priceDate) |
| returnOfTenKilo | BigDecimal | return_of_ten_kilo | Return per 10,000 units (万份收益) - for currency funds |
| sevenDayAnnualizedRateOfReturn | BigDecimal | seven_day_annualized_rate_of_return | 7-day annualized return (七日年化收益率) - for currency funds |
| price | BigDecimal | price | Current net asset value per unit (单位净值) |
| accumulatedPrice | BigDecimal | accumulated_price | Accumulated net asset value (累计净值) |

## Composite Key
Uses `PriceIdentity` as an embedded composite primary key containing:
- `fundId` - The fund identifier
- `priceDate` - The date of the price record

## Usage
Stores daily historical price data for all funds. Used for:
- Price history queries
- Return rate calculations
- Chart data generation
- Performance analysis

## Special Handling
Different fields are populated based on fund type:
- Currency funds (货币型): Uses `returnOfTenKilo` and `sevenDayAnnualizedRateOfReturn`
- Non-currency funds: Uses `price` and `accumulatedPrice`
