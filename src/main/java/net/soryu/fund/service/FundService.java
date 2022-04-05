package net.soryu.fund.service;

import net.soryu.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.transaction.Transactional;

public interface FundService {
    Page<Fund> get(Pageable pageable) throws Exception;

    Fund findById(String id) throws Exception;

    Page<Fund> findByCompanyId(String companyId, Pageable pageable) throws Exception;

    @Transactional
    Fund create(Fund fund) throws Exception;

    @Transactional
    Fund update(Fund fund) throws Exception;

    @Transactional
    List<Fund> create(List<Fund> funds) throws Exception;

    List<Fund> findAll() throws Exception;

    Fund findByName(String name) throws Exception;
}
