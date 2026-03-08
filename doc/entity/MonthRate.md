# MonthRate

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund's monthly return rate. Maps to the `fund_return_rate_by_month` table in the database. Stores calculated monthly performance metrics.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "fund_return_rate_by_month")` - Specifies the database table name

## Fields

| Field | Type | Description |
|-------|------|-------------|
| monthRateIdentity | MonthRateIdentity | Composite primary key (fundId + year + month) |
| name | String | Fund name (denormalized for query performance) |
| companyName | String | Company name (denormalized for query performance) |
| type | String | Fund type (denormalized for query performance) |
| rate | BigDecimal | Monthly return rate (月收益率) |

## Composite Key
Uses `MonthRateIdentity` as an embedded composite primary key containing:
- `fundId` - The fund identifier
- `year` - The year of the rate record
- `month` - The month of the rate record

## Usage
Stores pre-calculated monthly return rates for funds. Used for:
- Monthly performance analysis
- Return rate queries by year/month
- Performance comparison charts
- Statistics generation

## Denormalization Note
The `name`, `companyName`, and `type` fields are denormalized (duplicated from Fund and Company entities) to improve query performance and simplify reporting queries.
