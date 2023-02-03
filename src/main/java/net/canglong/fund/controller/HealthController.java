package net.canglong.fund.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/health", headers = "Accept=application/json")
public class HealthController {

  @GetMapping
  public Object health() {
    Map<String, Boolean> health = new HashMap<>();
    health.put("health", true);
    return health;
  }
}
