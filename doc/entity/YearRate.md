# YearRate

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund's annual return rate. Maps to the `fund_return_rate_by_year` table in the database. Stores calculated yearly performance metrics.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "fund_return_rate_by_year")` - Specifies the database table name
- `@AllArgsConstructor` - Lombok: generates all-args constructor
- `@NoArgsConstructor` - Lombok: generates no-args constructor

## Fields

| Field | Type | Description |
|-------|------|-------------|
| yearRateIdentity | YearRateIdentity | Composite primary key (fundId + year) |
| name | String | Fund name (denormalized for query performance) |
| companyName | String | Company name (denormalized for query performance) |
| type | String | Fund type (denormalized for query performance) |
| rate | BigDecimal | Annual return rate (年收益率) |

## Composite Key
Uses `YearRateIdentity` as an embedded composite primary key containing:
- `fundId` - The fund identifier
- `year` - The year of the rate record

## Usage
Stores pre-calculated annual return rates for funds. Used for:
- Annual performance analysis
- Year-over-year comparison
- Performance ranking
- Statistics generation
- Multi-year return analysis

## Denormalization Note
The `name`, `companyName`, and `type` fields are denormalized (duplicated from Fund and Company entities) to improve query performance and simplify reporting queries.
