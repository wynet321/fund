package net.soryu.fund.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fund_price")
public class Price {

    @EmbeddedId
    private PriceIdentity priceIdentity;

    @Column(name = "return_of_ten_kilo")
    private BigDecimal returnOfTenKilo;

    @Column(name = "seven_day_annualized_rate_of_return")
    private BigDecimal sevenDayAnnualizedRateOfReturn;

    private BigDecimal price;

    @Column(name = "accumulated_price")
    private BigDecimal accumulatedPrice;

    public PriceIdentity getPriceIdentity() {
        return priceIdentity;
    }

    public void setPriceIdentity(PriceIdentity priceIdentity) {
        this.priceIdentity = priceIdentity;
    }

    public BigDecimal getReturnOfTenKilo() {
        return returnOfTenKilo;
    }

    public void setReturnOfTenKilo(BigDecimal returnOfTenKilo) {
        this.returnOfTenKilo = returnOfTenKilo;
    }

    public BigDecimal getSevenDayAnnualizedRateOfReturn() {
        return sevenDayAnnualizedRateOfReturn;
    }

    public void setSevenDayAnnualizedRateOfReturn(BigDecimal sevenDayAnnualizedRateOfReturn) {
        this.sevenDayAnnualizedRateOfReturn = sevenDayAnnualizedRateOfReturn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAccumulatedPrice() {
        return accumulatedPrice;
    }

    public void setAccumulatedPrice(BigDecimal accumulatedPrice) {
        this.accumulatedPrice = accumulatedPrice;
    }
    
}
