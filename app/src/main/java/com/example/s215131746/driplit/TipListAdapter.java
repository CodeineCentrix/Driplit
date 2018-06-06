package com.example.s215131746.driplit;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by s216127904 on 2018/06/06.
 */

public class TipListAdapter extends BaseAdapter {
    String[] tip;
    public TipListAdapter()
    {

    }
    @Override
    public int getCount() {
        return tip.length;
    }

    @Override
    public Object getItem(int position) {
        return tip[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        return null;
    }
}
