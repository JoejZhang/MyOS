package com.zjz.myos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjz.myos.R;
import com.zjz.myos.entry.Pcb;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjz on 2018/1/13.
 */

public class PcbListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Pcb> mList;

    public PcbListAdapter(Context context, ArrayList<Pcb> list) {
        mContext = context;
        mList = new ArrayList<Pcb>();
        mList.add(new Pcb());//标题
        mList.addAll(list);
    }

    public void updateList(ArrayList<Pcb> list) {
        mList.clear();
        mList.add(new Pcb());
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pcb, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        if (position == 0) {
            viewHolder.mTvPcbName.setText("进程名");
            viewHolder.mTvPcbNeed.setText("进程完成\n所需时间（s）");
            viewHolder.mTvPcbRun.setText("已运行时间（s）");
            viewHolder.mTvPcbBig.setText("占用内存大小");
            viewHolder.mTvPcbStatus.setText("运行状态");
        } else {
            Pcb pcb = mList.get(position);
            viewHolder.mTvPcbName.setText(pcb.getPcbName());
            viewHolder.mTvPcbNeed.setText(String.valueOf(pcb.getPcbNeed()));
            viewHolder.mTvPcbRun.setText(String.valueOf(pcb.getPcbRun()));
            viewHolder.mTvPcbBig.setText(String.valueOf(pcb.getPcbBig()));
            viewHolder.mTvPcbStatus.setText(pcb.getStatus());
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_pcb_name)
        TextView mTvPcbName;
        @BindView(R.id.tv_pcb_need)
        TextView mTvPcbNeed;
        @BindView(R.id.tv_pcb_run)
        TextView mTvPcbRun;
        @BindView(R.id.tv_pcb_big)
        TextView mTvPcbBig;
        @BindView(R.id.tv_pcb_status)
        TextView mTvPcbStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
