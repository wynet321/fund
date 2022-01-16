package net.soryu.fund.service.impl;

import net.soryu.fund.entity.Company;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.repository.FundRepo;
import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.FundService;
import net.soryu.fund.service.WebsiteDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

@Service
public class FundServiceImpl extends abstractServiceImpl implements FundService {

    @Resource
    private FundRepo fundRepo;
    @Resource
    private WebsiteDataService dataService;
    @Resource
    private CompanyService companyService;

    @Override
    public Page<Fund> get(Pageable pageable) throws Exception {
        return fundRepo.findAll(pageable);
    }

    @Override
    public List<Fund> createByCompanyId(String companyId) throws Exception {
        Company company = companyService.find(companyId);
        return fundRepo.saveAll(dataService.getFunds(companyId, company.getAbbr()));
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
    public Page<Fund> findByCompanyId(String companyId, Pageable pageable) throws Exception {
        return fundRepo.findAllByCompanyId(companyId, pageable);
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
