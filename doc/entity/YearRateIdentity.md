# YearRateIdentity

## Package
`net.canglong.fund.entity`

## Description
Composite primary key class for the `YearRate` entity. Represents the unique identifier for an annual rate record, combining fund ID and year.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Embeddable` - JPA annotation indicating this class can be embedded in an entity
- `@NoArgsConstructor` - Lombok: generates no-args constructor
- `@AllArgsConstructor` - Lombok: generates all-args constructor

## Implements
- `Serializable` - Required for JPA composite keys

## Fields

| Field | Type | Description |
|-------|------|-------------|
| fundId | String | The fund identifier |
| year | int | The year of the rate record |

## Methods

### hashCode()
Custom hash code implementation combining fundId and year.

### equals(Object obj)
Custom equality check comparing fundId and year fields.

## Usage
Used as the `@EmbeddedId` in the `YearRate` entity to create a composite primary key. This allows one annual rate record per fund per year.

## Design Pattern
This class follows the JPA composite primary key pattern using `@Embeddable` and `@EmbeddedId`.
