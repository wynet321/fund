package net.canglong.fund.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FundPercentage {
    private String id;
    private String name;
    private String percentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public FundPercentage(String id, String name, String percentage, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
