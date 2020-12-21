package net.cheetahead.fund.service;

import net.cheetahead.fund.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.transaction.Transactional;

public interface CompanyService {

    Page<Company> findAll(Pageable pageable) throws Exception;

    Company findById(String id) throws Exception;

    @Transactional
    Company create(Company company) throws Exception;

    @Transactional
    List<Company> createAll() throws Exception;

    @Transactional
    Company update(Company company) throws Exception;

    @Transactional
    List<Company> updateAll() throws Exception;

}
