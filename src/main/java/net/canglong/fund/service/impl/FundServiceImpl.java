package net.canglong.fund.service.impl;

import net.canglong.fund.entity.Fund;
import net.canglong.fund.repository.FundRepo;
import net.canglong.fund.service.FundService;
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

    @Override
    public Page<Fund> get(Pageable pageable) throws Exception {
        return fundRepo.findAll(pageable);
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

    public List<Fund> findAll() throws Exception {
        return fundRepo.findAll();
    }

    public Fund findByName(String name) throws Exception {
        return fundRepo.findByName(name);
    }
}
