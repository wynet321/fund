package net.canglong.fund;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedHeaders("*")
        .allowedOriginPatterns("*")
        .allowedMethods("*")
        // When using wildcard origins, do not allow credentials per CORS spec
        .allowCredentials(false);
  }
}
