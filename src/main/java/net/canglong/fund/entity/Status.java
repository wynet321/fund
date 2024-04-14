package net.canglong.fund.entity;

import lombok.Data;

@Data
public class Status {

  private int totalFundCount;
  private int leftFundCount;
  private long elapseTime;
  private boolean terminated;
  private int aliveThreadCount;
  private long taskCount;
}
