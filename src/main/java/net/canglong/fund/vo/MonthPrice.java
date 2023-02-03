package net.canglong.fund.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class MonthPrice {

  private String fundId;
  private String fundName;
  private int year;
  private List<DatePriceIdentity> priceList;
}
