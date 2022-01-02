package net.soryu.fund.controller;

import net.soryu.fund.service.DataService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/import", headers = "Accept=application/json")
public class ImportController {

    @Resource
    private DataService dataService;

    @PostMapping
    public Object importData() throws Exception {
        return dataService.generateFundPriceRetrievalJob();
    }

    @GetMapping
    public Object getStatus() throws Exception {
        return dataService.getStatus();
    }

    @DeleteMapping
    public Object stopImport() throws Exception {
        return dataService.stop();
    }


}
