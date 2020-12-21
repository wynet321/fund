package net.cheetahead.fund.entity;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fund_return_rate_by_year")
public class YearRate {
    @EmbeddedId
    private YearRateIdentity yearRateIdentity;
    private BigDecimal rate;

    public YearRateIdentity getYearRateIdentity() {
        return yearRateIdentity;
    }

    public void setYearRateIdentity(YearRateIdentity yearRateIdentity) {
        this.yearRateIdentity = yearRateIdentity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }



}
