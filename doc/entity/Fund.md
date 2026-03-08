# Fund

## Package
`net.canglong.fund.entity`

## Description
Entity class representing a fund (投资基金) in the system. Maps to the `fund` table in the database.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString
- `@Entity` - JPA annotation marking this as a database entity
- `@Table(name = "fund")` - Specifies the database table name

## Fields

| Field | Type | Column Name | Description |
|-------|------|-------------|-------------|
| id | String | id | Primary key, fund code (基金代码) |
| parentId | String | parent_id | Parent fund ID for structured funds |
| name | String | name | Fund name (基金名称) |
| companyId | String | company_id | Company ID that manages this fund |
| type | String | type | Fund type (e.g., 混合型, 股票型, 债券型) |
| currentPage | int | current_page | Current page for data retrieval tracking |
| statisticDueDate | LocalDate | statistic_due_date | Date when statistics were last updated |
| startDate | LocalDate | start_date | Fund inception/start date |
| endDate | LocalDate | end_date | Latest available data date |

## Relationships
- Many-to-One with Company (via companyId)
- One-to-Many with Price (via fund ID)
- One-to-Many with MonthRate (via fund ID)
- One-to-Many with YearRate (via fund ID)

## Usage
Used throughout the application to represent fund information, including data retrieval, statistics calculation, and API responses.
