package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.R;

import java.util.ArrayList;
import java.util.Date;

import viewmodels.ReportedLeaks;

/**
 * Created by s215131746 on 2018/08/11.
 */

public class ReportedLeaksAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] location;
    java.sql.Date[] date;

    public ReportedLeaksAdapter(Context c, ArrayList<ReportedLeaks> l)
    {

        int i = l.size();
        location = new String[i];
        date = new java.sql.Date[i];

        i=0;
        for(ReportedLeaks leak :l )
        {
            location[i] = leak.location;
            date[i] = (java.sql.Date) leak.date;
            i++;
        }
        //location = l;
        //date = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return location.length;
    }

    @Override
    public Object getItem(int position) {
        return location[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.activity_reported_leaks, null);
        TextView address =(TextView) v.findViewById(R.id.LeakAddress);
        TextView day = (TextView) v.findViewById(R.id.LeakDate);

        String loc = location[position];
        java.sql.Date ldate = date[position];

        address.setText(loc);
        day.setText((CharSequence) ldate);
        return v;
    }
}
