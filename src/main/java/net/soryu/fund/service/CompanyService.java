package net.soryu.fund.service;

import net.soryu.fund.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.transaction.Transactional;

public interface CompanyService {

    Page<Company> find(Pageable pageable) throws Exception;

    Company find(String id) throws Exception;

    @Transactional
    Company create(Company company) throws Exception;

    @Transactional
    List<Company> create(List<Company> companies) throws Exception;

    @Transactional
    Company update(Company company) throws Exception;

    @Transactional
    List<Company> update(List<Company> companies) throws Exception;

}
