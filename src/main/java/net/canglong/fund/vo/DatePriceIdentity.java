package net.canglong.fund.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DatePriceIdentity {

  private LocalDate priceDate;
  private BigDecimal accumulatedPrice;
}
