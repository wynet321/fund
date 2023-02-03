package net.canglong.fund.controller;

import javax.annotation.Resource;
import net.canglong.fund.service.JobService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/import", headers = "Accept=application/json")
public class ImportJobController {

  @Resource
  private JobService jobService;

  @PostMapping(value = {"/{threadCount}", ""})
  public Object startImportJob(
      @PathVariable(name = "threadCount", required = false) Integer threadCount) {
    if (threadCount == null) {
      return jobService.startPriceRetrievalJob(10);
    }
    return jobService.startPriceRetrievalJob(threadCount);
  }

  @GetMapping
  public Object getJobStatus() {
    return jobService.getPriceRetrievalJobStatus();
  }

  @DeleteMapping
  public Object stopImportJob() {
    return jobService.stopPriceRetrievalJob();
  }

}
