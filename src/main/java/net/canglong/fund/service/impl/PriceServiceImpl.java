package net.canglong.fund.service.impl;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.math.BigDecimal;
import java.util.Map;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
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
import net.canglong.fund.vo.FundPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PriceServiceImpl implements PriceService {

  @Resource
  private PriceRepo priceRepo;
  @Resource
  private WebsiteDataService websiteDataService;
  @Resource
  private FundService fundService;
  @Resource(name = "priceExecutor")
  private ThreadPoolTaskExecutor priceExecutor;
  @Resource(name = "fundExecutor")
  private ThreadPoolTaskExecutor fundExecutor;
  private long startTime;
  private final AtomicInteger fundCount = new AtomicInteger(0);
  @Resource
  private CompanyService companyService;
  @Value("${fund.threadCount}")
  private int threadCount;

  @Override
  public Page<Price> findByName(@NonNull String name, @NonNull Pageable pageable) {
    Fund fund = fundService.findByName(name);
    if (fund != null) {
      Price price = new Price();
      price.setPriceIdentity(new PriceIdentity(fund.getId(), null));
      return priceRepo.findAll(Example.of(price), pageable);
    }
    return null;
  }

  @Override
  public Page<Price> findByFundId(@NonNull String id, @NonNull Pageable pageable) {
    return priceRepo.findLatestPriceBeforeDate(id, pageable);
  }

  @Override
  public Integer create(@NonNull String fundId) throws Exception {
    int page;
    int count = 0;
    
    Fund fund = fundService.findById(fundId)
        .orElseThrow(() -> new Exception("Fund not found with id: " + fundId));
    
    LocalDate latestPriceDate = priceRepo.findLatestPriceDateById(fundId);
    if (latestPriceDate == null || LocalDate.now()
        .isAfter(LocalDate.parse(latestPriceDate.toString()).plusWeeks(1))) {
      page = fund.getCurrentPage();
      log.debug("Start to collect fund {}", fund.getName());
      List<Price> prices;
      String priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
      do {
        prices = websiteDataService.getPrices(priceWebPage, fund, page - 1);
        if (prices == null) {
          log.warn("Received null prices for fund {} on page {}", fund.getId(), page - 1);
          break;
        }
        if (!prices.isEmpty()) {
          prices = priceRepo.saveAll(prices);
          count += prices.size();
        }
        if (count % 1000 == 0) {
          log.debug("{} completed {} records.", fund.getName(), count);
        }
        priceWebPage = websiteDataService.getPriceWebPage(fund, page++);
      } while (websiteDataService.containsPrice(priceWebPage));
      log.debug("{} total {} records.", fund.getName(), count);
      log.info("{} page {} done.", fund.getName(), page - 1);
      fund.setCurrentPage(page - 1);
      fundService.create(fund);
      LocalDate fundLatestDate = priceRepo.findLatestPriceDateById(fund.getId());
      log.info("Fund {} latest price date is {}", fund.getId(),
          fundLatestDate != null ? fundLatestDate.toString() : "Empty");
    } else {
      log.info("Bypass fund {} latest price date is {}", fund.getId(), latestPriceDate);
    }
    
    return count;
  }

  @Override
  public FundPercentage findPercentageByDate(@NonNull String id, @NonNull LocalDate startDate, @NonNull LocalDate endDate) {
    Price priceAtStartDate = priceRepo.findLatestPriceBeforeDate(id, startDate);
    Price priceAtEndDate = priceRepo.findLatestPriceBeforeDate(id, endDate);
    // Guard against nulls and divide-by-zero
    if (priceAtStartDate == null || priceAtEndDate == null
        || priceAtStartDate.getAccumulatedPrice() == null
        || BigDecimal.ZERO.compareTo(priceAtStartDate.getAccumulatedPrice()) == 0
        || priceAtEndDate.getAccumulatedPrice() == null) {
      return fundService.findById(id)
          .map(fund -> new FundPercentage(id, fund.getName(), "0.00%", startDate, endDate))
          .orElse(new FundPercentage(id, id, "0.00%", startDate, endDate));
    }
    BigDecimal ratio = priceAtEndDate.getAccumulatedPrice()
        .subtract(priceAtStartDate.getAccumulatedPrice())
        .divide(priceAtStartDate.getAccumulatedPrice(), 2, RoundingMode.HALF_UP);
    DecimalFormat df = new DecimalFormat("0.00%");
    String percentage = df.format(ratio);
    Fund fund = fundService.findById(id)
        .orElseThrow(() -> new RuntimeException("Fund not found with id: " + id));
    return new FundPercentage(id, fund.getName(), percentage, startDate, endDate);
  }

  @Override
  public Price findPriceAtCreationById(@NonNull String id) {
    return priceRepo.findPriceAtCreationById(id);
  }

  @Override
  public YearPrice findYearPriceById(@NonNull String id) {
    Price priceAtFundCreation = priceRepo.findPriceAtCreationById(id);
    Fund fund = fundService.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Fund not found with id: " + id));
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
          priceAtFundCreation.getAccumulatedPrice(), priceAtFundCreation.getPrice()));
    }
    for (int i = 0; i < years; i++) {
      LocalDate date = fundCreationDate.with(firstDayOfYear()).plusYears(1 + i);
      Price price = priceRepo.findLatestPriceBeforeDate(id, date);
      if (price != null && price.getAccumulatedPrice() != null) {
        priceList.add(new DatePriceIdentity(price.getPriceIdentity().getPriceDate(),
            price.getAccumulatedPrice(), price.getPrice()));
      }
    }
    return new YearPrice(id, fund.getName(), priceList);
  }

  @Override
  public Map<Integer, FundPrice> findYearPriceMapById(@NonNull String id) {
    YearPrice yearPrice = findYearPriceById(id);
    List<DatePriceIdentity> datePriceIdentities = yearPrice.getPriceList();
    Map<Integer, FundPrice> yearPrices = new HashMap<>();
    for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
      yearPrices.put(datePriceIdentity.getPriceDate().getYear(),
          new FundPrice(datePriceIdentity.getAccumulatedPrice(), datePriceIdentity.getCurrentPrice()));
    }
    return yearPrices;
  }

  @Override
  public MonthPrice findMonthPriceById(@NonNull String id, int year) {
    Price priceAtFundCreation = priceRepo.findPriceAtCreationById(id);
    Fund fund = fundService.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Fund not found with id: " + id));
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
                price.getAccumulatedPrice(), price.getPrice()));
          }
        }
        return new MonthPrice(id, fund.getName(), year, priceList);
      }
    }
    return new MonthPrice(id, fund.getName(), year, new ArrayList<>());
  }

  @Override
  public Map<Integer, FundPrice> findMonthPriceMapById(@NonNull String id, int year) {
    MonthPrice monthPrice = findMonthPriceById(id, year);
    List<DatePriceIdentity> datePriceIdentities = monthPrice.getPriceList();
    Map<Integer, FundPrice> monthPrices = new HashMap<>();
    for (DatePriceIdentity datePriceIdentity : datePriceIdentities) {
      monthPrices.put(datePriceIdentity.getPriceDate().getMonthValue(),
          new FundPrice(datePriceIdentity.getAccumulatedPrice(), datePriceIdentity.getCurrentPrice()));
    }
    return monthPrices;
  }

  @Override
  public Price findEarliestPriceAfterDate(@NonNull String id, @NonNull LocalDate targetDate) {
    return priceRepo.findEarliestPriceAfterDate(id, targetDate);
  }

  @Override
  public LocalDate findLatestPriceDateById(@NonNull String id) {
    return priceRepo.findLatestPriceDateById(id);
  }

  @Override
  public LocalDate findEarliestPriceDateById(@NonNull String id) {
    return priceRepo.findEarliestPriceDateById(id);
  }

  @Override
  public Page<Price> find(@NonNull String id, @NonNull LocalDate startDate, @NonNull Pageable pageable) {
    return priceRepo.findPriceSet(id, startDate, pageable);
  }

  @Override
  public List<MonthAveragePrice> findAllMonthAveragePriceByFundId(@NonNull String fundId,
      @NonNull LocalDate startDate) {
    return priceRepo.findAllMonthAveragePriceByFundId(fundId, startDate);
  }

  @Override
  public Price findLatestPriceBeforeDate(@NonNull String id, @NonNull LocalDate date) {
    return priceRepo.findLatestPriceBeforeDate(id, date);
  }

  @Async
  @Scheduled(fixedDelay = 60000)
  public void reportStatusOfRetrievePriceForAll() {
    Status status = getPriceRetrievalJobStatus();
    if (status.isTerminated() && priceExecutor != null) {
      log.info("\n*******************\nRetrieval job was completed.\n*******************");
      fundExecutor = null;
      priceExecutor = null;
    } else if (status.getTaskCount() > 0) {
      log.info(
          "\n*******************\nPrice Retrieval\nTotal fund count: {}\nLeft fund count: {}\nElapse Time: {}\nActive Threads: {}\n*******************",
          status.getTotalFundCount(), status.getLeftFundCount(), status.getElapseTime(),
          status.getAliveThreadCount());
    }
  }

  @Override
  public Boolean startPriceRetrievalJob(int threadCount) {
    if (threadCount <= 0) {
      threadCount = this.threadCount;
    }
    log.info("Start to retrieve fund information...");
    log.info("Website retrieval thread count is {}", threadCount);
    if (priceExecutor == null || priceExecutor.getThreadPoolExecutor().isShutdown()) {
      priceExecutor = new ThreadPoolTaskExecutor();
      priceExecutor.setCorePoolSize(threadCount);
      priceExecutor.setThreadNamePrefix("Price retrieval thread pool");
      priceExecutor.setWaitForTasksToCompleteOnShutdown(true);
      priceExecutor.initialize();
      fundExecutor = new ThreadPoolTaskExecutor();
      fundExecutor.setCorePoolSize(threadCount);
      fundExecutor.setThreadNamePrefix("Fund retrieval thread pool");
      fundExecutor.setWaitForTasksToCompleteOnShutdown(true);
      fundExecutor.initialize();
    }
    if (priceExecutor.getThreadPoolExecutor().getQueue().isEmpty()) {
      List<String> companyIds = websiteDataService.getCompanyIds();
      log.info("Totally found {} companies.", companyIds.size());
      startTime = System.currentTimeMillis();
      for (String companyId : companyIds) {
        if (companyId != null) {
          fundExecutor.execute(new FundTask(companyId));
        } else {
          log.warn("Skipping null companyId in the list");
        }
      }
      fundExecutor.shutdown();
    }
    return false;
  }

  @Async
  @Scheduled(fixedDelay = 86400000)
  public void startPriceRetrievalJob() {
    startPriceRetrievalJob(this.threadCount);
  }

  @Override
  public Status getPriceRetrievalJobStatus() {
    Status status = new Status();
    if (fundExecutor != null && fundExecutor.getThreadPoolExecutor().isTerminated()) {
      status.setTotalFundCount(fundCount.get());
      if (priceExecutor != null) {
        priceExecutor.getThreadPoolExecutor().shutdown();
      }
    }
    if (priceExecutor != null) {
      status.setLeftFundCount(priceExecutor.getThreadPoolExecutor().getQueue().size());
      status.setAliveThreadCount(priceExecutor.getActiveCount());
      status.setElapseTime((System.currentTimeMillis() - startTime) / 1000);
      status.setTerminated(priceExecutor.getThreadPoolExecutor().isTerminated());
      status.setTaskCount(priceExecutor.getThreadPoolExecutor().getTaskCount());
    }
    return status;
  }

  @Override
  public boolean stopPriceRetrievalJob() {
    try {
      priceExecutor.getThreadPoolExecutor().getQueue().clear();
      priceExecutor.shutdown();
      log.info("executor is terminating...");
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public List<Price> findByFundIdAndDateRange(String fundId, LocalDate startDate, LocalDate endDate) {
    return priceRepo.findByFundIdAndDateRange(fundId, startDate, endDate);
  }

  class PriceTask implements Runnable {

    private final String fundId;

    public PriceTask(String fundId) {
      this.fundId = fundId;
    }

    @Override
    public void run() {
      try {
        if (fundId != null) {
          create(fundId);
        } else {
          log.error("fundId is null in PriceTask");
        }
      } catch (Exception e) {
        log.error("Error in PriceTask for fundId: " + fundId, e);
      }
    }
  }

  class FundTask implements Runnable {

    private final String companyId;

    public FundTask(@NonNull String companyId) {
      this.companyId = companyId;
    }

    @Override
    public void run() {
      try {
        Company company = websiteDataService.getCompany(companyId);
        if (company == null) {
          log.warn("No company found for id: {}", companyId);
          return;
        }
        Company savedCompany = companyService.create(company);
        String companyAbbr = savedCompany.getAbbr() != null ? savedCompany.getAbbr() : "";
        List<Fund> fundList = fundService.create(
            websiteDataService.getFunds(Objects.requireNonNull(company.getId()), companyAbbr));
        log.info("Total {} funds found for {}", fundList.size(),
            savedCompany.getName());
        fundCount.addAndGet(fundList.size());
        for (Fund fund : fundList) {
          try {
            priceExecutor.execute(new PriceTask(fund.getId()));
          } catch (RejectedExecutionException e) {
            log.warn("Price task for fund {} rejected - thread pool saturated", fund.getId());
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
