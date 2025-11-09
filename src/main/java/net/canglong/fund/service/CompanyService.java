package net.canglong.fund.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import net.canglong.fund.entity.Company;

public interface CompanyService {

  Page<Company> find(@NonNull Pageable pageable);

  Company find(@NonNull String id);

  Company create(@NonNull Company company);

  List<Company> create(@NonNull List<Company> companies);

  Company update(@NonNull Company company);

  void delete(@NonNull String id);

  List<Company> update(@NonNull List<Company> companies);
}
