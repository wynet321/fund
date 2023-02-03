package net.canglong.fund.entity;

import lombok.Data;

@Data
public class Status {

  private int leftCount;
  private long elapseTime;
  private boolean terminated;
  private int aliveThreadCount;
  private long taskCount;
}
