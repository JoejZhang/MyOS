package com.zjz.myos.entry;

/**
 * Created by zjz on 2018/1/13.
 */

public class Memory {
    private int mBegin;
    private int mBig;
    private String mName;
    private boolean isBusy;

    public Memory(int begin, int big, String name, boolean isBusy) {
        mBegin = begin;
        mBig = big;
        mName = name;
        this.isBusy = isBusy;
    }

    public int getBegin() {
        return mBegin;
    }

    public void setBegin(int begin) {
        mBegin = begin;
    }

    public int getBig() {
        return mBig;
    }

    public void setBig(int big) {
        mBig = big;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}
