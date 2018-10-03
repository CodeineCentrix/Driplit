package Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.s215131746.driplit.DBAccess;
import com.example.s215131746.driplit.GeneralMethods;
import com.example.s215131746.driplit.R;

import java.util.ArrayList;

import viewmodels.ReportLeakModel;

/**
 * Created by s215131746 on 2018/08/11.
 */

public class ReportedLeaksAdapter extends BaseAdapter {
    Switch[] switches;
    ImageView[] leakpic;
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
        switches = new Switch[i];
        leak = l;
        i=0;
        leakpic = new ImageView[l.size()];
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.activity_reported_leaks, null);
        Animation anime = new TranslateAnimation(360f,5f,500f,5f);
        anime.setDuration(3000);
        Animation anime2 = new TranslateAnimation(-360f,5f,-500f,1f);
        anime2.setDuration(3000);
        TextView longitude =v.findViewById(R.id.LeakAddress);
        LinearLayout linear_reported_leaks = v.findViewById(R.id.linear_reported_leaks);
        if(position%2==0) {
            linear_reported_leaks.setAnimation(anime);
        }else {
            linear_reported_leaks.setAnimation(anime2);
        }
        TextView day =  v.findViewById(R.id.LeakDate);
        leakpic[position] = v.findViewById(R.id.imageView);
        if(leak.get(position).image!=null){
            leakpic[position].setImageBitmap(leak.get(position).image);
        }
        java.sql.Date leakDate = date[position];
        approve[position] = v.findViewById(R.id.view);
         switches[position] = v.findViewById(R.id.switch1);
         switches[position].setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 approveLeak(v,position);
             }
         });
        if(!isAdmin){

            switches[position].setVisibility(View.GONE);
        }else {

        }
        if(leak.get(position).status==0){
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.red));
        } else if(leak.get(position).status==1) {
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.green));
            switches[position].setChecked(true);
        }
        longitude.setText(leak.get(position).Location);
        day.setText(""+leakDate);
        return v;
    }

    public int approveLeak(int position){
        if(!switches[position].isChecked()){
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.red));
            return leak.get(position).status=0;

        }else{
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.green));
            return leak.get(position).status=1;
        }
    }
    public void approveLeak(View v, final int position ){
        final GeneralMethods m = new GeneralMethods(v.getContext());
        m.writeToFile("do","approve.txt");
        final int duration = 3000;
        approveLeak(position);
        final DBAccess business = new DBAccess();
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
