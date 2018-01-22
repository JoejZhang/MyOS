package com.zjz.myos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zjz.myos.adapters.JobListAdapter;
import com.zjz.myos.adapters.MemoryAdapter;
import com.zjz.myos.adapters.PcbListAdapter;
import com.zjz.myos.entry.Job;
import com.zjz.myos.entry.Memory;
import com.zjz.myos.entry.Pcb;
import com.zjz.myos.view.CreateJobActivity;
import com.zjz.myos.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_add_job)
    Button mBtnAddJob;
    @BindView(R.id.btn_begin)
    Button mBtnBegin;
    @BindView(R.id.btn_pause)
    Button mBtnPause;
    @BindView(R.id.lvfs_job)
    ListViewForScrollView mLvfsJob;
    @BindView(R.id.lvfs_pbc)
    ListViewForScrollView mLvfsPcb;
    @BindView(R.id.tb_main)
    Toolbar mToolbar;
    @BindView(R.id.lv_memory)
    ListView mlvMemory;

    private JobListAdapter mJobListAdapter;
    private ArrayList<Job> mJobArrayList;
    private PcbListAdapter mPcbListAdapter;
    private ArrayList<Pcb> mPcbs;

    private MemoryAdapter mMemoryAdapter;
    private ArrayList<Memory> mMemories ;//空闲分区表

    private int TIME = 1000;
    private int SIZE = 10;//不可再切割的最小内存大小

    private int runningPosition = -1;//正在运行的进程编号
    private boolean isRunning = false;//是否正在运行
    private boolean isBegin = false;//是否已经开始运行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intiData();
    }

    private void intiData() {
        mToolbar.setTitle("操作系统");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        Memory m = new Memory(0,200,"",false);
        mMemories = new ArrayList<>();
        mMemories.add(m);//初始化空闲列表
        mMemoryAdapter = new MemoryAdapter(this,mMemories);
        mlvMemory.setAdapter(mMemoryAdapter);

        mJobArrayList = new ArrayList<>();
        mJobListAdapter = new JobListAdapter(this, mJobArrayList);
        mLvfsJob.setAdapter(mJobListAdapter);

        mPcbs = new ArrayList<>();
        mPcbListAdapter = new PcbListAdapter(this,mPcbs);
        mLvfsPcb.setAdapter(mPcbListAdapter);

    }

    //点击事件
    @OnClick({R.id.btn_add_job, R.id.btn_begin, R.id.btn_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_job:
                addJob();
                break;
            case R.id.btn_begin:
                beginRun();
                break;
            case R.id.btn_pause:
                pauseOrRun();
                break;
        }
    }

    //添加作业，运行中的话需要暂停运行
    private void addJob() {
        if(!isRunning){
            startActivityForResult(new Intent(this, CreateJobActivity.class), 0);
        }
        else{
            showToast("正在运行中");
        }
    }

    //暂停或者继续
    private void pauseOrRun() {
        if(!isBegin){
            showToast("请先开始运行");
        }
        else{
            if(isRunning){
                //暂停
                isRunning = false;
                mBtnPause.setText("继续");
            }
            else{
                //开始
                isRunning = true;
                mBtnPause.setText("暂停");
            }
        }
    }

    //开始执行
    private void beginRun() {
        if(!isBegin){
            isBegin = true;
            isRunning = true;
            mBtnBegin.setText("清空所有重来");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(TIME);//设置停顿时间，模拟时间片运转

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(isRunning) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //作业调度，先来服务
                                    int flagPosition = runningPosition;

                                    while (mJobArrayList.size() > 0 && mPcbs.size() < 4) {//有作业且进程数量小于4
                                        Pcb pcb = new Pcb();
                                        Job job = mJobArrayList.get(0);

                                        if (canUes(job)) {//作业能够调度
                                            pcb.setPcbName(job.getJobName());
                                            pcb.setPcbNeed(job.getJobNeed());
                                            pcb.setPcbRun(0);
                                            pcb.setPcbBig(job.getJobBig());
                                            pcb.setStatus("等待");
                                            if (runningPosition == -1) {//没有正在运行的进程
                                                mPcbs.add(pcb);//运行第一个进程
                                            } else {//有正在运行的进程
                                                ArrayList<Pcb> list = new ArrayList<Pcb>();
                                                for (int i = 0; i < flagPosition; i++) {
                                                    list.add(mPcbs.get(i));
                                                }
                                                list.add(pcb);
                                                for (int i = flagPosition; i < mPcbs.size(); i++) {
                                                    list.add(mPcbs.get(i));
                                                }
                                                mPcbs = list;
                                                runningPosition = (runningPosition + 1) % 4;//插入新的进程之后正在运行的进程就换位了
                                            }

                                            mJobArrayList.remove(0);//调入最先进入的
                                            mPcbListAdapter.updateList(mPcbs);//刷新视图
                                            mJobListAdapter.updateList(mJobArrayList);
                                            mMemoryAdapter.updateList(mMemories);
                                        } else {
                                            break;
                                        }
                                    }

                                    //进程调度，时间片轮转
                                    if (mPcbs.size() > 0) {//有进程
                                        if (runningPosition == -1) {//没有程序在运行中
                                            mPcbs.get(0).setStatus("运行中");
                                            runningPosition = 0;
                                        } else {
                                            Pcb pcb = mPcbs.get(runningPosition);
                                            if (pcb.getPcbNeed() - pcb.getPcbRun() <= 1) {//这一次可以运行完

                                                releaseMemory(pcb);//释放内存

                                                if (mPcbs.size() > 1) {//执行完这个进程后如果还有进程
                                                    mPcbs.get((runningPosition + 1) % mPcbs.size()).setStatus("运行中");
                                                }
                                                mPcbs.remove(runningPosition);

                                                if (mPcbs.size() == 0) {//执行完这个进程后如果没有进程了
                                                    runningPosition = -1;
                                                } else {
                                                    runningPosition = runningPosition % mPcbs.size();
                                                }

                                            } else {//这个进程还没有运行完
                                                mPcbs.get(runningPosition).setPcbRun(mPcbs.get(runningPosition).getPcbRun() + 1);
                                                if (mPcbs.size() > 1) {//如果有多的进程
                                                    mPcbs.get(runningPosition).setStatus("等待中");
                                                    runningPosition = (runningPosition + 1) % (mPcbs.size());
                                                    mPcbs.get(runningPosition).setStatus("运行中");
                                                }

                                            }
                                        }

                                        mPcbListAdapter.updateList(mPcbs);//更新视图
                                    }//有进程
                                }

                                //回收内存算法
                                private void releaseMemory(Pcb pcb) {
                                    int memoryPosition = -1;
                                    for (int i = 0; i < mMemories.size(); i++) {
                                        if (mMemories.get(i).getName().equals(pcb.getPcbName())) {
                                            memoryPosition = i;
                                            break;
                                        }
                                    }
                                    boolean front = false;//标记前一块区域是否空闲
                                    boolean back = false;//标记后一块区域是否空闲
                                    if (memoryPosition > 0 && !mMemories.get(memoryPosition - 1).isBusy()) {//前一块是空闲的
                                        front = true;
                                    }
                                    if (memoryPosition < mMemories.size() - 1 && !mMemories.get(memoryPosition + 1).isBusy()) {//后一块是空闲的
                                        back = true;
                                    }
                                    if (front && !back) {//只有前一块是空闲的
                                        mMemories.get(memoryPosition - 1).setBig(mMemories.get(memoryPosition - 1).getBig() + mMemories.get(memoryPosition).getBig());
                                        mMemories.get(memoryPosition - 1).setBusy(false);
                                        mMemories.remove(memoryPosition);
                                    } else if (front && back) {//前后都是空闲的

                                        mMemories.get(memoryPosition - 1).setBig(mMemories.get(memoryPosition - 1).getBig() + mMemories.get(memoryPosition).getBig() + mMemories.get(memoryPosition + 1).getBig());
                                        mMemories.get(memoryPosition - 1).setBusy(false);
                                        Iterator<Memory> iterator = mMemories.iterator();
                                        int i = 0;
                                        while (iterator.hasNext()) {
                                            iterator.next();
                                            if (i == memoryPosition || i == memoryPosition + 1) {
                                                iterator.remove();
                                            }
                                            i++;
                                        }
                                    } else if (!front && back) {//只有后一块是空闲的
                                        mMemories.get(memoryPosition).setBig(mMemories.get(memoryPosition + 1).getBig() + mMemories.get(memoryPosition).getBig());
                                        mMemories.get(memoryPosition).setBusy(false);
                                        mMemories.remove(memoryPosition + 1);
                                    } else {//前后都是已分配的
                                        mMemories.get(memoryPosition).setBusy(false);
                                    }
                                    mMemoryAdapter.updateList(mMemories);
                                }

                                //首次适应算法，检查是否可以分配内存
                                private boolean canUes(Job job) {
                                    int need = job.getJobBig();//作业需要的内存大小
                                    int position = -1;//适应的位置
                                    for (int i = 0; i < mMemories.size(); i++) {//寻找适应的位置
                                        if (need <= mMemories.get(i).getBig() && !mMemories.get(i).isBusy()) {
                                            position = i;
                                            break;
                                        }
                                    }
                                    if (position == -1) {//不能分配
                                        return false;
                                    }
                                    ArrayList<Memory> m = new ArrayList<>();//新的空闲分区表
                                    for (int i = 0; i < position; i++) {
                                        m.add(mMemories.get(i));
                                    }
                                    Memory memory = mMemories.get(position);

                                    if (memory.getBig() - need <= SIZE) {//不可再分割
                                        memory.setBusy(true);//改为繁忙
                                        memory.setName(job.getJobName());
                                        m.add(memory);
                                    } else {
                                        Memory memory1 = new Memory(memory.getBegin() + need, memory.getBig() - need, "", false);
                                        memory.setBusy(true);//改为繁忙
                                        memory.setName(job.getJobName());
                                        memory.setBig(need);
                                        m.add(memory);
                                        m.add(memory1);
                                    }
                                    for (int i = position + 1; i < mMemories.size(); i++) {
                                        m.add(mMemories.get(i));
                                    }
                                    mMemories = m;

                                    return true;
                                }
                            });
                        }
                    }
                }
            }).start();
        }
        else{
            //清空所有作业和进程
            mJobArrayList.clear();
            mPcbs.clear();
            mMemories.clear();
            runningPosition = -1;
            isBegin = false;
            isRunning = false;

            Memory m = new Memory(0,200,"",false);
            mMemories.add(m);//初始化空闲列表

            mJobListAdapter.updateList(mJobArrayList);
            mPcbListAdapter.updateList(mPcbs);
            mMemoryAdapter.updateList(mMemories);
            mBtnPause.setText("暂停");
            mBtnBegin.setText("开始运行");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ((requestCode)) {
            case 0:if(resultCode == RESULT_OK){
                Job job = new Job(data.getStringExtra("JobName"),data.getIntExtra("JobNeed",0),data.getIntExtra("JobBig",0));
                mJobArrayList.add(job);
                mJobListAdapter.updateList(mJobArrayList);
            }
                break;
            default:
                break;
        }
    }

    private void showToast(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
