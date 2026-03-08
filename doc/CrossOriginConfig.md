# CrossOriginConfig

## Package
`net.canglong.fund`

## Description
Configuration class for Cross-Origin Resource Sharing (CORS). Enables the API to be accessed from different domains.

## Annotations
- `@Configuration` - Spring configuration class

## Implements
- `WebMvcConfigurer` - Spring MVC configuration interface

## Method

### `addCorsMappings(CorsRegistry registry)`
Configures CORS mappings for the application.
- **Mapping**: `/**` (applies to all endpoints)
- **Allowed Headers**: `*` (all headers)
- **Allowed Origins**: `*` (all origins via patterns)
- **Allowed Methods**: `*` (all HTTP methods)
- **Allow Credentials**: `false` (disabled for wildcard origins per CORS spec)

## Configuration Details

| Setting | Value | Description |
|---------|-------|-------------|
| Path | `/**` | All endpoints |
| Headers | `*` | All headers allowed |
| Origins | `*` | All origins allowed |
| Methods | `*` | GET, POST, PUT, DELETE, etc. |
| Credentials | `false` | Cookies/auth not sent |

## Usage
Automatically applied to all controllers. No explicit configuration needed.

## Design Pattern
- **Configuration Class**: Centralized CORS configuration
- **Interface Implementation**: WebMvcConfigurer for MVC customization

## Security Considerations
- This configuration allows requests from any origin
- Credentials (cookies, authorization headers) are disabled
- For production, consider restricting origins to specific domains:
  ```java
  registry.addMapping("/**")
      .allowedOrigins("https://yourdomain.com")
      .allowCredentials(true);
  ```

## Note
When using wildcard (`*`) for origins, the CORS specification requires `allowCredentials` to be `false`.
