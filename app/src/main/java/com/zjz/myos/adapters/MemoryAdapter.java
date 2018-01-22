package com.zjz.myos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjz.myos.R;
import com.zjz.myos.entry.Memory;
import com.zjz.myos.entry.Pcb;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjz on 2018/1/13.
 */

public class MemoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Memory> mList;

    public MemoryAdapter(Context context, ArrayList<Memory> list) {
        mContext = context;
        mList = list;
    }

    public void updateList(ArrayList<Memory> list) {
        mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_memory, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }

            Memory memory= mList.get(position);

        if(memory.isBusy()){
            viewHolder.mTvMemoryBig.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            viewHolder.mTvMemoryBig.setTextColor(mContext.getResources().getColor(R.color.white));
            String text ="已分配  "+ memory.getName() + " :  "+ String.valueOf(memory.getBig()+"内存");
            viewHolder.mTvMemoryBig.setText(text);
        }
        else{
            viewHolder.mTvMemoryBig.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvMemoryBig.setTextColor(mContext.getResources().getColor(R.color.textBlack));
            viewHolder.mTvMemoryBig.setText("空闲   "+memory.getBig()+"内存");
        }

        return convertView;
    }

     class ViewHolder {
        @BindView(R.id.tv_memory_big)
        TextView mTvMemoryBig;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
