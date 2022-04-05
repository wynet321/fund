package net.soryu.fund.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
@Getter
@Setter
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
}
