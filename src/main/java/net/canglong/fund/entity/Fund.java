package net.canglong.fund.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fund")
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
  @Column(name = "statistic_due_date")
  private LocalDate statisticDueDate;
  @Column(name = "start_date")
  private LocalDate startDate;
  @Column(name = "end_date")
  private LocalDate endDate;

}
