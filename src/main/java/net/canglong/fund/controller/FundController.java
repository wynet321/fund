package net.canglong.fund.controller;

import javax.annotation.Resource;
import net.canglong.fund.service.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/fund", headers = "Accept=application/json")
public class FundController {

  @Resource
  private FundService fundService;

  @GetMapping(value = "/{id}")
  public Object find(@PathVariable("id") String id) {
    return fundService.findById(id);
  }

  @GetMapping(value = "/types")
  public Object getTypes() {
    return fundService.findAllTypes();
  }
}
