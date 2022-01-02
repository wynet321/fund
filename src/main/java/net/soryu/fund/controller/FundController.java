package net.soryu.fund.controller;

import net.soryu.fund.service.CompanyService;
import net.soryu.fund.service.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/fund", headers = "Accept=application/json")
public class FundController {

    @Resource
    private FundService fundService;

    @Resource
    private CompanyService companyService;

    // private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/{id}")
    public Object find(@PathVariable("id") String id) throws Exception {
        return fundService.findById(id);
    }
    // @PostMapping
    // public Object fetch() throws Exception {
    // List<Company> companies = companyService.getAll();
    // List<Fund> funds = new LinkedList<Fund>();
    // for (Company company : companies) {
    // List<Fund> companyFunds = fundService.create(fetchDataService.fetchFund(company));
    // funds.addAll(companyFunds);
    // logger.info(new ObjectMapper().writeValueAsString(companyFunds));
    // }
    // return funds;
    // }
    //
    // @PostMapping(value = "/{id}")
    // public Object fetch(@PathVariable("id") String id) throws Exception {
    // Company company = companyService.get(id);
    // List<Fund> companyFunds = fetchDataService.fetchFund(company);
    // for (Fund fund : companyFunds) {
    // fundService.create(fund);
    // }
    // return companyFunds;
    // }
}
