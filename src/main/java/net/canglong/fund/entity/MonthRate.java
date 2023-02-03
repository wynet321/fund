package net.canglong.fund.entity;

import java.math.BigDecimal;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fund_return_rate_by_month")
public class MonthRate {

  @EmbeddedId
  private MonthRateIdentity monthRateIdentity;
  private String name;
  private String companyName;
  private String type;
  private BigDecimal rate;
}
