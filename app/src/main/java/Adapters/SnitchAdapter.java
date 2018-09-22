package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.R;
import com.example.s215131746.driplit.SnitchActivity;

import java.util.ArrayList;

import viewmodels.uspMobGetSnitches;

/**
 * Created by s215131746 on 2018/09/21.
 */


public class SnitchAdapter extends BaseAdapter {

    LayoutInflater lInflater;
    ArrayList<uspMobGetSnitches> snitches;
    public SnitchAdapter(Context c, ArrayList<uspMobGetSnitches> snitches){
        this.snitches = snitches;

        lInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return snitches.size();
    }

    @Override
    public Object getItem(int position) {
        return snitches.get(position).FullName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = lInflater.inflate(R.layout.activity_snitch,null);
        TextView name = (TextView) v.findViewById(R.id.txtName);
        TextView snitchCount = (TextView) v.findViewById(R.id.txtSnitchCount);

        name.setText(snitches.get(position).FullName);
        snitchCount.setText(""+snitches.get(position).TotalSnitches);

        return v;
    }
}
