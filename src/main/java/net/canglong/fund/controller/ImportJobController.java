package net.canglong.fund.controller;

import net.canglong.fund.service.JobService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/import", headers = "Accept=application/json")
public class ImportJobController {

    @Resource
    private JobService jobService;

    @PostMapping
    public Object startImportJob() throws Exception {
        return jobService.startPriceRetrievalJob();
    }

    @GetMapping
    public Object getJobStatus() throws Exception {
        return jobService.getPriceRetrievalJobStatus();
    }

    @DeleteMapping
    public Object stopImportJob() throws Exception {
        return jobService.stopPriceRetrievalJob();
    }

}
