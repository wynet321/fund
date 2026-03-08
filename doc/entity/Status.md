# Status

## Package
`net.canglong.fund.entity`

## Description
A simple data class representing the status of background jobs (price retrieval, statistics generation). Not a JPA entity - used for in-memory status tracking and API responses.

## Annotations
- `@Data` - Lombok annotation generating getters, setters, equals, hashCode, and toString

## Fields

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| totalFundCount | int | 0 | Total number of funds to process |
| leftFundCount | int | 0 | Number of funds remaining to process |
| elapseTime | long | 0L | Elapsed time in seconds |
| terminated | boolean | false | Whether the job has completed |
| aliveThreadCount | int | 0 | Number of active threads |
| taskCount | long | 0 | Total number of tasks submitted |

## Usage
Used to track and report the status of:
- Price data retrieval jobs
- Statistics generation jobs

Returned by service methods to provide real-time job progress information to clients.

## Note
This is not a database entity. Status information is transient and only valid during job execution.
