package Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.R;
import com.example.s215131746.driplit.TabMenu;

import java.util.ArrayList;
import java.util.Date;

import viewmodels.ReportLeakModel;
import viewmodels.ReportedLeaks;

/**
 * Created by s215131746 on 2018/08/11.
 */

public class ReportedLeaksAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] location;
    ArrayList<ReportLeakModel> leak;
    java.sql.Date[] date;
    View approve[];
    boolean isAdmin;
    Context context;
    public ReportedLeaksAdapter(Context c, ArrayList<ReportLeakModel> l, boolean isAdmin)
    {
        this.isAdmin = isAdmin;
        context = c;
        int i = l.size();
        location = new String[i];
        date = new java.sql.Date[i];
        leak = l;
        i=0;
        approve = new View[l.size()];
        for(ReportLeakModel leak :l )
        {
            location[i] = leak.Location;
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
        TextView longitude =(TextView) v.findViewById(R.id.LeakAddress);
        TextView day = (TextView) v.findViewById(R.id.LeakDate);

        String loc = location[position];
        String lon = location[position];
        java.sql.Date leakDate = date[position];
        approve[position] = v.findViewById(R.id.view);
        if(!isAdmin){
            approve[position].setVisibility(View.GONE);
        }else if(leak.get(position).status==0){
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.red));
        } else if(leak.get(position).status==1) {
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.green));
        }
        //address.setText(loc);
        longitude.setText(leak.get(position).Location);
        day.setText(""+leakDate);
        return v;
    }

    public int approveLeak(int position){
        if(leak.get(position).status==1){
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.red));
            return leak.get(position).status=0;
        }else{
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.green));
            return leak.get(position).status=1;
        }
    }
    public void approveLeak(View v, final int position ){
        final TabMenu.GeneralMethods m = new TabMenu.GeneralMethods(v.getContext());
        m.writeToFile("do","approve.txt");
        final int duration = 3000;
        approveLeak(position);
        final TabMenu.DBAccess business = new TabMenu.DBAccess();
        Handler h = new Handler();
        Snackbar mySnackbar = Snackbar.make(v,"Leak Fixed", duration);
        mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.writeToFile("undo","approve.txt");
                approveLeak(position);
            }


        });
        mySnackbar.show();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(m.readFromFile("approve.txt").equals("do")){
                    business.MobApproveLeak(leak.get(position));
                }

            }
            //1 second wait before saving to the database
        },duration);

    }
}
