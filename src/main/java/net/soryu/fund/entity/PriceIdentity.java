package net.soryu.fund.entity;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((fundId == null) ? 0 : fundId.hashCode());
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
        PriceIdentity other = (PriceIdentity) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (fundId == null) {
            if (other.fundId != null)
                return false;
        } else if (!fundId.equals(other.fundId))
            return false;
        return true;
    }


}
