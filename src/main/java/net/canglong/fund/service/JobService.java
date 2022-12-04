package net.canglong.fund.service;

import net.canglong.fund.entity.Status;

public interface JobService {

    Status startPriceRetrievalJob(int threadCount) throws Exception;

    Status getPriceRetrievalJobStatus() throws Exception;
    
    boolean stopPriceRetrievalJob() throws Exception;
}
