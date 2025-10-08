package net.canglong.fund.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatePriceIdentity {

  private LocalDate priceDate;
  private BigDecimal accumulatedPrice;
}
