# StatisticScheduler

## Package
`net.canglong.fund.schedule`

## Description
Spring scheduled task component that automates statistics generation for funds. Runs background jobs to calculate and store return rates.

## Annotations
- `@Component` - Spring component
- `@Log4j2` - Lombok logging

## Static Field

| Field | Type | Description |
|-------|------|-------------|
| monitorRequired | boolean | Flag to control status reporting |

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| rateService | RateService | Constructor | Service for rate operations |

## Constructor
```java
public StatisticScheduler(RateService rateService)
```
Initializes the scheduler with RateService and sets `monitorRequired` to false.

## Scheduled Tasks

### `runStatisticGeneration()`
Automatically generates statistics for default fund types.
- **Schedule**: `@Scheduled(fixedDelay = 108000000)` - Every 30 hours
- **Default Types**: 混合型, 股票型, 债券型, QDII, 短期理财债券型
- **Process**:
  1. Sets `monitorRequired = true`
  2. Calls `rateService.generateStatisticData()` with default types
  3. Does not refresh all data (incremental update)

### `reportStatisticJobStatus()`
Reports the status of the statistics generation job.
- **Schedule**: `@Scheduled(fixedDelay = 60000)` - Every 60 seconds
- **Condition**: Only reports when `monitorRequired` is true
- **Logging**: Outputs progress information including:
  - Left fund count
  - Elapsed time
  - Active threads
- **Completion**: Sets `monitorRequired = false` when job terminates

## Usage
Automatically runs as part of the Spring application. No manual invocation required.

## Design Pattern
- **Scheduled Task**: Automated background processing
- **State Management**: Static flag for cross-method communication
- **Logging**: Structured progress reporting

## Configuration
Schedules are fixed and defined in code. To change intervals, modify the `fixedDelay` values (in milliseconds).
