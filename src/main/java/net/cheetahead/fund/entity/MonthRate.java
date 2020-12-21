package net.cheetahead.fund.entity;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fund_return_rate_by_month")
public class MonthRate {
    @EmbeddedId
    private MonthRateIdentity monthRateIdentity;
    private BigDecimal rate;

    public MonthRateIdentity getMonthRateIdentity() {
        return monthRateIdentity;
    }

    public void setMonthRateIdentity(MonthRateIdentity monthRateIdentity) {
        this.monthRateIdentity = monthRateIdentity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

}
