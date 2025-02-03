package net.canglong.fund.entity;

import lombok.Data;

@Data
public class Status {

  private int totalFundCount = 0;
  private int leftFundCount = 0;
  private long elapseTime = 0L;
  private boolean terminated = false;
  private int aliveThreadCount = 0;
  private long taskCount = 0;
}
