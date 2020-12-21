package net.cheetahead.fund.entity;

public class Status {
    
    private int queueSize;
    private long elapseTime;
    private boolean stopped;
    private int aliveThreadCount;
    
    public int getQueueSize() {
        return queueSize;
    }
    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
    public long getElapseTime() {
        return elapseTime;
    }
    public void setElapseTime(long elapseTime) {
        this.elapseTime = elapseTime;
    }
    public boolean isStopped() {
        return stopped;
    }
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    public int getAliveThreadCount() {
        return aliveThreadCount;
    }
    public void setAliveThreadCount(int aliveThreadCount) {
        this.aliveThreadCount = aliveThreadCount;
    }

    
}
