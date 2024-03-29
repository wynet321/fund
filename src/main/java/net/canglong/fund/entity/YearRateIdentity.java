package net.canglong.fund.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class YearRateIdentity implements Serializable {

  private static final long serialVersionUID = -8108587183162053549L;

  private String fundId;
  private int year;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fundId == null) ? 0 : fundId.hashCode());
    result = prime * result + year;
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
    YearRateIdentity other = (YearRateIdentity) obj;
    if (fundId == null) {
      if (other.fundId != null) {
        return false;
      }
    } else if (!fundId.equals(other.fundId)) {
      return false;
    }
    return year == other.year;
  }

}
