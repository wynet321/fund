package net.canglong.fund.service;

import java.util.List;
import javax.transaction.Transactional;
import net.canglong.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundService {

  Page<Fund> get(Pageable pageable);

  Fund findById(String id);

  Page<Fund> findByCompanyId(String companyId, Pageable pageable);

  @Transactional
  Fund create(Fund fund);

  @Transactional
  Fund update(Fund fund);

  @Transactional
  List<Fund> create(List<Fund> funds);

  List<Fund> findAll();

  Fund findByName(String name);

  List<Fund> findAllByType(String type);

  List<Fund> findAllExcludesType(String type);

  List<Fund> findAllByTypes(List<String> types);

  List<String> findAllTypes();

}
