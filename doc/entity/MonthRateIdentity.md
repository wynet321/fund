# MonthRateIdentity

## Package
`net.canglong.fund.entity`

## Description
Composite primary key class for the `MonthRate` entity. Represents the unique identifier for a monthly rate record, combining fund ID, year, and month.

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
| year | int | year | The year of the rate record |
| month | int | month | The month of the rate record |

## Methods

### hashCode()
Custom hash code implementation combining fundId, year, and month.

### equals(Object obj)
Custom equality check comparing fundId, year, and month fields.

## Usage
Used as the `@EmbeddedId` in the `MonthRate` entity to create a composite primary key. This allows one monthly rate record per fund per month.

## Design Pattern
This class follows the JPA composite primary key pattern using `@Embeddable` and `@EmbeddedId`.
