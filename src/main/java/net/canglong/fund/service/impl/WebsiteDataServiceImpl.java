package net.canglong.fund.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.PriceIdentity;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.WebsiteDataService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WebsiteDataServiceImpl implements WebsiteDataService {

  @Resource
  private FundService fundService;

  public WebsiteDataServiceImpl() {
    Unirest.config().defaultBaseUrl("http://eid.csrc.gov.cn/");
    Unirest.config().concurrency(200, 50);
  }

  @Override
  public List<String> getCompanyIds() {
    log.debug("Start to get company id list...");
    JsonNode pagedCompanies = Unirest.get("/fund/disclose/fund_compay_affiche.do")
        .queryString("type", "4040-1010").asJson().getBody();
    JSONArray sourceCompanies = pagedCompanies.getObject().getJSONArray("aaData");
    List<String> companyIds = new LinkedList<>();
    for (Object company : sourceCompanies) {
      companyIds.add(((JSONObject) company).getString("code"));
    }
    log.debug("Completed getting company id list.");
    return companyIds;
  }

  @Override
  public Company getCompany(String companyId) throws Exception {
    Company targetCompany = new Company();
    targetCompany.setId(companyId);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("type", "4040-1010");
    parameters.put("code", targetCompany.getId());
    log.debug("Start to get company detail for {}", targetCompany.getName());
    JsonNode companyDetail = Unirest.get("/fund/disclose/fund_compay_detail.do")
        .queryString(parameters).asJson()
        .getBody();
    targetCompany.setName(
        companyDetail.getObject().getJSONObject("fundCompany").get("cname").toString());
    targetCompany.setAbbr(
        companyDetail.getObject().getJSONObject("fundCompany").get("shortName").toString());
    targetCompany.setCreatedOn((new SimpleDateFormat("yyyy-MM-dd")).parse(
        companyDetail.getObject().getJSONObject("fundCompany").get("foundDate").toString().isEmpty()
            ? "1980-01-01"
            : companyDetail.getObject().getJSONObject("fundCompany").get("foundDate").toString()));
    targetCompany.setAddress(
        companyDetail.getObject().getJSONObject("fundCompany").get("officeArea").toString());
    log.debug("Completed getting company detail for {}", targetCompany.getName());
    return targetCompany;
  }

  @Override
  public List<Fund> getFunds(String companyId, String companyAbbr) {
    int index = 0;
    int totalCount = 1;
    List<Fund> funds = new LinkedList<>();
    log.debug("Start to get fund list for {}", companyAbbr);
    while (index < totalCount) {
      JsonNode node = Unirest.get(
              "/fund/disclose/advanced_search_fund.do?aoData=%5B%7B%22name%22%3A%22sEcho%22%2C%22value%22%3A1%7D%2C%7B%22name%22%3A%22iColumns%22%2C%22value%22%3A4%7D"
                  + "%2C%7B%22name%22%3A%22sColumns%22%2C%22value%22%3A%22%2C%2C%2C%22%7D%2C%7B%22name%22%3A%22iDisplayStart%22%2C%22value%22%3A"
                  + index
                  + "%7D%2C%7B%22name%22%3A%22iDisplayLength%22%2C%22value%22%3A20%7D%2C%7B%22name%22%3A%22mDataProp_0%22%2C%22value%22%3A%22code%22%7D%2C%7B%22name%22%3A"
                  + "%22mDataProp_1%22%2C%22value%22%3A%22shortName%22%7D%2C%7B%22name%22%3A%22mDataProp_2%22%2C%22value%22%3A%22fundCompany%22%7D%2C%7B%22name%22"
                  + "%3A%22mDataProp_3%22%2C%22value%22%3A%22fundType%22%7D%2C%7B%22name%22%3A%22fundType%22%2C%22value%22%3A%22%22%7D%2C%7B%22name%22"
                  + "%3A%22fundCompanyShortName%22%2C%22value%22%3A%22"
                  + companyAbbr
                  + "%22%7D%2C%7B%22name%22%3A%22fundCode%22%2C%22value%22%3A%22%22%7D%2C%7B%22name%22%3A"
                  + "%22fundShortName%22%2C%22value%22%3A%22%22%7D%5D")
          .asJson().getBody();
      JSONArray sourceFunds = node.getObject().getJSONArray("aaData");
      for (Object sourceFund : sourceFunds) {
        Fund fund = new Fund();
        fund.setId(((JSONObject) sourceFund).getString("code"));
        fund.setName(((JSONObject) sourceFund).getString("shortName"));
        fund.setType(((JSONObject) sourceFund).getString("fundType"));
        fund.setCompanyId(companyId);
        funds.add(fund);
      }
      totalCount = node.getObject().getInt("iTotalRecords");
      index += 20;
    }
    log.info("Completed getting {} funds for Company {}", totalCount, companyAbbr);
    return funds;
  }

  public String getPriceWebPage(Fund fund, int page) throws Exception {
    try {
      return Unirest.get(
          "/fund/web/list_net.daily_report?1=1&fundCode=" + fund.getId() + "&limit=20&start="
              + 20 * page).asString().getBody();
    } catch (Exception e) {
      try {
        Thread.sleep(600000);
        return Unirest.get(
            "/fund/web/list_net.daily_report?1=1&fundCode=" + fund.getId() + "&limit=20&start="
                + 20 * page).asString().getBody();
      } catch (Exception ee) {
        throw new Exception("Retry fetch website failed!");
      }

    }
  }

  public boolean containsPrice(String html) {
    if (html == null) {
      return false;
    }
    return html.contains("class=\"dd\"");
  }

  @Override
  public List<Price> getPrices(String html, Fund fund, int page) {
    boolean isCurrencyFund = fund.getType().equals("货币型");
    Document doc = Jsoup.parse(html);
    LinkedList<Price> prices = new LinkedList<>();
    Elements header = doc.getElementsByAttributeValueMatching("class", "cc");
    if (header.isEmpty()) {
      log.info("No price on {} by empty header", fund.getId());
      return prices;
    }
    Elements bodyElements = doc.getElementsByAttributeValueMatching("class", "dd|aa");
    if (bodyElements.isEmpty()) {
      log.info("No price on {} by empty body", fund.getId());
      return prices;
    }

    if (isCurrencyFund) {
      for (Element element : bodyElements) {
        Elements rowElements = element.getElementsByTag("td");
        // price column is empty on parent-child fund
        if (rowElements.get(3).text().isEmpty()) {
          continue;
        }
        prices.add(getCurrencyFundPrice(rowElements, page));
      }
    } else {
      // determine column index by analyzing header only on non currency fund
      Elements headerElements = header.get(0).getElementsByTag("td");
      int[] nonCurrencyFundFieldIndex = getFieldIndex(headerElements);
      for (Element element : bodyElements) {
        Elements rowElements = element.getElementsByTag("td");
        // price column is empty on parent-child fund
        if (rowElements.get(nonCurrencyFundFieldIndex[4]).text().isEmpty()) {
          continue;
        }
        prices.add(getNonCurrencyFundPrice(rowElements, nonCurrencyFundFieldIndex, page));
      }
    }
    log.debug("Retrieved page {} price for fund {}", page, fund.getName());
    return prices;
  }

  private int[] getFieldIndex(Elements elements) {
    int[] fieldIndex = new int[6];
    for (int i = 0; i < elements.size(); i++) {
      switch (elements.get(i).text()) {
        case "基金代码":
          fieldIndex[0] = i;
          break;
        case "分级代码":
          fieldIndex[1] = i;
          break;
        case "基金简称":
          fieldIndex[2] = i;
          break;
        case "份额净值":
          fieldIndex[3] = i;
          break;
        case "累计净值":
          fieldIndex[4] = i;
          break;
        case "估值日期":
          fieldIndex[5] = i;
          break;
      }
    }
    return fieldIndex;
  }

  private Price getCurrencyFundPrice(Elements elements, int page) {
    Pattern pattern = Pattern.compile("\\d*\\.\\d{1,4}");
    Price price = new Price();
    if (elements.get(1).text().equals("-")) {
      // No parent fund
      price.setPriceIdentity(
          new PriceIdentity(elements.get(0).text(), LocalDate.parse(elements.get(8).text())));
    } else {
      if (page == 0) {
        addParentFund(elements);
      }
      price.setPriceIdentity(
          new PriceIdentity(elements.get(1).text(), LocalDate.parse(elements.get(8).text())));
    }
    Matcher matcher = pattern.matcher(elements.get(3).text());
    price.setReturnOfTenKilo(matcher.find() ? new BigDecimal(matcher.group()) : BigDecimal.ZERO);
    matcher = pattern.matcher(elements.get(4).text().trim().replace("%", ""));
    price.setSevenDayAnnualizedRateOfReturn(
        matcher.find() ? new BigDecimal(matcher.group()) : BigDecimal.ZERO);
    return price;
  }

  private Price getNonCurrencyFundPrice(Elements elements, int[] fieldIndex, int page) {
    Pattern pattern = Pattern.compile("\\d*\\.\\d{1,4}");
    Price price = new Price();
    if (elements.get(fieldIndex[1]).text().equals("-")) {
      price.setPriceIdentity(new PriceIdentity(elements.get(fieldIndex[0]).text(),
          LocalDate.parse(elements.get(fieldIndex[5]).text())));
    } else {
      if (page == 0) {
        addParentFund(elements);
      }
      price.setPriceIdentity(new PriceIdentity(elements.get(fieldIndex[1]).text(),
          LocalDate.parse(elements.get(fieldIndex[5]).text())));
    }
    Matcher matcherAccumulatedPrice = pattern.matcher(elements.get(fieldIndex[4]).text());
    Matcher matcherPrice = pattern.matcher(elements.get(fieldIndex[3]).text());
    price.setPrice(matcherPrice.find() ? new BigDecimal(matcherPrice.group()) : BigDecimal.ZERO);
    price.setAccumulatedPrice(
        matcherAccumulatedPrice.find() ? new BigDecimal(matcherAccumulatedPrice.group())
            : price.getPrice());
    return price;
  }

  private void addParentFund(Elements elementTds) {
    Fund parentFund = fundService.findById(elementTds.get(0).text());
    if (parentFund == null) {
      log.error("Parent Fund {} can't be found.", elementTds.get(0).text());
    } else {
      Fund originalFund = fundService.findById(elementTds.get(1).text());
      if (originalFund == null) {
        // new fund
        Fund newFund = new Fund();
        newFund.setId(elementTds.get(1).text());
        newFund.setName(elementTds.get(2).text());
        newFund.setCompanyId(parentFund.getCompanyId());
        newFund.setType(parentFund.getType());
        newFund.setParentId(elementTds.get(0).text());
        fundService.create(newFund);
      } else {
        originalFund.setName(elementTds.get(2).text());
        fundService.create(originalFund);
      }
    }
  }
}
