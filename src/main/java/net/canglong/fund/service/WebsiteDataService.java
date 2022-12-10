package net.canglong.fund.service;

import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Price;

import java.util.List;

public interface WebsiteDataService {
    List<String> getCompanyIds();

    List<Fund> getFunds(String companyId, String companyAbbr);

    Company getCompany(String companyId) throws Exception;

    String getPriceWebPage(Fund fund, int page);

    boolean containsPrice(String html);

    List<Price> getPrices(String html, Fund fund, int page);
}
