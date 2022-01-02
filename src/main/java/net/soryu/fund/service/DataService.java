package net.soryu.fund.service;

import net.soryu.fund.entity.Company;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.Status;

import java.util.List;

public interface DataService {
    List<Company> getCompanyListFromWebsite() throws Exception;

    List<Fund> getFundsFromWebSite(Company company) throws Exception;

    List<?> getFundPriceFromWebsite(Fund fund, int page) throws Exception;

    Status generateFundPriceRetrievalJob() throws Exception;

    Status getStatus() throws Exception;

    List<Company> stop() throws Exception;
}
