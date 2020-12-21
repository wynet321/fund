package net.cheetahead.fund.service;

import net.cheetahead.fund.entity.Company;
import net.cheetahead.fund.entity.Fund;
import net.cheetahead.fund.entity.Status;

import java.util.List;

public interface DataService {
    List<Company> fetchCompanies() throws Exception;

    List<Fund> fetchFunds(Company company) throws Exception;

    List<?> fetchCurrencyPrice(String fundId, int page) throws Exception;

    List<?> fetchFundPrice(String id, int page) throws Exception;
     
    Integer generate() throws Exception;
    
    Status getStatus() throws Exception;
    
    List<Company> stop() throws Exception;
}
