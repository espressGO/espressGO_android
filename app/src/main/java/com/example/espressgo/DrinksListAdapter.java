package com.example.espressgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DrinksListAdapter extends BaseAdapter
{
    ArrayList<DrinkListItem> listData;
    private LayoutInflater layoutInflater;
    public DrinksListAdapter(Context aContext, ArrayList<DrinkListItem> listData) {
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
            v = layoutInflater.inflate(R.layout.drinks_row, null);
            holder = new ViewHolder();
            holder.uDrink = (TextView) v.findViewById(R.id.tvDrink);
            holder.uDescription = (TextView) v.findViewById(R.id.tvDescription);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uDrink.setText(listData.get(position).getDrink());
        holder.uDescription.setText(listData.get(position).getDescription());
        return v;
    }
    static class ViewHolder {
        TextView uDrink;
        TextView uDescription;
    }
}
