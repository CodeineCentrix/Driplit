package com.example.s215131746.driplit;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ReportedLeaksAdapter;

public class ReportedLeaks extends AppCompatActivity {
    DBAccess access;
    GeneralMethods method;
    ListView reportedLeaks;
    String[] location;
    Double[] longitude;
    Double[] latitude;
    String[] date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reported_leaks_list);

        Resources res = getResources();
        reportedLeaks = (ListView)findViewById(R.id.reportedLeaks);

        access = new DBAccess();


        ArrayList<viewmodels.ReportLeakModel> leaks = access.GetReportedLeaks();

        //Trying to link the properties in the GetReportedLeaks/ReportedLeak class to the Adapter to display.

        ReportedLeaksAdapter leaksAdapter = new ReportedLeaksAdapter(this, leaks);
        reportedLeaks.setAdapter(leaksAdapter);
    }
}
