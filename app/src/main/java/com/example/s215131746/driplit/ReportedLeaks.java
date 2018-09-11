package com.example.s215131746.driplit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Adapters.ReportedLeaksAdapter;
import viewmodels.PersonModel;
import viewmodels.ReportLeakModel;

public class ReportedLeaks extends AppCompatActivity {
    DBAccess access;
    ListView reportedLeaks;
    boolean isAdmin;
    int personID;
    private ProgressDialog progressDialog;
    ReportedLeaksAdapter leaksAdapter ;
    ArrayList<viewmodels.ReportLeakModel> leaks;
    Handler h = new Handler();
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
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);

       /* reportedLeaks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leaksAdapter.approveLeak(view,position);
            }
        });*/
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    class helpThread implements Runnable {
        Context c;
        public helpThread(Context c) {
            this.c = c;
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
                        progressDialog.dismiss();
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
}
