package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.R;

/**
 * Created by s215131746 on 2018/09/21.
 */


public class SnitchAdapter extends BaseAdapter {

    LayoutInflater lInflater;



    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = lInflater.inflate(R.layout.activity_snitch,null);
        TextView name = (TextView) v.findViewById(R.id.txtName);
        TextView snitchCount = (TextView) v.findViewById(R.id.txtSnitchCount);



        return v;
    }
}
