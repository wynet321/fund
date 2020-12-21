package net.cheetahead.fund.service.impl;

import net.cheetahead.fund.entity.Company;
import net.cheetahead.fund.repository.CompanyRepo;
import net.cheetahead.fund.service.CompanyService;
import net.cheetahead.fund.service.DataService;
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
    private DataService dataService;

    @Override
    public Page<Company> findAll(Pageable pageable) throws Exception {
        return companyRepo.findAll(pageable);
    }

    @Override
    public Company create(Company company) throws Exception {
        return companyRepo.save(company);
    }

    @Override
    public List<Company> createAll() throws Exception {
        return companyRepo.saveAll(dataService.fetchCompanies());
    }

    @Override
    public Company update(Company company) throws Exception {
        return companyRepo.save(company);
    }

    @Override
    public List<Company> updateAll() throws Exception {
        return companyRepo.saveAll(dataService.fetchCompanies());
    }

    @Override
    public Company findById(String id) throws Exception {
        Optional<Company> optional = companyRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

}
