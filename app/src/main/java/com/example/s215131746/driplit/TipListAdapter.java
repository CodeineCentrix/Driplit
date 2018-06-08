package com.example.s215131746.driplit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import viewmodels.GetTip;

/**
 * Created by s216127904 on 2018/06/06.
 */

public class TipListAdapter extends BaseAdapter {

    private int count = getCount();
    private String person;
    private Date date;
    private String description;

    private String[] tips;
    private LayoutInflater mInflater;
    private Context context;
    public TipListAdapter(Context context)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return tips[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mInflater.inflate(R.layout.tip_trick, null);
        DBAccess business = new DBAccess();
        TextView person, datePosted, tipDescription;
        ListView lvPostedTips = view.findViewById(R.id.lvPostedTips);
        ArrayList<TipModel> tipsTricks = new ArrayList<>();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (TipModel tip:tipsTricks)
        {
            //finding control ID's
            person =  view.findViewById(R.id.tvPostName);
            datePosted =  view.findViewById(R.id.tvDate);
            tipDescription = view.findViewById(R.id.tvDescription);

            //Getting positions
            String tipPos = tips[position];

            //Assigning values to controls
            person.setText("Shervin");
            datePosted.setText(null);
            tipDescription.setText(tip.TipDescription);
        }


        //this.getLayoutInflater().inflate(R.layout.tiptrick_layout,null);

         return view;
    }
}
