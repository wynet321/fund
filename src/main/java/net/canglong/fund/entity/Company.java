package net.canglong.fund.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "company")
public class Company {

  @Id
  private String id;
  private String name;
  private String abbr;
  @Column(name = "created_on")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createdOn;
  private String address;
}
