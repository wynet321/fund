package net.soryu.fund.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "fund_return_rate_by_month")
public class MonthRate {
    @EmbeddedId
    private MonthRateIdentity monthRateIdentity;
    private BigDecimal rate;
}
