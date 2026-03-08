# FundServiceImpl

## Package
`net.canglong.fund.service.impl`

## Description
Implementation of `FundService` interface. Provides concrete business logic for fund management operations.

## Annotations
- `@Service` - Spring annotation marking this as a service bean

## Implements
- `FundService`

## Dependencies

| Field | Type | Injection | Description |
|-------|------|-----------|-------------|
| fundRepo | FundRepo | `@Resource` | Repository for fund data access |

## Constructor
Default constructor (no-args), dependencies injected by Spring.

## Method Implementations

### `Page<Fund> get(Pageable pageable)`
Delegates to `fundRepo.findAll(pageable)`.

### `Optional<Fund> findById(String id)`
Delegates to `fundRepo.findById(id)`.

### `Page<Fund> findByCompanyId(String companyId, Pageable pageable)`
Delegates to `fundRepo.findAllByCompanyId(companyId, pageable)`.

### `Fund create(Fund fund)`
Delegates to `fundRepo.save(fund)`.

### `Fund update(Fund fund)`
Delegates to `fundRepo.save(fund)`.

### `List<Fund> create(List<Fund> funds)`
Delegates to `fundRepo.saveAll(funds)`.

### `List<Fund> findAll()`
Delegates to `fundRepo.findAll()`.

### `Fund findByName(String name)`
Delegates to `fundRepo.findByName(name)`.

### `List<Fund> findAllByType(String type)`
Delegates to `fundRepo.findAllByType(type)`.

### `List<Fund> findAllExcludesType(String type)`
Delegates to `fundRepo.findAllExcludesType(type)`.

### `List<Fund> findAllByTypes(List<String> types)`
Delegates to `fundRepo.findAllByTypes(types)`.

### `List<String> findAllTypes()`
Delegates to `fundRepo.findAllTypes()`.

### `List<Fund> searchByNameContaining(String keyword, int limit)`
Calls `fundRepo.findByNameContaining(keyword)` and limits results.

### `List<Fund> searchByNameOrIdContaining(String keyword, int limit)`
Calls `fundRepo.findByNameOrIdContaining(keyword)` and limits results.

## Usage
Autowired by controllers and other services requiring fund operations.

## Design Pattern
- **Service Layer**: Implements business logic
- **Repository Pattern**: Delegates data access to repository
- **Dependency Injection**: Uses `@Resource` for loose coupling
