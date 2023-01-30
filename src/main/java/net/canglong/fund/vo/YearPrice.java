package net.canglong.fund.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class YearPrice {
    private String fundId;
    private String fundName;
    private List<DatePriceIdentity> priceList;

}
