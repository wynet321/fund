package net.canglong.fund.controller;

import net.canglong.fund.entity.Company;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.WebsiteDataService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/company", headers = "Accept=application/json")
public class CompanyController {

    @Resource
    private CompanyService companyService;
    @Resource
    private WebsiteDataService dataService;

    @GetMapping
    public Object findAll(Pageable pageable) throws Exception {
        return companyService.find(pageable);
    }

    @GetMapping(value = "/{id}")
    public Object find(@PathVariable("id") String id) throws Exception {
        return companyService.find(id);
    }

    @PostMapping(value = "/ids")
    public Object importAll() throws Exception {
        List<String> companyIds = dataService.getCompanyIds();
        List<Company> companies = new LinkedList<Company>();
        for (String id : companyIds) {
            Company company = dataService.getCompany(id);
            companies.add(company);
        }
        return companyService.create(companies);
    }
}
