package net.cheetahead.fund.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MonthRateIdentity implements Serializable {

    private static final long serialVersionUID = 5044068352262279471L;

    @Column(name = "fund_id")
    private String fundId;

    private int month;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

}
