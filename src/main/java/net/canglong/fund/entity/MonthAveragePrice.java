package net.canglong.fund.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import lombok.Data;
import lombok.Getter;

@Data
public class MonthAveragePrice {
  @Column(name = "month")
  int month;
  @Column(name = "averagePrice")
  BigDecimal averagePrice;

}
