package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s215131746.driplit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import viewmodels.TipModel;

/**
 * Created by s216127904 on 2018/06/06.
 */

public class TipListAdapter extends BaseAdapter {
    private String[] person;
    private Date[] date;
    private String[] description;
    private LayoutInflater mInflater;
    private Context c;
    public TipListAdapter(Context context,ArrayList<TipModel> tipsList){
        c = context;
        int i =tipsList.size();
        person = new String[i];
        description = new String[i];
        date = new Date[i];
        i=0;
        for(TipModel tip :tipsList ){
            description[i] =tip.TipDescription;
            //person[i];
            person[i] = tip.FullName;
            date[i] = tip.DatePosted;
            i++;
        }
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return description.length;
    }
    @Override
    public Object getItem(int position) {
        return description[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.tiptrick_layout,null);
        TextView tipDescription = v.findViewById(R.id.tvDescription),
        tvPerson = v.findViewById(R.id.tvPostName),
        tvDate = v.findViewById(R.id.tvDate);
        SimpleDateFormat df = new SimpleDateFormat("MMM dd");
        tipDescription.setText(""+description[position]);
        tvPerson.setText("By: "+person[position]);
        tvDate.setText(df.format( date[position]));
//        LinearLayout llTipContainer = v.findViewById(R.id.llTipContainer);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0,16,8,4);
//        llTipContainer.setLayoutParams(lp);

         return v;
    }
}
