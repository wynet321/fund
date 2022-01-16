package net.soryu.fund.controller;

import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/fund", headers = "Accept=application/json")
public class FundController {

    @Resource
    private FundService fundService;

    @Resource
    private CompanyService companyService;

    @GetMapping(value = "/{id}")
    public Object find(@PathVariable("id") String id) throws Exception {
        return fundService.findById(id);
    }

}
