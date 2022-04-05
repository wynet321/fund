package net.soryu.fund.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

    private int leftCount;
    private long elapseTime;
    private boolean stopped;
    private int aliveThreadCount;

}
