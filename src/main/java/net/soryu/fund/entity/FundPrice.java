package net.soryu.fund.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class FundPrice implements Serializable {

    private static final long serialVersionUID = -2151168637864582701L;

    @EmbeddedId
    private PriceIdentity priceIdentity;

    public PriceIdentity getPriceIdentity() {
        return priceIdentity;
    }

    public void setPriceIdentity(PriceIdentity priceIdentity) {
        this.priceIdentity = priceIdentity;
    }
}
