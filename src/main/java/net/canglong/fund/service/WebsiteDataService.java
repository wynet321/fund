package net.canglong.fund.service;

import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Price;

import java.util.List;

public interface WebsiteDataService {
    List<String> getCompanyIds() throws Exception;

    List<Fund> getFunds(String companyId, String companyAbbr) throws Exception;

    List<Price> getPrices(Fund fund, int page) throws Exception;

    Company getCompany(String companyId) throws Exception;

}
