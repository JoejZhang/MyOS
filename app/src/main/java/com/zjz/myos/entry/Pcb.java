package com.zjz.myos.entry;

/**
 * Created by zjz on 2018/1/13.
 */

public class Pcb {
    private String mPcbName;
    private int mPcbNeed;
    private int mPcbRun;
    private int mPcbBig;
    private String status;

    public Pcb(){
        this("ah",1,1,1,"运行汇总");
    }
    public Pcb(String pcbName, int pcbNeed, int pcbRun, int pbcBig, String status) {
        mPcbName = pcbName;
        mPcbNeed = pcbNeed;
        mPcbRun = pcbRun;
        mPcbBig = pbcBig;
        this.status = status;
    }

    public String getPcbName() {
        return mPcbName;
    }

    public void setPcbName(String pcbName) {
        mPcbName = pcbName;
    }

    public int getPcbNeed() {
        return mPcbNeed;
    }

    public void setPcbNeed(int pcbNeed) {
        mPcbNeed = pcbNeed;
    }

    public int getPcbRun() {
        return mPcbRun;
    }

    public void setPcbRun(int pcbRun) {
        mPcbRun = pcbRun;
    }

    public int getPcbBig() {
        return mPcbBig;
    }

    public void setPcbBig(int pcbBig) {
        mPcbBig = pcbBig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
