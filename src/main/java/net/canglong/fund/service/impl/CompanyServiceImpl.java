package net.canglong.fund.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.annotation.Resource;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Price;
import net.canglong.fund.repository.CompanyRepo;
import net.canglong.fund.repository.FundRepo;
import net.canglong.fund.repository.MonthRateRepo;
import net.canglong.fund.repository.PriceRepo;
import net.canglong.fund.repository.YearRateRepo;
import net.canglong.fund.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyServiceImpl implements CompanyService {

  @Resource
  private CompanyRepo companyRepo;
  @Resource
  private FundRepo fundRepo;
  @Resource
  private PriceRepo priceRepo;
  @Resource
  private MonthRateRepo monthRateRepo;
  @Resource
  private YearRateRepo yearRateRepo;

  @Override
  public Page<Company> find(@NonNull Pageable pageable) {
    return companyRepo.findAll(pageable);
  }

  @Override
  public Company create(@NonNull Company company) {
    return companyRepo.save(company);
  }

  @Override
  public List<Company> create(@NonNull List<Company> companies) {
    return companyRepo.saveAll(companies);
  }

  @Override
  public Company update(@NonNull Company company) {
    return companyRepo.save(company);
  }

  @Override
  public List<Company> update(@NonNull List<Company> companies) {
    return companyRepo.saveAll(companies);
  }

  @Override
  public Company find(@NonNull String id) {
    Optional<Company> optional = companyRepo.findById(id);
    return optional.orElse(null);
  }

  @Override
  @Transactional
  public void delete(@NonNull String id) {
    // Find all funds belonging to this company
    Page<Fund> fundPage;
    int page = 0;
    final int pageSize = 100; // Process 100 funds at a time to avoid memory issues
    
    do {
      fundPage = fundRepo.findAllByCompanyId(id, PageRequest.of(page, pageSize));
      List<Fund> funds = fundPage.getContent();
      
      // Delete all related data for each fund in the current page
      for (Fund fund : funds) {
        String fundId = fund.getId();
        
        // Delete prices in batches
        Page<Price> pricePage;
        int pricePageNum = 0;
        do {
          pricePage = priceRepo.findByFundId(fundId, PageRequest.of(pricePageNum, pageSize));
          if (!pricePage.isEmpty()) {
            priceRepo.deleteAll(pricePage.getContent());
          }
          pricePageNum++;
        } while (pricePage.hasNext());
        
        // Delete month rates
        monthRateRepo.deleteByFundId(fundId);
        
        // Delete year rates
        yearRateRepo.deleteByFundId(fundId);
        
        // Delete the fund
        fundRepo.deleteById(Objects.requireNonNull(fundId));
      }
      
      page++;
    } while (fundPage.hasNext());
    
    // Finally, delete the company
    companyRepo.deleteById(id);
  }

}
