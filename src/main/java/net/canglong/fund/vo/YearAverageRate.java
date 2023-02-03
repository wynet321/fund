package net.canglong.fund.vo;

import java.math.BigDecimal;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.canglong.fund.entity.YearRateIdentity;

@Data
@AllArgsConstructor
public class YearAverageRate {
  private String fundId;
  private String name;
  private String companyName;
  private String type;
  private BigDecimal average;
  private BigDecimal stddev;
}
