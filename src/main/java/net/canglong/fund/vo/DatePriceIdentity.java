package net.canglong.fund.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DatePriceIdentity {
    private LocalDate priceDate;
    private BigDecimal accumulatedPrice;
}
