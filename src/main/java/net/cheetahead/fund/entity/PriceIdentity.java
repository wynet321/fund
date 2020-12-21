package net.cheetahead.fund.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PriceIdentity implements Serializable {

    private static final long serialVersionUID = -1767477911376005219L;
    
    @Column(name = "fund_id")
    private String fundId;
    private Date date;

    public PriceIdentity() {
        
    }
    
    public PriceIdentity(String fundId, Date date) {
        this.date = date;
        this.fundId = fundId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
