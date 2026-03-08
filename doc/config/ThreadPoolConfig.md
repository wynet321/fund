# ThreadPoolConfig

## Package
`net.canglong.fund.config`

## Description
Configuration class for thread pool setup. Defines multiple thread pools for concurrent processing of different types of tasks.

## Annotations
- `@Configuration` - Spring configuration class

## Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `fund.threadCount` | 20 | Base thread count for all pools |

## Beans

### `priceExecutor` (Bean name: "priceExecutor")
Thread pool for price data processing tasks.
- **Core Pool Size**: `threadCount`
- **Max Pool Size**: `threadCount * 2`
- **Thread Name Prefix**: "Price-Task-"
- **Rejection Policy**: `AbortPolicy` - Rejects tasks when pool is saturated
- **Shutdown**: Waits for tasks to complete, 60-second timeout

### `fundExecutor` (Bean name: "fundExecutor")
Thread pool for fund data processing tasks.
- **Core Pool Size**: `threadCount`
- **Max Pool Size**: `threadCount * 2`
- **Thread Name Prefix**: "Fund-Task-"
- **Rejection Policy**: `AbortPolicy`
- **Shutdown**: Waits for tasks to complete, 60-second timeout

### `statisticExecutor` (Bean name: "statisticExecutor")
Thread pool for statistics generation tasks.
- **Core Pool Size**: `threadCount`
- **Max Pool Size**: `threadCount * 2`
- **Thread Name Prefix**: "Statistic-Task-"
- **Rejection Policy**: `AbortPolicy`
- **Shutdown**: Waits for tasks to complete, 60-second timeout

## Usage
Injected into services using `@Resource(name = "beanName")`:
- `PriceServiceImpl` uses `priceExecutor` and `fundExecutor`
- `RateServiceImpl` uses `statisticExecutor`

## Design Pattern
- **Configuration Class**: Centralized thread pool configuration
- **Bean Definition**: Named beans for selective injection
- **Resource Management**: Graceful shutdown handling

## Tuning
Adjust `fund.threadCount` in `application.yml` based on:
- Available CPU cores
- Memory constraints
- External API rate limits
- Database connection pool size
