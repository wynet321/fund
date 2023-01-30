package net.canglong.fund.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FundPercentage {
    private String id;
    private String name;
    private String percentage;
    private LocalDate startDate;
    private LocalDate endDate;

}
