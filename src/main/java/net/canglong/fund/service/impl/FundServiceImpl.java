package net.canglong.fund.service.impl;

import java.util.List;
import java.util.Optional;
import jakarta.annotation.Resource;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.repository.FundRepo;
import net.canglong.fund.service.FundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FundServiceImpl implements FundService {

  @Resource
  private FundRepo fundRepo;

  @Override
  public Page<Fund> get(@NonNull Pageable pageable) {
    return fundRepo.findAll(pageable);
  }

  @Override
  public Optional<Fund> findById(@NonNull String id) {
    return fundRepo.findById(id);
  }

  @Override
  public Page<Fund> findByCompanyId(@NonNull String companyId, @NonNull Pageable pageable) {
    return fundRepo.findAllByCompanyId(companyId, pageable);
  }

  @Override
  public Fund create(@NonNull Fund fund) {
    return fundRepo.save(fund);
  }

  @Override
  public Fund update(@NonNull Fund fund) {
    return fundRepo.save(fund);
  }

  @Override
  public List<Fund> create(@NonNull List<Fund> funds) {
    return fundRepo.saveAll(funds);
  }

  public List<Fund> findAll() {
    return fundRepo.findAll();
  }

  public Fund findByName(@NonNull String name) {
    return fundRepo.findByName(name);
  }

  @Override
  public List<Fund> findAllByType(@NonNull String type) {
    return fundRepo.findAllByType(type);
  }

  @Override
  public List<Fund> findAllExcludesType(@NonNull String type) {
    return fundRepo.findAllExcludesType(type);
  }

  @Override
  public List<Fund> findAllByTypes(@NonNull List<String> types) {
    return fundRepo.findAllByTypes(types);
  }

  @Override
  public List<String> findAllTypes() {
    return fundRepo.findAllTypes();
  }

  @Override
  public List<Fund> searchByNameContaining(@NonNull String keyword, int limit) {
    List<Fund> funds = fundRepo.findByNameContaining(keyword);
    return funds.size() > limit ? funds.subList(0, limit) : funds;
  }

  @Override
  public List<Fund> searchByNameOrIdContaining(@NonNull String keyword, int limit) {
    List<Fund> funds = fundRepo.findByNameOrIdContaining(keyword);
    return funds.size() > limit ? funds.subList(0, limit) : funds;
  }

}
