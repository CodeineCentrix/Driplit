package com.example.s215131746.driplit;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ReportedLeaksAdapter;
import viewmodels.PersonModel;

public class ReportedLeaks extends AppCompatActivity {
    TabMenu.DBAccess access;
    TabMenu.GeneralMethods method;
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

        access = new TabMenu.DBAccess();


        ArrayList<viewmodels.ReportLeakModel> leaks;

        //Trying to link the properties in the GetReportedLeaks/ReportedLeak class to the Adapter to display.
        TabMenu.GeneralMethods m = new TabMenu.GeneralMethods(reportedLeaks.getContext());
        boolean isAdmin = m.Read("person.txt",",")[PersonModel.ISAMDIN].equals("true");
        if(isAdmin){
            leaks = access.GetAdminReportedLeaks();
        }else {
            leaks = access.GetReportedLeaks();
        }
        final ReportedLeaksAdapter leaksAdapter = new ReportedLeaksAdapter(this, leaks,isAdmin);
        reportedLeaks.setAdapter(leaksAdapter);

        reportedLeaks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leaksAdapter.approveLeak(view,position);
            }
        });
    }
}
