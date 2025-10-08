package net.canglong.fund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class FundApplication {

  public static void main(String[] args) {
    SpringApplication.run(FundApplication.class, args);
  }

}
