package net.soryu.fund.service;

import net.soryu.fund.entity.Status;

public interface JobService {

    Status startPriceRetrievalJob() throws Exception;

    Status getPriceRetrievalJobStatus() throws Exception;
    
    boolean stopPriceRetrievalJob() throws Exception;
}
