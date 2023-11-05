package net.canglong.fund.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearPrice {

  private String fundId;
  private String fundName;
  private List<DatePriceIdentity> priceList;

}
