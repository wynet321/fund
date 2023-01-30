package net.canglong.fund.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class MonthPrice {
    private String fundId;
    private String fundName;
    private int year;
    private List<DatePriceIdentity> priceList;
}
