# PriceIdentity

## Package
`net.canglong.fund.entity`

## Description
Composite primary key class for the `Price` entity. Represents the unique identifier for a price record, combining fund ID and date.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Embeddable` - JPA annotation indicating this class can be embedded in an entity
- `@NoArgsConstructor` - Lombok: generates no-args constructor
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Implements
- `Serializable` - Required for JPA composite keys

## Fields

| Field | Type | Column Name | Description |
|-------|------|-------------|-------------|
| fundId | String | fund_id | The fund identifier |
| priceDate | LocalDate | price_date | The date of the price record |

## Methods

### hashCode()
Custom hash code implementation combining fundId and priceDate.

### equals(Object obj)
Custom equality check comparing fundId and priceDate fields.

## Usage
Used as the `@EmbeddedId` in the `Price` entity to create a composite primary key. This allows multiple price records per fund (one per date).

## Design Pattern
This class follows the JPA composite primary key pattern using `@Embeddable` and `@EmbeddedId`.
