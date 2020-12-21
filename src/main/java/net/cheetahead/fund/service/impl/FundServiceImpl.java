package net.cheetahead.fund.service.impl;

import net.cheetahead.fund.entity.Company;
import net.cheetahead.fund.entity.Fund;
import net.cheetahead.fund.repository.FundRepo;
import net.cheetahead.fund.service.CompanyService;
import net.cheetahead.fund.service.DataService;
import net.cheetahead.fund.service.FundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

@Service
public class FundServiceImpl implements FundService {

    @Resource
    private FundRepo fundRepo;

    @Resource
    private CompanyService companyService;

    @Resource
    private DataService dataService;

    @Override
    public Page<Fund> get(Pageable pageable) throws Exception {
        return fundRepo.findAll(pageable);
    }

    @Override
    public List<Fund> createByCompanyId(String companyId) throws Exception {
        return fundRepo.saveAll(dataService.fetchFunds(companyService.findById(companyId)));
    }

    @Override
    public Fund findById(String id) throws Exception {
        Optional<Fund> optional = fundRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Fund> findByCompany(Company company) throws Exception {
        return fundRepo.findAllByCompanyId(company.getId());
    }

    @Override
    public Fund create(Fund fund) throws Exception {
        return fundRepo.save(fund);
    }

    @Override
    public Fund update(Fund fund) throws Exception {
        return fundRepo.save(fund);
    }

    @Override
    public List<Fund> create(List<Fund> funds) throws Exception {
        return fundRepo.saveAll(funds);
    }
}
