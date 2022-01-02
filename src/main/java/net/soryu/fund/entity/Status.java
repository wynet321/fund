package net.soryu.fund.entity;

public class Status {
    
    private int leftCount;
    private long elapseTime;
    private boolean stopped;
    private int aliveThreadCount;
    
    
    public int getLeftCount() {
        return leftCount;
    }
    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
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
