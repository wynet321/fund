package net.canglong.fund.service.impl;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import net.canglong.fund.entity.Company;
import net.canglong.fund.entity.Fund;
import net.canglong.fund.entity.MonthAveragePrice;
import net.canglong.fund.entity.Price;
import net.canglong.fund.entity.PriceIdentity;
import net.canglong.fund.entity.Status;
import net.canglong.fund.repository.PriceRepo;
import net.canglong.fund.service.CompanyService;
import net.canglong.fund.service.FundService;
import net.canglong.fund.service.PriceService;
import net.canglong.fund.service.WebsiteDataService;
import net.canglong.fund.vo.DatePriceIdentity;
import net.canglong.fund.vo.FundPercentage;
import net.canglong.fund.vo.MonthPrice;
import net.canglong.fund.vo.YearPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PriceServiceImpl implements PriceService {

  private final static int GET_FUND_LIST = 0;
  private final static int GET_FUND_PRICE = 1;
  @Resource
  private PriceRepo priceRepo;
  @Resource
  private WebsiteDataService websiteDataService;
  @Resource
  private FundService fundService;
  private ThreadPoolTaskExecutor executor;
  private long startTime;
  private int fundCount;
  @Resource
  private CompanyService companyService;
  private boolean retrievePriceForAllTerminated = false;
  @Value("${spring.threadCount}")
  private int threadCount;

  @Override
  public Page<Price> findByName(String name, Pageable pageable) {
    Fund fund = fundService.findByName(name);
    if (fund != null) {
      Price price = new Price();
      price.setPriceIdentity(new PriceIdentity(fund.getId(), null));
      return priceRepo.findAll(Example.of(price), pageable);
    }
    return null;
  }

  @Override
  public Page<Price> findByFundId(String id, Pageable pageable) {
    return priceRepo.findLatestPriceBeforeDate(id, pageable);
  }

  @Override
  public Integer create(String fundId) throws Exception {
    int page;
    int count = 0;
    Fund fund = fundService.findById(fundId);
    if (fund != null) {
      page = fund.getCurrentPage();
      log.debug("Start to collect fund {}", fund.getName());
      List<Price> prices;
      String priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
      do {
        prices = websiteDataService.getPrices(priceWebPage, fund, page - 1);
        prices = priceRepo.saveAll(prices);
        count += prices.size();
        if (count % 1000 == 0) {
          log.debug("{} completed {} records.", fund.getName(), count);
        }
        priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
      } while (websiteDataService.containsPrice(priceWebPage));
      log.debug("{} total {} records.", fund.getName(), count);
      log.info("{} page {} done.", fund.getName(), page - 1);
      fund.setCurrentPage(page - 1);
      fundService.create(fund);
    } else {
      throw new Exception("can't find the fund in DB.");
    }
    return count;
  }

  @Override
  public FundPercentage findPercentageByDate(String id, LocalDate startDate, LocalDate endDate) {
    Price priceAtStartDate = priceRepo.findLatestPriceBeforeDate(id, startDate);
    Price priceAtEndDate = priceRepo.findLatestPriceBeforeDate(id, endDate);
    BigDecimal ratio = priceAtEndDate.getAccumulatedPrice()
        .subtract(priceAtStartDate.getAccumulatedPrice())
        .divide(priceAtStartDate.getAccumulatedPrice(), 2, RoundingMode.HALF_UP);
    DecimalFormat df = new DecimalFormat("0.00%");
    String percentage = df.format(ratio);
    Fund fund = fundService.findById(id);
    return new FundPercentage(id, fund.getName(), percentage, startDate,
        endDate);
  }

  @Override
  public Price findStartDateById(String id) {
    return priceRepo.findPriceAtCreationById(id);
  }

  @Override
  public YearPrice findYearPriceById(String id) {
    Price priceAtFundCreation = priceRepo.findPriceAtCreationById(id);
    Fund fund = fundService.findById(id);
    if (priceAtFundCreation == null) {
      return new YearPrice(id, fund.getName(), new ArrayList<>());
    }
    LocalDate fundCreationDate = priceAtFundCreation.getPriceIdentity().getPriceDate();
    LocalDate today = LocalDate.now();
    int years = today.getYear() - fundCreationDate.getYear();
    List<DatePriceIdentity> priceList = new ArrayList<>();
    if (!priceAtFundCreation.getPriceIdentity().getPriceDate().isEqual(
        LocalDate.of(priceAtFundCreation.getPriceIdentity().getPriceDate().getYear(), 12, 31))) {
      priceList.add(new DatePriceIdentity(
          LocalDate.of(priceAtFundCreation.getPriceIdentity().getPriceDate().getYear() - 1, 12, 31),
          priceAtFundCreation.getAccumulatedPrice()));
    }
    for (int i = 0; i < years; i++) {
      LocalDate date = fundCreationDate.with(firstDayOfYear()).plusYears(1 + i);
      Price price = priceRepo.findLatestPriceBeforeDate(id, date);
      if (price.getAccumulatedPrice() != null) {
        priceList.add(new DatePriceIdentity(price.getPriceIdentity().getPriceDate(),
            price.getAccumulatedPrice()));
      }
    }
    return new YearPrice(id, fund.getName(), priceList);
  }

  @Override
  public Map<Integer, BigDecimal> findYearPriceMapById(String id) {
    YearPrice yearPrice = findYearPriceById(id);
    List<DatePriceIdentity> datePriceIdentities = yearPrice.getPriceList();
    Map<Integer, BigDecimal> yearPrices = new HashMap<>();
    for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
      yearPrices.put(datePriceIdentity.getPriceDate().getYear(),
          datePriceIdentity.getAccumulatedPrice());
    }
    return yearPrices;
  }

  @Override
  public MonthPrice findMonthPriceById(String id, int year) {
    Price priceAtFundCreation = priceRepo.findPriceAtCreationById(id);
    Fund fund = fundService.findById(id);
    if (priceAtFundCreation != null) {
      LocalDate fundCreationDate = priceAtFundCreation.getPriceIdentity().getPriceDate();
      LocalDate today = LocalDate.now();
      if (fundCreationDate.getYear() <= year && today.getYear() >= year) {
        List<DatePriceIdentity> priceList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
          LocalDate currentDate = LocalDate.of(year, i, 1);
          LocalDate date = currentDate.withDayOfMonth(
              currentDate.getMonth().length(currentDate.isLeapYear()));
          Price price = priceRepo.findLatestPriceBeforeDate(id, date);
          if (price != null && price.getAccumulatedPrice() != null) {
            priceList.add(new DatePriceIdentity(price.getPriceIdentity().getPriceDate(),
                price.getAccumulatedPrice()));
          }
        }
        return new MonthPrice(id, fund.getName(), year, priceList);
      }
    }
    return new MonthPrice(id, fund.getName(), year, new ArrayList<>());
  }

  @Override
  public Map<Integer, BigDecimal> findMonthPriceMapById(String id, int year) {
    MonthPrice monthPrice = findMonthPriceById(id, year);
    List<DatePriceIdentity> datePriceIdentities = monthPrice.getPriceList();
    Map<Integer, BigDecimal> monthPrices = new HashMap<>();
    for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
      monthPrices.put(datePriceIdentity.getPriceDate().getMonthValue(),
          datePriceIdentity.getAccumulatedPrice());
    }
    return monthPrices;
  }

  @Override
  public Price findEarliestPriceAfterDate(String id, LocalDate targetDate) {
    return priceRepo.findEarliestPriceAfterDate(id, targetDate);
  }

  @Override
  public Price findLatestPrice(String id) {
    return priceRepo.findLatestPrice(id);
  }

  @Override
  public Page<Price> find(String id, LocalDate startDate, Pageable pageable) {
    return priceRepo.findPriceSet(id, startDate, pageable);
  }

  @Override
  public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(String fundId,
      LocalDate startDate) {
    return priceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
  }

  @Override
  public Price findLatestPriceBeforeDate(String id, LocalDate date) {
    return priceRepo.findLatestPriceBeforeDate(id, date);
  }

  @Override
  @Scheduled(fixedDelay = 86400000)
  @Async
  public void retrievePriceForAll() {
    retrievePriceForAllTerminated = startPriceRetrievalJob(threadCount);
    if (retrievePriceForAllTerminated) {
      log.info(
          "Bypass fund retrieval and import job since less than 1 month's data need to be retrieved.");
    }
  }

  @Override
  @Scheduled(fixedDelay = 60000)
  @Async
  public void reportStatusOfRetrievePriceForAll() {
    Status status = getPriceRetrievalJobStatus();
    if (!retrievePriceForAllTerminated) {
      if (status.isTerminated()) {
        log.info("\n*******************\nRetrieval job was terminated.\n*******************");
      } else if (status.getTaskCount() > 0) {
        log.info(
            "\n*******************\nTotal fund count: {}\nLeft fund count: {}\nElapse Time: {}\nActive Threads: {}\n*******************",
            status.getTotalFundCount(), status.getLeftFundCount(), status.getElapseTime(),
            status.getAliveThreadCount());
      }
      retrievePriceForAllTerminated = status.isTerminated();
    }
  }

  @Override
  public Boolean startPriceRetrievalJob(int threadCount) {
    Price price = findLatestPrice("000001");
    if (LocalDate.now().isAfter(price.getPriceIdentity().getPriceDate().plusMonths(0))) {
      log.info("Start to retrieve fund information...");
      log.info("Website retrieval thread count is {}", threadCount);
      if (executor == null || executor.getThreadPoolExecutor().isShutdown()) {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setThreadNamePrefix("Website retrieval thread pool");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
      }
      if (executor.getThreadPoolExecutor().getQueue().isEmpty()) {
        List<String> companyIds = websiteDataService.getCompanyIds();
        log.info("Totally found {} companies.", companyIds.size());
        startTime = System.currentTimeMillis();
        for (String id : companyIds) {
          if (!executor.getThreadPoolExecutor().isShutdown()) {
            executor.execute(new Task(GET_FUND_LIST, id));
          }
        }
        fundCount = executor.getThreadPoolExecutor().getQueue().size();
        log.info("Totally found {} funds.", fundCount);
        executor.shutdown();
      }
      return false;
    }
    return true;
  }

  @Override
  public Status getPriceRetrievalJobStatus() {
    Status status = new Status();
    if (executor != null) {
      status.setTotalFundCount(fundCount);
      status.setLeftFundCount(executor.getThreadPoolExecutor().getQueue().size());
      status.setAliveThreadCount(executor.getActiveCount());
      status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
      status.setTerminated(executor.getThreadPoolExecutor().isTerminated());
      status.setTaskCount(executor.getThreadPoolExecutor().getTaskCount());
    }
    return status;
  }

  @Override
  public boolean stopPriceRetrievalJob() {
    try {
      executor.getThreadPoolExecutor().getQueue().clear();
      executor.shutdown();
      log.info("executor is terminating...");
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  class Task implements Runnable {

    private final String id;
    private final int jobType;

    public Task(int jobType, String id) {
      this.jobType = jobType;
      this.id = id;
    }

    @Override
    public void run() {
      try {
        if (jobType == GET_FUND_PRICE) {
          create(id);
        } else if (jobType == GET_FUND_LIST) {
          Company savedCompany = companyService.create(websiteDataService.getCompany(id));
          List<Fund> fundList = fundService.create(
              websiteDataService.getFunds(id, savedCompany.getAbbr()));
          log.info("Imported fund list for {}. Total funds: {}", savedCompany.getName(),
              fundList.size());
          for (Fund fund : fundList) {
            if (!executor.getThreadPoolExecutor().isShutdown()) {
              executor.execute(new Task(GET_FUND_PRICE, fund.getId()));
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
