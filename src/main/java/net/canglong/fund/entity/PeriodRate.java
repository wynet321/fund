package net.canglong.fund.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import lombok.Data;

@Data
@Entity
@Table(name = "fund_return_rate_by_period")
public class PeriodRate {

  @Id
  private String id;
  private String name;
  private String companyName;
  private String type;
  @Column(name = "one_year_rate")
  private BigDecimal oneYearRate;
  @Column(name = "two_year_rate")
  private BigDecimal twoYearRate;
  @Column(name = "three_year_rate")
  private BigDecimal threeYearRate;
  @Column(name = "four_year_rate")
  private BigDecimal fourYearRate;
  @Column(name = "five_year_rate")
  private BigDecimal fiveYearRate;
  @Column(name = "six_year_rate")
  private BigDecimal sixYearRate;
  @Column(name = "seven_year_rate")
  private BigDecimal sevenYearRate;
  @Column(name = "eight_year_rate")
  private BigDecimal eightYearRate;
  @Column(name = "nine_year_rate")
  private BigDecimal nineYearRate;
  @Column(name = "ten_year_rate")
  private BigDecimal tenYearRate;

}
