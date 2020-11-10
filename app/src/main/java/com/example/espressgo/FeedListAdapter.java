package com.example.espressgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter
{
    ArrayList<FeedListItem> listData;
    private LayoutInflater layoutInflater;
    public FeedListAdapter(Context aContext, ArrayList<FeedListItem> listData) {
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
            v = layoutInflater.inflate(R.layout.feed_row, null);
            holder = new ViewHolder();
            holder.uEmail = (TextView) v.findViewById(R.id.tvEmail);
            holder.uShop = (TextView) v.findViewById(R.id.tvShop);
            holder.uDrink = (TextView) v.findViewById(R.id.tvDrink);
            holder.uComment = (TextView) v.findViewById(R.id.tvComment);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uEmail.setText(listData.get(position).getEmail());
        holder.uShop.setText(listData.get(position).getShop());
        if (listData.get(position).getDrink() != null)
            holder.uDrink.setText(listData.get(position).getDrink());
        holder.uComment.setText(listData.get(position).getComment());
        return v;
    }
    static class ViewHolder {
        TextView uEmail;
        TextView uShop;
        TextView uDrink;
        TextView uComment;
    }
}

