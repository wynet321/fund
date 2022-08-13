package net.canglong.fund.entity;

import java.math.BigDecimal;

public interface MonthAveragePrice {

    String getId();

    BigDecimal getAveragePrice();

    int getMonth();

}
