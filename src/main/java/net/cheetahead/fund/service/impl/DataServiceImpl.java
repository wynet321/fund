package net.cheetahead.fund.service.impl;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.cheetahead.fund.entity.Company;
import net.cheetahead.fund.entity.CurrencyPrice;
import net.cheetahead.fund.entity.Fund;
import net.cheetahead.fund.entity.FundPrice;
import net.cheetahead.fund.entity.PriceIdentity;
import net.cheetahead.fund.entity.Status;
import net.cheetahead.fund.service.CompanyService;
import net.cheetahead.fund.service.DataService;
import net.cheetahead.fund.service.FundService;
import net.cheetahead.fund.service.PriceService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    private CompanyService companyService;

    @Resource
    private FundService fundService;

    @Resource
    private PriceService priceService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);;

    private long start;

    @Override
    public List<Company> fetchCompanies() throws Exception {
        Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
        JsonNode pagedCompanies = Unirest.get("/fund/disclose/fund_compay_affiche.do").queryString("type", "4040-1010").asJson().getBody();
        JSONArray sourceCompanies = pagedCompanies.getObject().getJSONArray("aaData");
        logger.info("Completed to fetch company list.");
        List<Company> targetCompanies = new LinkedList<Company>();
        for (Object company : sourceCompanies) {
            Company targetCompany = new Company();
            targetCompany.setId(((JSONObject) company).getString("code"));
            targetCompany.setName(((JSONObject) company).getString("name"));
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("type", "4040-1010");
            parameters.put("code", targetCompany.getId());
            JsonNode companyDetail = Unirest.get("/fund/disclose/fund_compay_detail.do").queryString(parameters).asJson()
                    .getBody();
            targetCompany.setAbbr(companyDetail.getObject().getJSONObject("fundCompany").get("shortName").toString());
            targetCompany.setCreatedOn((new SimpleDateFormat("yyyy-MM-dd")).parse(companyDetail.getObject().getJSONObject("fundCompany").get("foundDate")
                    .toString()));
            targetCompany.setAddress(companyDetail.getObject().getJSONObject("fundCompany").get("officeArea").toString());
            targetCompanies.add(targetCompany);
        }
        logger.info("Completed to fetch companies' info.");
        return targetCompanies;
    }

    @Override
    public List<Fund> fetchFunds(Company company) throws Exception {
        Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
        int start = 0;
        int totalCount = 1;
        List<Fund> funds = new LinkedList<Fund>();
        while (start < totalCount) {
            JsonNode node = Unirest.get(
                    "/fund/disclose/advanced_search_fund.do?aoData=%5B%7B%22name%22%3A%22sEcho%22%2C%22value%22%3A2%7D%2C%7B%22name%22%3A%22iColumns%22%2C%22value%22%3A4%7D%2C%7B%22name%22%3A%22sColumns%22%2C%22value%22%3A%22%2C%2C%2C%22%7D%2C%7B%22name%22%3A%22iDisplayStart%22%2C%22value%22%3A"
                            + start
                            + "%7D%2C%7B%22name%22%3A%22iDisplayLength%22%2C%22value%22%3A20%7D%2C%7B%22name%22%3A%22mDataProp_0%22%2C%22value%22%3A%22code%22%7D%2C%7B%22name%22%3A%22mDataProp_1%22%2C%22value%22%3A%22shortName%22%7D%2C%7B%22name%22%3A%22mDataProp_2%22%2C%22value%22%3A%22fundCompany%22%7D%2C%7B%22name%22%3A%22mDataProp_3%22%2C%22value%22%3A%22fundType%22%7D%2C%7B%22name%22%3A%22fundType%22%2C%22value%22%3A%22%22%7D%2C%7B%22name%22%3A%22fundCompanyShortName%22%2C%22value%22%3A%22"
                            + company.getAbbr()
                            + "%22%7D%2C%7B%22name%22%3A%22fundCode%22%2C%22value%22%3A%22%22%7D%2C%7B%22name%22%3A%22fundShortName%22%2C%22value%22%3A%22%22%7D%5D")
                    .asJson().getBody();
            JSONArray sourceFunds = node.getObject().getJSONArray("aaData");
            for (Object sourceFund : sourceFunds) {
                Fund fund = fundService.findById(((JSONObject) sourceFund).getString("code"));
                if (fund == null) {
                    fund = new Fund();
                } else {
                    continue;
                }
                fund.setId(((JSONObject) sourceFund).getString("code"));
                fund.setName(((JSONObject) sourceFund).getString("shortName"));
                fund.setType(((JSONObject) sourceFund).getString("fundType"));
                fund.setCompanyId(company.getId());
                funds.add(fund);
            }
            totalCount = node.getObject().getInt("iTotalRecords");
            start += 20;
        }
        return funds;
    }

    @Override
    public List<?> fetchCurrencyPrice(String fundId, int page) throws Exception {
        Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
        String html = Unirest.get("/fund/web/list_net.daily_report?1=1&fundCode=" + fundId + "&limit=20&start=" + 20 * page).asString().getBody();
        Document doc = Jsoup.parse(html);
        Pattern pattern = Pattern.compile("[\\d]*\\.[\\d]{1,4}");
        List<CurrencyPrice> prices = new LinkedList<CurrencyPrice>();
        Elements header = doc.getElementsByAttributeValueMatching("class", "cc");
        if (header.isEmpty()) {
            return prices;
        }
        int[] index = new int[6];
        Elements headerTds = header.get(0).getElementsByTag("td");
        for (int i = 0; i < headerTds.size(); i++) {
            switch (headerTds.get(i).text()) {
                case "基金代码":
                    index[0] = i;
                    break;
                case "分级代码":
                    index[1] = i;
                    break;
                case "基金简称":
                    index[2] = i;
                    break;
                case "每万份基金已实现收益":
                    index[3] = i;
                    break;
                case "7日年化收益率":
                    index[4] = i;
                    break;
                case "估值日期":
                    index[5] = i;
                    break;
            }
        }
        Elements elements = doc.getElementsByAttributeValueMatching("class", "dd|aa");
        if (elements.isEmpty()) {
            return prices;
        }
        for (Element element : elements) {
            Elements elementTds = element.getElementsByTag("td");
            // price column is empty to parent-child fund
            if (elementTds.get(index[3]).text().isEmpty()) {
                continue;
            }
            CurrencyPrice price = new CurrencyPrice();
            if (elementTds.get(index[1]).text().equals("-")) {
                price.setPriceIdentity(new PriceIdentity(elementTds.get(index[0]).text(), Date.valueOf(elementTds.get(index[5]).text())));
            } else {
                if (page == 0) {
                    updateFund(elementTds, index);
                }
                price.setPriceIdentity(new PriceIdentity(elementTds.get(index[1]).text(), Date.valueOf(elementTds.get(index[5]).text())));
            }
            Matcher matcher = pattern.matcher(elementTds.get(index[3]).text());
            price.setReturnOfTenKilo(matcher.find() ? new BigDecimal(matcher.group()) : BigDecimal.ZERO);
            matcher = pattern.matcher(elementTds.get(index[4]).text().trim().replace("%", ""));
            price.setSevenDayAnnualizedRateOfReturn(matcher.find() ? new BigDecimal(matcher.group()) : BigDecimal.ZERO);
            prices.add(price);
        }
        return prices;
    }

    @Override
    public List<?> fetchFundPrice(String fundId, int page) throws Exception {
        Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
        String html = Unirest.get("/fund/web/list_net.daily_report?1=1&fundCode=" + fundId + "&limit=20&start=" + 20 * page).asString().getBody();
        Document doc = Jsoup.parse(html);
        Pattern pattern = Pattern.compile("[\\d]*\\.[\\d]{1,4}");

        List<FundPrice> prices = new LinkedList<FundPrice>();
        Elements header = doc.getElementsByAttributeValueMatching("class", "cc");
        if (header.isEmpty()) {
            return prices;
        }
        int[] index = new int[6];
        Elements headerTds = header.get(0).getElementsByTag("td");
        for (int i = 0; i < headerTds.size(); i++) {
            switch (headerTds.get(i).text()) {
                case "基金代码":
                    index[0] = i;
                    break;
                case "分级代码":
                    index[1] = i;
                    break;
                case "基金简称":
                    index[2] = i;
                    break;
                case "份额净值":
                    index[3] = i;
                    break;
                case "累计净值":
                    index[4] = i;
                    break;
                case "估值日期":
                    index[5] = i;
                    break;
            }
        }
        Elements elements = doc.getElementsByAttributeValueMatching("class", "dd|aa");
        if (elements.isEmpty()) {
            return prices;
        }
        for (Element element : elements) {
            Elements elementTds = element.getElementsByTag("td");
            // price column is empty to parent-child fund
            if (elementTds.get(index[4]).text().isEmpty()) {
                continue;
            }
            FundPrice price = new FundPrice();
            // no child
            if (elementTds.get(index[1]).text().equals("-")) {
                price.setPriceIdentity(new PriceIdentity(elementTds.get(index[0]).text(), Date.valueOf(elementTds.get(index[5]).text())));
            } else {
                if (page == 0) {
                    updateFund(elementTds, index);
                }
                price.setPriceIdentity(new PriceIdentity(elementTds.get(index[1]).text(), Date.valueOf(elementTds.get(index[5]).text())));
            }
            Matcher matcherAccumulatedPrice = pattern.matcher(elementTds.get(index[4]).text());
            Matcher matcherPrice = pattern.matcher(elementTds.get(index[3]).text());
            price.setPrice(matcherPrice.find() ? new BigDecimal(matcherPrice.group()) : BigDecimal.ZERO);
            price.setAccumulatedPrice(matcherAccumulatedPrice.find() ? new BigDecimal(matcherAccumulatedPrice.group()) : price.getPrice());
            prices.add(price);
        }
        return prices;
    }

    private void updateFund(Elements elementTds, int[] index) throws Exception {
        // update fund
        Fund parentFund = fundService.findById(elementTds.get(index[0]).text());
        Fund originalFund = fundService.findById(elementTds.get(index[1]).text());
        if (originalFund == null) {
            // new fund
            Fund newFund = new Fund();
            newFund.setId(elementTds.get(index[1]).text());
            newFund.setName(elementTds.get(index[2]).text());
            newFund.setCompanyId(parentFund.getCompanyId());
            newFund.setType(parentFund.getType());
            newFund.setParentId(elementTds.get(index[0]).text());
            fundService.create(newFund);
        } else {
            originalFund.setName(elementTds.get(index[2]).text());
            fundService.update(originalFund);
        }
    }

    @Override
    public Integer generate() throws Exception {
        List<Company> companies = companyService.createAll();
        logger.info("Totally " + companies.size() + " companies.");
        start = System.currentTimeMillis();
        for (Company company : companies) {
            executor.execute(new Task(company));
        }
        return companies.size();
    }

    @Override
    public Status getStatus() throws Exception {
        Status status = new Status();
        status.setQueueSize(executor.getQueue().size());
        status.setAliveThreadCount(executor.getActiveCount());
        status.setElapseTime((System.currentTimeMillis() - start) / 1000);
        status.setStopped(executor.isTerminated());
        return status;
    }

    @Override
    public List<Company> stop() throws Exception {
        List<Runnable> tasks = executor.shutdownNow();
        List<Company> companies = new LinkedList<Company>();
        for (Runnable task : tasks) {
            companies.add(((Task) task).company);
        }
        logger.info("executor is terminating...");
        return companies;
    }

    class Task implements Runnable {

        private Company company;

        public Task(Company company) {
            this.company = company;
        }

        @Override
        public void run() {
            try {
                List<Fund> funds = fetchFunds(company);
                fundService.create(funds);
                funds = fundService.findByCompany(company);
                logger.info(company.getName() + " - " + funds.size() + " funds.");
                for (Fund fund : funds) {
                    logger.info("start to collect " + fund.getId());
                    int count = priceService.create(fund.getId());
                    logger.info(fund.getName() + " total " + count + " records.");
                }
                logger.info(company.getName() + " finished.");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
