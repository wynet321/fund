package net.canglong.fund.service;

import java.util.List;
import jakarta.transaction.Transactional;
import net.canglong.fund.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
