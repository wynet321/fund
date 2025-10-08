package net.canglong.fund.entity;

import java.math.BigDecimal;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
