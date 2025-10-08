package net.canglong.fund.entity;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class PriceIdentity implements Serializable {

  private static final long serialVersionUID = -1767477911376005219L;

  @Column(name = "fund_id")
  private String fundId;
  private LocalDate priceDate;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((priceDate == null) ? 0 : priceDate.hashCode());
    result = prime * result + ((fundId == null) ? 0 : fundId.hashCode());
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
    PriceIdentity other = (PriceIdentity) obj;
    if (priceDate == null) {
      if (other.priceDate != null) {
        return false;
      }
    } else if (!priceDate.equals(other.priceDate)) {
      return false;
    }
    if (fundId == null) {
      return other.fundId == null;
    } else {
      return fundId.equals(other.fundId);
    }
  }


}
