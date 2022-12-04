package net.canglong.fund.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
    private int leftCount;
    private long elapseTime;
    private boolean terminated;
    private int aliveThreadCount;
    private long taskCount;
}
