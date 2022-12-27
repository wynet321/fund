package net.canglong.fund.controller;

import net.canglong.fund.service.FundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/fund", headers = "Accept=application/json")
public class FundController {

    @Resource
    private FundService fundService;

    @GetMapping(value = "/{id}")
    public Object find(@PathVariable("id") String id) {
        return fundService.findById(id);
    }

}
