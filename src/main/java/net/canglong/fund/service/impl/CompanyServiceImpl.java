package net.canglong.fund.service.impl;

import net.canglong.fund.entity.Company;
import net.canglong.fund.repository.CompanyRepo;
import net.canglong.fund.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyRepo companyRepo;

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
    public Company update(Company company) {
        return companyRepo.save(company);
    }

    @Override
    public List<Company> update(List<Company> companies) {
        return companyRepo.saveAll(companies);
    }

    @Override
    public Company find(String id) throws Exception {
        Optional<Company> optional = companyRepo.findById(id);
        return optional.orElse(null);
    }

}
