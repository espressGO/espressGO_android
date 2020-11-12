package com.example.espressgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FollowListAdapter extends BaseAdapter
{
    ArrayList<FollowListItem> listData;
    private LayoutInflater layoutInflater;
    public FollowListAdapter(Context aContext, ArrayList<FollowListItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.follow_row, null);
            holder = new ViewHolder();
            holder.uDisplayName = (TextView) v.findViewById(R.id.tvDisplayName);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uDisplayName.setText(listData.get(position).getDisplayName());
        return v;
    }
    static class ViewHolder {
        TextView uDisplayName;
    }
}
