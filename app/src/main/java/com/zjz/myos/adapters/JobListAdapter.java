package com.zjz.myos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjz.myos.R;
import com.zjz.myos.entry.Job;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjz on 2018/1/13.
 */

public class JobListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Job> mList;

    public JobListAdapter(Context context, ArrayList<Job> list) {
        mContext = context;
        mList = new ArrayList<Job>();
        mList.add(new Job());//标题
        mList.addAll(list);
    }

    public void updateList(ArrayList<Job> list){
        mList.clear();
        mList.add(new Job());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_job, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
            if(position == 0){
            viewHolder.mTvJobName.setText( "作业名");
            viewHolder.mTvJobNeed.setText("作业完成所需时间(s)");
            viewHolder.mTvJobBig.setText(("占用内存大小"));
        }
        else{
            Job job = mList.get(position);
            viewHolder.mTvJobName.setText(job.getJobName());
            viewHolder.mTvJobNeed.setText(String.valueOf(job.getJobNeed()));
            viewHolder.mTvJobBig.setText(String.valueOf(job.getJobBig()));

        }
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.tv_job_name)
        TextView mTvJobName;
        @BindView(R.id.tv_job_need)
        TextView mTvJobNeed;
        @BindView(R.id.tv_job_big)
        TextView mTvJobBig;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
