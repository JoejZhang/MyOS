package com.zjz.myos.entry;

/**
 * Created by zjz on 2018/1/13.
 */

public class Job {
    private String mJobName;
    private int mJobNeed;
    private int mJobBig;

    public Job( ) {
        this("zuoye",1,1);
    }
    public Job(String jobName) {
        this(jobName,1,1);
    }

    public Job(String jobName, int jobNeed, int jobBig) {
        mJobName = jobName;
        mJobNeed = jobNeed;
        mJobBig = jobBig;
    }

    public String getJobName() {
        return mJobName;
    }

    public void setJobName(String jobName) {
        mJobName = jobName;
    }

    public int getJobNeed() {
        return mJobNeed;
    }

    public void setJobNeed(int jobNeed) {
        mJobNeed = jobNeed;
    }


    public int getJobBig() {
        return mJobBig;
    }

    public void setJobBig(int jobBig) {
        mJobBig = jobBig;
    }
}
