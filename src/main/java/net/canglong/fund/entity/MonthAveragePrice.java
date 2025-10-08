package net.canglong.fund.entity;

import java.math.BigDecimal;

// Interface-based projection for native query mapping in Spring Data
public interface MonthAveragePrice {
  Integer getMonth();
  BigDecimal getAveragePrice();
}
