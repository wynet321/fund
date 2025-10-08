package net.canglong.fund.entity;

import java.math.BigDecimal;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "fund_return_rate_by_year")
@AllArgsConstructor
@NoArgsConstructor
public class YearRate {

  @EmbeddedId
  private YearRateIdentity yearRateIdentity;
  private String name;
  private String companyName;
  private String type;
  private BigDecimal rate;
}
