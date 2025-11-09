package net.canglong.fund.service;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import net.canglong.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface FundService {

  Page<Fund> get(@NonNull Pageable pageable);

  Optional<Fund> findById(@NonNull String id);

  Page<Fund> findByCompanyId(@NonNull String companyId, @NonNull Pageable pageable);

  @Transactional
  Fund create(@NonNull Fund fund);

  @Transactional
  Fund update(@NonNull Fund fund);

  @Transactional
  List<Fund> create(@NonNull List<Fund> funds);

  List<Fund> findAll();

  Fund findByName(@NonNull String name);

  List<Fund> findAllByType(@NonNull String type);

  List<Fund> findAllExcludesType(@NonNull String type);

  List<Fund> findAllByTypes(@NonNull List<String> types);

  List<String> findAllTypes();

  List<Fund> searchByNameContaining(@NonNull String keyword, int limit);

  List<Fund> searchByNameOrIdContaining(@NonNull String keyword, int limit);

}
