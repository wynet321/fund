# Company

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund management company (基金公司) in the system. Maps to the `company` table in the database.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "company")` - Specifies the database table name

## Fields

| Field | Type | Column Name | Description |
|-------|------|-------------|-------------|
| id | String | id | Primary key, company code |
| name | String | name | Full company name (公司名称) |
| abbr | String | abbr | Company abbreviation/short name |
| createdOn | Date | created_on | Company establishment date |
| address | String | address | Company office address |

## Annotations on Fields
- `@JsonFormat(pattern = "yyyy-MM-dd")` on `createdOn` - Formats date as "yyyy-MM-dd" in JSON responses

## Relationships
- One-to-Many with Fund (a company manages multiple funds)

## Usage
Represents fund management companies that issue and manage investment funds. Used in company management, fund categorization, and statistics aggregation.
