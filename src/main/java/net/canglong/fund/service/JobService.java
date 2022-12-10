package net.canglong.fund.service;

import net.canglong.fund.entity.Status;

public interface JobService {

    Status startPriceRetrievalJob(int threadCount);

    Status getPriceRetrievalJobStatus();
    
    boolean stopPriceRetrievalJob();
}
