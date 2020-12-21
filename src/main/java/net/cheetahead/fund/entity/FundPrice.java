package net.cheetahead.fund.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="fund_price")
public class FundPrice {

    @EmbeddedId
    private PriceIdentity priceIdentity;

    private BigDecimal price;
    @Column(name = "accumulated_price")
    private BigDecimal accumulatedPrice;

    public PriceIdentity getPriceIdentity() {
        return priceIdentity;
    }

    public void setPriceIdentity(PriceIdentity priceIdentity) {
        this.priceIdentity = priceIdentity;
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
