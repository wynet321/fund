package net.cheetahead.fund.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "currency_price")
public class CurrencyPrice {

    @EmbeddedId
    private PriceIdentity priceIdentity;
    @Column(name = "return_of_ten_kilo")
    private BigDecimal returnOfTenKilo;
    @Column(name = "seven_day_annualized_rate_of_return")
    private BigDecimal sevenDayAnnualizedRateOfReturn;
    
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
    
    
}
