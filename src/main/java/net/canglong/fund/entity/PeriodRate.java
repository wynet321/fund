package net.canglong.fund.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "fund_return_rate_by_period")
public class PeriodRate {
    @Id
    private String id;
    private String name;
    private String companyName;
    private String type;
    private BigDecimal oneYearRate;
    private BigDecimal twoYearRate;
    private BigDecimal threeYearRate;
    private BigDecimal fourYearRate;
    private BigDecimal fiveYearRate;
    private BigDecimal sixYearRate;
    private BigDecimal sevenYearRate;
    private BigDecimal eightYearRate;
    private BigDecimal nineYearRate;
    private BigDecimal tenYearRate;

}
