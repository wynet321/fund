package net.soryu.fund.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MonthRateIdentity implements Serializable {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fundId == null) ? 0 : fundId.hashCode());
        result = prime * result + month;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonthRateIdentity other = (MonthRateIdentity) obj;
        if (fundId == null) {
            if (other.fundId != null)
                return false;
        } else if (!fundId.equals(other.fundId))
            return false;
        if (month != other.month)
            return false;
        return true;
    }

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