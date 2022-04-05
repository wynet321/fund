package net.soryu.fund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class MonthRateIdentity implements Serializable {

    private static final long serialVersionUID = 5044068352262279471L;

    @Column(name = "fund_id")
    private String fundId;
    private int month;

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
}
