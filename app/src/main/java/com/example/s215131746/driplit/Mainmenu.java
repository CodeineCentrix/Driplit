package com.example.s215131746.driplit;

import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

public class Mainmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);




    }
    //Trying to link onClick of EditProfile button to Fragment EditProfile to display EditProfile intent.
    public void EditProfileScreen(View view)
    {
       // TabLayout menu = findViewById(R.id.tabs);
       // TabItem ti_editProfile = findViewById(R.id.tiEditProfile);
       // int i = menu.getTabCount();
       // menu.getChildAt(i);
//
       // Fragment editProfile = null;
       // editProfile.getFragmentManager().findFragmentById(R.id.tiEditProfile);
       // editProfile.getActivity().getIntent();
        Intent showEditProfile = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showEditProfile,"Edit");
    }
    public void TrendScreen(View view)
    {
        Intent showTrend = new Intent(getApplicationContext(),TabMenu.class);
        showTrend.putExtras(getIntent().getBundleExtra("emailAddress"));
        SetBundel(showTrend,"Tend");
    }
    public void TipScreen(View view)
    {
        Intent showTips = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showTips,"Tips");
    }
    public void RecordScreen(View view)
    {
        Intent showRecord = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showRecord,"Record");
    }
    public void ReportScreen(View view)
    {
        Intent showReport = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showReport,"Report");
    }
    public void FactOfTheDayScreen(View view)
    {
        Intent showFOD = new Intent(getApplicationContext(),FODScreen.class);
        startActivity(showFOD);
    }
    public void SetBundel(Intent in,String tabName)
    {

        Bundle i = new Bundle();
        i.putString("Tab", tabName);
        in.putExtras(i);
        startActivity(in);
    }

}
