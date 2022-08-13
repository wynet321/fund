package net.canglong.fund.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "fund_return_rate_by_year")
public class YearRate {
    @EmbeddedId
    private YearRateIdentity yearRateIdentity;
    private BigDecimal rate;
}
