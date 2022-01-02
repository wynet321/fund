package net.soryu.fund.controller;

import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.DataService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/company", headers = "Accept=application/json")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @Resource
    private DataService dataService;

    @GetMapping
    public Object findAll(Pageable pageable) throws Exception {
        return companyService.find(pageable);
    }

    @GetMapping(value = "/{id}")
    public Object find(@PathVariable("id") String id) throws Exception {
        return companyService.find(id);
    }

    @PostMapping
    public Object fetch() throws Exception {
        return companyService.create(dataService.getCompanyListFromWebsite());
    }
}
