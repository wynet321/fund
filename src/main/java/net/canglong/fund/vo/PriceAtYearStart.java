package net.canglong.fund.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PriceAtYearStart {
    private String fundId;
    private String fundName;
    private List<DatePriceIdentity> priceList;

    public PriceAtYearStart(String fundId, String fundName, List<DatePriceIdentity> priceList){
        this.fundId=fundId;
        this.fundName=fundName;
        this.priceList=priceList;
    }
}
