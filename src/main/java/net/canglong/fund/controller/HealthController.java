package net.canglong.fund.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/health", headers = "Accept=application/json")
public class HealthController {

    @GetMapping
    public Object health() throws Exception {
        Map<String, Boolean> health = new HashMap<String, Boolean>();
        health.put("health", true);
        return health;
    }
}
