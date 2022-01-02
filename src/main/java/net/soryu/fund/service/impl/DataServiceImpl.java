package net.soryu.fund.service.impl;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.soryu.fund.entity.Company;
import net.soryu.fund.entity.CurrencyFundPrice;
import net.soryu.fund.entity.Fund;
import net.soryu.fund.entity.FundPrice;
import net.soryu.fund.entity.NonCurrencyFundPrice;
import net.soryu.fund.entity.PriceIdentity;
import net.soryu.fund.entity.Status;
import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.DataService;
import net.soryu.fund.service.FundService;
import net.soryu.fund.service.PriceService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

    @Value("${server.thread-count}")
    private int threadCount;

    private ThreadPoolTaskExecutor executor;

    private long startTime;

    public DataServiceImpl() {
        Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
    }

    @Override
    public List<Company> getCompanyListFromWebsite() throws Exception {
        logger.info("Start to get company list...");
        JsonNode pagedCompanies = Unirest.get("/fund/disclose/fund_compay_affiche.do").queryString("type", "4040-1010").asJson().getBody();
        JSONArray sourceCompanies = pagedCompanies.getObject().getJSONArray("aaData");
        List<Company> targetCompanies = new LinkedList<Company>();
        for (Object company : sourceCompanies) {
            Company targetCompany = new Company();
            targetCompany.setId(((JSONObject) company).getString("code"));
            targetCompany.setName(((JSONObject) company).getString("name"));
            // Map<String, Object> parameters = new HashMap<String, Object>();
            // parameters.put("type", "4040-1010");
            // parameters.put("code", targetCompany.getId());
            // logger.debug("Start to get company detail for " + targetCompany.getAbbr());
            // JsonNode companyDetail =
            // Unirest.get("/fund/disclose/fund_compay_detail.do").queryString(parameters).asJson()
            // .getBody();
            // targetCompany.setAbbr(companyDetail.getObject().getJSONObject("fundCompany").get("shortName").toString());
            // targetCompany.setCreatedOn((new
            // SimpleDateFormat("yyyy-MM-dd")).parse(companyDetail.getObject().getJSONObject("fundCompany").get("foundDate")
            // .toString()));
            // targetCompany.setAddress(companyDetail.getObject().getJSONObject("fundCompany").get("officeArea").toString());
            targetCompanies.add(targetCompany);
            // logger.debug("Completed getting company detail for " + targetCompany.getAbbr());
        }
        logger.info("Completed getting company list.");
        return targetCompanies;
    }

    @Override
    public List<Fund> getFundsFromWebSite(Company company) throws Exception {
        int index = 0;
        int totalCount = 1;
        List<Fund> funds = new LinkedList<Fund>();
        logger.info("Start to get funds for " + company.getAbbr());
        while (index < totalCount) {
            JsonNode node = Unirest.get(
                    "/fund/disclose/advanced_search_fund.do?aoData=%5B%7B%22name%22%3A%22sEcho%22%2C%22value%22%3A2%7D%2C%7B%22name%22%3A%22iColumns%22%2C%22value%22%3A4%7D%2C%7B%22name%22%3A%22sColumns%22%2C%22value%22%3A%22%2C%2C%2C%22%7D%2C%7B%22name%22%3A%22iDisplayStart%22%2C%22value%22%3A"
                            + index
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
            index += 20;
        }
        logger.info("Completed getting " + totalCount + " funds for " + company.getAbbr());
        return funds;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getFundPriceFromWebsite(Fund fund, int page) throws Exception {
        boolean isCurrencyFund = fund.getType().equals("货币型");
        String html = Unirest.get("/fund/web/list_net.daily_report?1=1&fundCode=" + fund.getId() + "&limit=20&start=" + 20 * page).asString().getBody();
        Document doc = Jsoup.parse(html);
        Pattern pattern = Pattern.compile("[\\d]*\\.[\\d]{1,4}");
        List<? extends FundPrice> prices;
        if (isCurrencyFund) {
            prices = new LinkedList<CurrencyFundPrice>();
        } else {
            prices = new LinkedList<NonCurrencyFundPrice>();
        }
        Elements header = doc.getElementsByAttributeValueMatching("class", "cc");
        if (header.isEmpty()) {
            return prices;
        }
        int[] index = new int[6];
        Elements headerTds = header.get(0).getElementsByTag("td");
        if (isCurrencyFund) {
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
        } else {
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
        }
        Elements elements = doc.getElementsByAttributeValueMatching("class", "dd|aa");
        if (elements.isEmpty()) {
            return prices;
        }
        if (isCurrencyFund) {
            for (Element element : elements) {
                Elements elementTds = element.getElementsByTag("td");
                // price column is empty to parent-child fund
                if (elementTds.get(index[3]).text().isEmpty()) {
                    continue;
                }
                CurrencyFundPrice price = new CurrencyFundPrice();
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
                ((LinkedList<CurrencyFundPrice>) prices).add(price);
            }
            logger.info("Got page " + page + " price for fund " + fund.getName());
            return prices;
        } else {
            for (Element element : elements) {
                Elements elementTds = element.getElementsByTag("td");
                // price column is empty to parent-child fund
                if (elementTds.get(index[4]).text().isEmpty()) {
                    continue;
                }
                NonCurrencyFundPrice price = new NonCurrencyFundPrice();
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
                ((LinkedList<NonCurrencyFundPrice>) prices).add(price);
            }

            logger.info("Completed getting page " + page + " price for fund " + fund.getName());
            return prices;
        }
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
    public Status generateFundPriceRetrievalJob() throws Exception {
        logger.info("Website retrieval thread count is " + threadCount);
        if (executor == null) {
            executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(threadCount);
            executor.setThreadNamePrefix("Website retrieval thread pool");
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.initialize();
        }
        if (executor.getThreadPoolExecutor().getQueue().isEmpty()) {
            List<Company> companies = getCompanyListFromWebsite();
            logger.info("Totally found " + companies.size() + " companies.");
            startTime = System.currentTimeMillis();
            for (Company company : companies) {
                executor.execute(new Task(company));
            }
            executor.shutdown();
        }
        return getStatus();
    }

    @Override
    public Status getStatus() throws Exception {
        Status status = new Status();
        status.setLeftCount(executor.getThreadPoolExecutor().getQueue().size());
        status.setAliveThreadCount(executor.getActiveCount());
        status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
        status.setStopped(executor.getThreadPoolExecutor().isTerminated());
        return status;
    }

    @Override
    public List<Company> stop() throws Exception {
        executor.shutdown();
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        executor.getThreadPoolExecutor().getQueue().drainTo(queue);
        List<Company> companies = new LinkedList<Company>();
        for (Runnable task : queue) {
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
                companyService.create(getCompanyDetailsFromWebsite(company));
                List<Fund> fundsFromWebSite = getFundsFromWebSite(company);
                fundService.create(fundsFromWebSite);
                logger.info(company.getName() + " - totally found " + fundsFromWebSite.size() + " funds.");
                int totalPages = 1;
                List<Fund> fundList = new LinkedList<Fund>();
                for (int i = 0; i < totalPages; i++) {
                    Page<Fund> funds = fundService.findByCompanyId(company.getId(), PageRequest.of(i, 100));
                    fundList.addAll(funds.toList());
                    totalPages = funds.getTotalPages();
                    logger.debug(company.getName() + " added " + funds.getNumberOfElements() + " funds.");
                }
                for (Fund fund : fundsFromWebSite) {
                    logger.info("start to collect " + fund.getId());
                    int count = priceService.create(fund.getId());
                    logger.info(fund.getName() + " total " + count + " records.");
                }
                logger.info(company.getName() + " retrieval finished.");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        private Company getCompanyDetailsFromWebsite(Company company) throws Exception {
            Company targetCompany = new Company();
            targetCompany.setId(company.getId());
            targetCompany.setName(company.getName());
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("type", "4040-1010");
            parameters.put("code", targetCompany.getId());
            logger.debug("Start to get company detail for " + targetCompany.getName());
            JsonNode companyDetail = Unirest.get("/fund/disclose/fund_compay_detail.do").queryString(parameters).asJson()
                    .getBody();
            targetCompany.setAbbr(companyDetail.getObject().getJSONObject("fundCompany").get("shortName").toString());
            targetCompany.setCreatedOn((new SimpleDateFormat("yyyy-MM-dd")).parse(companyDetail.getObject().getJSONObject("fundCompany").get("foundDate")
                    .toString()));
            targetCompany.setAddress(companyDetail.getObject().getJSONObject("fundCompany").get("officeArea").toString());
            logger.debug("Completed getting company detail for " + targetCompany.getName());
            return targetCompany;
        }

    }
}
