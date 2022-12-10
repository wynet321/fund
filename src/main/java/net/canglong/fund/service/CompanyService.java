package net.canglong.fund.service;

import net.canglong.fund.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.transaction.Transactional;

public interface CompanyService {

    Page<Company> find(Pageable pageable);

    Company find(String id);

    @Transactional
    Company create(Company company);

    @Transactional
    List<Company> create(List<Company> companies);

    @Transactional
    Company update(Company company);

    @Transactional
    List<Company> update(List<Company> companies);

}
