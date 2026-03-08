# PeriodRate

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund's return rates over multiple time periods (1-10 years). Maps to the `fund_return_rate_by_period` table in the database. Stores calculated multi-year performance metrics.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "fund_return_rate_by_period")` - Specifies the database table name

## Fields

| Field | Type | Column Name | Description |
|-------|------|-------------|-------------|
| id | String | id | Primary key (same as fund ID) |
| name | String | name | Fund name (denormalized) |
| companyName | String | company_name | Company name (denormalized) |
| type | String | type | Fund type (denormalized) |
| oneYearRate | BigDecimal | one_year_rate | 1-year return rate |
| twoYearRate | BigDecimal | two_year_rate | 2-year return rate |
| threeYearRate | BigDecimal | three_year_rate | 3-year return rate |
| fourYearRate | BigDecimal | four_year_rate | 4-year return rate |
| fiveYearRate | BigDecimal | five_year_rate | 5-year return rate |
| sixYearRate | BigDecimal | six_year_rate | 6-year return rate |
| sevenYearRate | BigDecimal | seven_year_rate | 7-year return rate |
| eightYearRate | BigDecimal | eight_year_rate | 8-year return rate |
| nineYearRate | BigDecimal | nine_year_rate | 9-year return rate |
| tenYearRate | BigDecimal | ten_year_rate | 10-year return rate |

## Usage
Stores pre-calculated multi-period return rates for funds. Used for:
- Long-term performance analysis
- Period-based fund ranking
- Performance comparison across different time horizons
- Investment decision support

## Calculation
Rates are calculated by comparing the accumulated NAV at the end of each period with the accumulated NAV at the beginning of the period.
