package com.example.s215131746.driplit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.ReportedLeaksAdapter;
import viewmodels.PersonModel;
import viewmodels.ReportLeakModel;
import viewmodels.UspMobGetPersonItemTotal;

public class ReportedLeaks extends AppCompatActivity {
    public static final int FIXED = 1;
    public static final int WAITING = 2;
    public static final int WITHPIC = 3;
    public static final int TODAY = 4;
    DBAccess access;
    ListView reportedLeaks;
    boolean isAdmin;
    int personID;
    private ProgressDialog progressDialog;
    ReportedLeaksAdapter leaksAdapter ;
    ArrayList<viewmodels.ReportLeakModel> leaks;
    Handler h = new Handler();
    private ProgressBar bar;
    CalendarView cvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reported_leaks_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //code for back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        cvDate  = findViewById(R.id.cvDate);

        bar = findViewById(R.id.progressBar2);
        reportedLeaks = (ListView)findViewById(R.id.reportedLeaks);
        access = new DBAccess();
        Bundle tab = getIntent().getExtras();
        personID = Integer.parseInt(tab.getString("personID"));
        //Trying to link the properties in the GetReportedLeaks/ReportedLeak class to the Adapter to display.
        GeneralMethods m = new GeneralMethods(reportedLeaks.getContext());
        isAdmin = m.Read(this.getString(R.string.person_file_name),",")[PersonModel.ISAMDIN].equals("true");
        progressDialog = new ProgressDialog(ReportedLeaks.this,
                R.style.Theme_AppCompat_Dialog);

        helpThread r = new helpThread(this);
        new Thread(r).start();
       // progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);

       /* reportedLeaks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leaksAdapter.approveLeak(view,position);
            }
        });*/
        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {



                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateS = df.format(calendar.getTime());
                ArrayList<ReportLeakModel> today = new ArrayList<>();
                for (ReportLeakModel reported:leaks){
                    if(reported.date.toString().equals(dateS)){
                        today.add(reported);
                        TextView txtNoLeak = findViewById(R.id.txtNoLeak);
                        txtNoLeak.setVisibility(View.INVISIBLE);
                    }
                }
                if(today.size()>0){
                    onCallBackGround(today);
                    cvDate.setVisibility(View.INVISIBLE);
                    reportedLeaks.setVisibility(View.VISIBLE);
                }else {
                    reportedLeaks.setVisibility(View.INVISIBLE);
                    TextView txtNoLeak = findViewById(R.id.txtNoLeak);
                    txtNoLeak.setVisibility(View.VISIBLE);
                    txtNoLeak.setText("NO LEAKS RECORDED");
                }

            }
        });
        cvDate.setMaxDate(cvDate.getDate());
        cvDate.setVisibility(View.INVISIBLE);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(leaks!=null)
        switch (item.getItemId()){
           case R.id.action_fixed:
               ArrayList<ReportLeakModel> fixed = new ArrayList<>();
               for (ReportLeakModel reported:leaks){
                   if(reported.status==1){
                       fixed.add(reported);
                   }
               }

               onCallBackGround(fixed);
               break;
           case R.id.action_waiting:
               ArrayList<ReportLeakModel> waiting = new ArrayList<>();
               for (ReportLeakModel reported:leaks){
                   if(reported.status==0){
                       waiting.add(reported);
                   }
               }
               onCallBackGround(waiting);
               break;
           case R.id.action_withPic:
               ArrayList<ReportLeakModel> withPic = new ArrayList<>();
               for (ReportLeakModel reported:leaks){
                   if(reported.picPath!=null){
                       withPic.add(reported);
                   }
               }
               onCallBackGround(withPic);
               break;
           case R.id.action_today:
               SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
               String date = df.format(Calendar.getInstance().getTime());
               ArrayList<ReportLeakModel> today = new ArrayList<>();
               for (ReportLeakModel reported:leaks){
                   if(reported.date.toString().equals(date)){
                       today.add(reported);
                   }
               }
               onCallBackGround(today);
               break;
            case R.id.action_selectDay:
                cvDate.setVisibility(View.VISIBLE);
                reportedLeaks.setVisibility(View.INVISIBLE);
                break;
            case R.id.action_all:
                onCallBackGround(leaks);
                break;
           default:
               finish();
               break;
       }

        return super.onOptionsItemSelected(item);
    }


    public void onCallBackGround(ArrayList<ReportLeakModel> newLeaks ){
        if(leaks.size()>0) {
            leaksAdapter = new ReportedLeaksAdapter(this, newLeaks, isAdmin);
            reportedLeaks.setAdapter(leaksAdapter);
            cvDate.setVisibility(View.INVISIBLE);
            reportedLeaks.setVisibility(View.VISIBLE);
            TextView txtNoLeak = findViewById(R.id.txtNoLeak);
            txtNoLeak.setVisibility(View.INVISIBLE);
        }else {
            TextView txtNoLeak = findViewById(R.id.txtNoLeak);
            txtNoLeak.setVisibility(View.VISIBLE);
            txtNoLeak.setText("NO LEAKS RECORDED");
        }
    }
    class helpThread implements Runnable {
        Context c;
        int id;
        public helpThread(Context c) {
            this.c = c;
        }
        public helpThread(Context c,int id) {
            this.c = c;
            this.id = id;
        }

        @Override
        public void run() {

            try {
                if(isAdmin){
                    leaks = access.GetAdminReportedLeaks();
                }else {
                    leaks = access.GetReportedLeaks(personID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            h.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        bar.setVisibility(View.GONE);
                        if(leaks.size()>0) {
                            leaksAdapter = new ReportedLeaksAdapter(c, leaks, isAdmin);
                            reportedLeaks.setAdapter(leaksAdapter);
                        }else {
                            TextView txtNoLeak = findViewById(R.id.txtNoLeak);
                            txtNoLeak.setVisibility(View.VISIBLE);
                            txtNoLeak.setText("NO LEAKS RECORDED");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_tab, menu);
        return true;
    }
}
