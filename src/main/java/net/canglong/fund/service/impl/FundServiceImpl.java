package net.canglong.fund.service.impl;

import net.canglong.fund.entity.Fund;
import net.canglong.fund.repository.FundRepo;
import net.canglong.fund.service.FundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

@Service
public class FundServiceImpl implements FundService {

    @Resource
    private FundRepo fundRepo;

    @Override
    public Page<Fund> get(Pageable pageable) {
        return fundRepo.findAll(pageable);
    }

    @Override
    public Fund findById(String id) {
        Optional<Fund> optional = fundRepo.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Page<Fund> findByCompanyId(String companyId, Pageable pageable) {
        return fundRepo.findAllByCompanyId(companyId, pageable);
    }

    @Override
    public Fund create(Fund fund) {
        return fundRepo.save(fund);
    }

    @Override
    public Fund update(Fund fund) {
        return fundRepo.save(fund);
    }

    @Override
    public List<Fund> create(List<Fund> funds) {
        return fundRepo.saveAll(funds);
    }

    public List<Fund> findAll() {
        return fundRepo.findAll();
    }

    public Fund findByName(String name) {
        return fundRepo.findByName(name);
    }

    @Override
    public List<Fund> findAllByType(String type) {
        return fundRepo.findAllByType(type);
    }

    @Override
    public List<Fund> findAllExcludesType(String type) {
        return fundRepo.findAllExcludesType(type);
    }

    @Override
    public List<String> findAllTypes() {
        return fundRepo.findAllTypes();
    }

}
