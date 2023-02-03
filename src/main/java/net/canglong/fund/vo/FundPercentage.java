package net.canglong.fund.vo;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class FundPercentage {

  private String id;
  private String name;
  private String percentage;
  private LocalDate startDate;
  private LocalDate endDate;

}
