package net.cheetahead.fund.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class YearRateIdentity implements Serializable {

    private static final long serialVersionUID = -8108587183162053549L;

    @Column(name = "fund_id")
    private String fundId;
    private int year;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
