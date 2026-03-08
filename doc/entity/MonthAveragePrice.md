# MonthAveragePrice

## Package
`net.canglong.fund.entity`

## Description
An interface-based projection for native query results. Used to map the results of a native SQL query that calculates monthly average prices.

## Type
Interface (not a class) - Spring Data JPA projection

## Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| getMonth() | Integer | Returns the month identifier (year * 100 + month) |
| getAveragePrice() | BigDecimal | Returns the calculated average price for the month |

## Usage
Used with Spring Data JPA native queries to map query results without creating a full entity. The `PriceRepo.findAllMonthAveragePriceByFundId()` method returns a list of this interface.

## Query Example
The native query calculates:
```sql
SELECT year(price_date)*100+month(price_date) as month,
       cast(avg(accumulated_price) as decimal(7,4)) as averagePrice
FROM fund_price
WHERE fund_id = :fundId AND price_date > :startDate
GROUP BY year(price_date), month(price_date)
```

## Design Pattern
This follows the Spring Data JPA interface-based projection pattern, which allows mapping query results to an interface without requiring a concrete implementation class.
