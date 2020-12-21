package net.cheetahead.fund.service;

import net.cheetahead.fund.entity.Company;
import net.cheetahead.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.transaction.Transactional;

public interface FundService {
    Page<Fund> get(Pageable pageable) throws Exception;

    @Transactional
    List<Fund> createByCompanyId(String companyId) throws Exception;

    Fund findById(String id) throws Exception;

    List<Fund> findByCompany(Company company) throws Exception;

    @Transactional
    Fund create(Fund fund) throws Exception;

    @Transactional
    Fund update(Fund fund) throws Exception;

    @Transactional
    List<Fund> create(List<Fund> funds) throws Exception;
}
