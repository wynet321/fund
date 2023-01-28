package net.canglong.fund.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class DatePriceIdentity {
    private LocalDate priceDate;
    private BigDecimal accumulatedPrice;
    public DatePriceIdentity(LocalDate priceDate, BigDecimal accumulatedPrice){
        this.priceDate=priceDate;
        this.accumulatedPrice=accumulatedPrice;
    }
}
