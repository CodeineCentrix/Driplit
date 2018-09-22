package com.example.s215131746.driplit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Adapters.SnitchAdapter;
import viewmodels.ReportLeakModel;
import viewmodels.uspMobGetSnitches;

public class SnitchActivity extends AppCompatActivity {

    private CalendarView cvDate;
    private DBAccess business;
    private uspMobGetSnitches snitchSetting;
    private ListView snitchList;
    private long date;
    private java.util.Date time;
    private TextView tvFrom,tvTo;
    private EditText top;
    SnitchAdapter snitch ;
    ArrayList<uspMobGetSnitches> snitches;
    int CODE,CODEFROM=1,CODETO=2;
    private View focusTo,focusFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snitch_list_layout);
        focusFrom = findViewById(R.id.vFrom);
        focusTo = findViewById(R.id.vTO);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        Foucs(0);
        top = findViewById(R.id.txtTop);
        top.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")&&snitches!=null){
                    snitchSetting.top = Integer.parseInt(s.toString());
                    ArrayList<uspMobGetSnitches> relodaSsnitches = new ArrayList<>();
                    for(int i = 0;i<snitchSetting.top && i< snitches.size();i++){
                        relodaSsnitches.add(snitches.get(i));
                    }
                    snitch = new SnitchAdapter(tvFrom.getContext(),relodaSsnitches);
                    snitchList.setAdapter(snitch);
                }
            }
        });

        //code for back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        business = new DBAccess();

        cvDate  = findViewById(R.id.cvDate);
        boolean clickable = cvDate.isClickable();
        snitchList = (ListView)findViewById(R.id.lstSnitchList);
        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Foucs(CODE);
                if(CODE == CODEFROM){
                    snitchSetting.from = new java.sql.Date(calendar.getTime().getTime());
                    tvFrom.setText("FROM: " + snitchSetting.from);

                }else {
                    if (CODE == CODETO) {
                        snitchSetting.to = new java.sql.Date(calendar.getTime().getTime());
                        snitches = business.MobGetSnitches(snitchSetting);
                        snitch = new SnitchAdapter(snitchList.getContext(), snitches);

                        tvTo.setText("TO: " + snitchSetting.to);
                        top.setText("" + snitchSetting.top);
                    } else {
                        snitchSetting.from = new java.sql.Date(calendar.getTime().getTime());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(snitchSetting.from);
                        cvDate.setMinDate(cal.getTime().getTime());
                        cal.add(Calendar.DATE, +6);
                        snitchSetting.to = new java.sql.Date(cal.getTime().getTime());

                        snitches = business.MobGetSnitches(snitchSetting);
                        snitch = new SnitchAdapter(snitchList.getContext(), snitches);
                        tvFrom.setText("FROM: " + snitchSetting.from);
                        tvTo.setText("TO: " + snitchSetting.to);
                        top.setText("" + snitchSetting.top);
                    }
                    snitchList.setAdapter(snitch);
                    cvDate.setVisibility(View.INVISIBLE);
                    snitchList.setVisibility(View.VISIBLE);


                }

            }

        });
        snitchSetting = new uspMobGetSnitches();
        cvDate.setMaxDate(cvDate.getDate());

        cvDate.setVisibility(View.INVISIBLE);
        date = cvDate.getDate();
        snitchSetting.from = new Date(date) ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(snitchSetting.from );
        cal.add(Calendar.DATE, -7);
        snitchSetting.from =  new java.sql.Date(cal.getTime().getTime());
        snitchSetting.to = new Date(date);
        snitchSetting.top = 5;
        tvFrom.setText("FROM: "+snitchSetting.from);
        tvTo.setText("TO: "+snitchSetting.to);
        //top.setText(""+snitchSetting.top);
        snitches = business.MobGetSnitches(snitchSetting);
         snitch = new SnitchAdapter(this,snitches);

        snitchList.setAdapter(snitch);

    }
    private void Foucs(int code){
        focusFrom.setVisibility(View.INVISIBLE);
        focusTo.setVisibility(View.INVISIBLE);
        if(code==CODEFROM){
            focusFrom.setVisibility(View.VISIBLE);
        }else if(code==CODETO){
            focusTo.setVisibility(View.VISIBLE);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(leaks!=null) {
//            switch (item.getItemId()) {
//                case R.id.action_fixed:
//                    ArrayList<ReportLeakModel> fixed = new ArrayList<>();
//                    for (ReportLeakModel reported : leaks) {
//                        if (reported.status == 1) {
//                            fixed.add(reported);
//                        }
//                    }
//
//                    onCallBackGround(fixed);
//                    break;
//                case R.id.action_waiting:
//                    ArrayList<ReportLeakModel> waiting = new ArrayList<>();
//                    for (ReportLeakModel reported : leaks) {
//                        if (reported.status == 0) {
//                            waiting.add(reported);
//                        }
//                    }
//                    onCallBackGround(waiting);
//                    break;
//                case R.id.action_withPic:
//                    ArrayList<ReportLeakModel> withPic = new ArrayList<>();
//                    for (ReportLeakModel reported : leaks) {
//                        if (reported.picPath != null) {
//                            withPic.add(reported);
//                        }
//                    }
//                    onCallBackGround(withPic);
//                    break;
//                case R.id.action_today:
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    String date = df.format(Calendar.getInstance().getTime());
//                    ArrayList<ReportLeakModel> today = new ArrayList<>();
//                    for (ReportLeakModel reported : leaks) {
//                        if (reported.date.toString().equals(date)) {
//                            today.add(reported);
//                        }
//                    }
//                    onCallBackGround(today);
//                    break;
//                case R.id.action_selectDay:
//                    cvDate.setVisibility(View.VISIBLE);
//                    reportedLeaks.setVisibility(View.INVISIBLE);
//                    break;
//                case R.id.action_all:
//                    onCallBackGround(leaks);
//                    break;
//                default:
//                    finish();
//                    break;
//            }
//        }else {
//            finish();
//        }
        switch (item.getItemId()){
            case R.id.action_selectDay:
                CODE=0;
                Calendar cal = Calendar.getInstance();
                cal.setTime(snitchSetting.from );
                cal.add(Calendar.YEAR, -2);
                cvDate.setMinDate(cal.getTime().getTime());
                cvDate.setVisibility(View.VISIBLE);
                snitchList.setVisibility(View.GONE);
                break;
            default:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fodscreen, menu);
        return true;
    }
    public void onDateSelecting(View v){
        cvDate.setVisibility(View.VISIBLE);
        snitchList.setVisibility(View.GONE);
        switch (v.getId()){
            case R.id.tvFrom:
                CODE = CODEFROM;
                Calendar cal = Calendar.getInstance();
                cal.setTime(snitchSetting.from );
                cal.add(Calendar.YEAR, -2);
                cvDate.setMinDate(cal.getTime().getTime());
                Foucs(CODE);
                break;
            case R.id.tvTo:
                CODE = CODETO;
                Foucs(CODE);
                cvDate.setMinDate( ((Date)snitchSetting.from).getTime());
                break;
        }
    }

}
