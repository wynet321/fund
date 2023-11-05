package net.canglong.fund.vo;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

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
