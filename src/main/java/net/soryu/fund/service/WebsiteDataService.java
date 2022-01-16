package net.soryu.fund.service;

import net.soryu.fund.entity.Company;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.Price;

import java.util.List;

public interface WebsiteDataService {
    List<String> getCompanyIds() throws Exception;

    List<Fund> getFunds(String companyId, String companyAbbr) throws Exception;

    List<Price> getPrices(Fund fund, int page) throws Exception;

    Company getCompany(String companyId) throws Exception;

}
