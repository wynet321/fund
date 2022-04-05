package net.soryu.fund.service.impl;

import net.soryu.fund.entity.Company;
import net.soryu.fund.repository.CompanyRepo;
import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.WebsiteDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyRepo companyRepo;
    @Resource
    private WebsiteDataService dataService;

    @Override
    public Page<Company> find(Pageable pageable) throws Exception {
        return companyRepo.findAll(pageable);
    }

    @Override
    public Company create(Company company) throws Exception {
        return companyRepo.save(company);
    }

    @Override
    public List<Company> create(List<Company> companies) throws Exception {
        return companyRepo.saveAll(companies);
    }

    @Override
    public Company update(Company company) throws Exception {
        return companyRepo.save(company);
    }

    @Override
    public List<Company> update(List<Company> companies) throws Exception {
        return companyRepo.saveAll(companies);
    }

    @Override
    public Company find(String id) throws Exception {
        Optional<Company> optional = companyRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

}
