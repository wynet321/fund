package net.canglong.fund.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Fund {

  @Id
  private String id;
  @Column(name = "parent_id")
  private String parentId;
  private String name;
  @Column(name = "company_id")
  private String companyId;
  private String type;
  @Column(name = "current_page")
  private int currentPage = 0;
  @Column(name = "average_rate_by_month")
  private BigDecimal averageRateByMonth;
  @Column(name = "average_rate_by_year")
  private BigDecimal averageRateByYear;
  @Column(name = "total_rate")
  private BigDecimal totalRate;
  @Column(name = "statistic_due_date")
  private LocalDate statisticDueDate;

}
