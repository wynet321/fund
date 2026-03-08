# FundApplication

## Package
`net.canglong.fund`

## Description
The main entry point for the Fund Management Spring Boot application. This class bootstraps the entire application and enables key Spring features.

## Annotations
- `@SpringBootApplication` - Marks this as a Spring Boot application with auto-configuration
- `@EnableJpaRepositories` - Enables JPA repositories for database access
- `@EnableScheduling` - Enables scheduled task execution
- `@EnableAsync(proxyTargetClass = true)` - Enables asynchronous method execution with CGLIB proxy

## Main Method
```java
public static void main(String[] args)
```
The entry point that starts the Spring Boot application.

## Responsibilities
- Application bootstrap and initialization
- Enable JPA repository support
- Enable scheduled tasks (for data ingestion and statistics generation)
- Enable asynchronous processing for concurrent operations
