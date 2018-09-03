package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.R;

import java.util.ArrayList;

import viewmodels.Surburb;

/**
 * Created by s216127904 on 2018/09/02.
 */

public class SinnerAdapter extends BaseAdapter {
    private ArrayList<Surburb> burbOrCity;
    private LayoutInflater mInflater;
    public SinnerAdapter(ArrayList<Surburb> burbOrCity, Context c){
        this.burbOrCity = burbOrCity;
        mInflater =(LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return burbOrCity.size();
    }

    @Override
    public Object getItem(int position) {
        return burbOrCity.get(position).ID;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.water_intake_item_layout,null);
        final TextView tvItemName = v.findViewById(R.id.tvItemName);
        tvItemName.setText(burbOrCity.get(position).SurburbName);
        return v;
    }
}
