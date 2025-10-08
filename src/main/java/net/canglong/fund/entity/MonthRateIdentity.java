package net.canglong.fund.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MonthRateIdentity implements Serializable {

  private static final long serialVersionUID = 5044068352262279471L;

  @Column(name = "fund_id")
  private String fundId;
  private int year;
  private int month;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fundId == null) ? 0 : fundId.hashCode());
    result = prime * result + year;
    result = prime * result + month;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MonthRateIdentity other = (MonthRateIdentity) obj;
    if (fundId == null) {
      if (other.fundId != null) {
        return false;
      }
    } else if (!fundId.equals(other.fundId)) {
      return false;
    }
    if (year != other.year) {
      return false;
    }
    return month == other.month;
  }
}
