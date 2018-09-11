package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import viewmodels.UspMobGetPersonItemTotal;

/**
 * Created by s216127904 on 2018/06/08.
 */

public class ItemTrend extends android.support.v4.app.Fragment {
    IntakeTrendClass in = new IntakeTrendClass();
    TextView tvNodata;
    HorizontalBarChart barChart;
    ArrayList<BarEntry> Itementries;
    final DBAccess business = new DBAccess();
    float averageUsage;
    SimpleDateFormat df = new SimpleDateFormat("MMM dd");
    CalendarView cvDate = null;
    Button btnSelectDate ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_trend, container, false);
        averageUsage =0;
        cvDate  = rootView.findViewById(R.id.cvDate);
        tvNodata = rootView.findViewById(R.id.tvNoData);
        barChart =  rootView.findViewById(R.id.bcT);
        btnSelectDate = rootView.findViewById(R.id.btnSelectDate);
        final GeneralMethods m = new GeneralMethods(rootView.getContext());
        cvDate.setVisibility(View.INVISIBLE);
        ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.uspMobGetPersonItemTotal(
                m.Read("person.txt",",")[2]);
        cvDate.setMaxDate(cvDate.getDate());
        int i = ItemUsages.size();

        final String[] Itemlabels = new String[i];
        if(i>0){
            i = 0;
            Itementries = new ArrayList<>();
            for (UspMobGetPersonItemTotal usage : ItemUsages) {
                Itemlabels[i] = usage.ItemName;
                averageUsage+=usage.UsageAmount;
                Itementries.add(new BarEntry(i, usage.UsageAmount));
                i++;
            }
            averageUsage/=i;
            in.SetUpGraph(barChart, Itementries, Itemlabels);
            Date date = new Date();
            date.setTime(cvDate.getDate());
            SetUp(Itemlabels, df.format(date));
        }
        else {SetUp(Itemlabels,m.GetDate());}

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDate.setVisibility(View.VISIBLE);
                btnSelectDate.setVisibility(View.INVISIBLE);
                if (barChart.getVisibility() == View.VISIBLE) {
                    barChart.setVisibility(View.INVISIBLE);
                    v.setVisibility(View.INVISIBLE);
                } else {
                    tvNodata.setVisibility(View.INVISIBLE);
                }
            }
        });

        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                cvDate.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date date = calendar.getTime();
                //String de = String.valueOf(d.getGregorianChange());
                String dateV = df.format(date), index2 = m.Read("person.txt", ",")[2];

                btnSelectDate.setVisibility(View.VISIBLE);

                ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.uspMobGetPersonItemTotalDate(index2, dateV);
                int x = ItemUsages.size();
                final String[] Itemlabels = new String[x];
                x = 0;
                ArrayList<BarEntry> Itementries = new ArrayList<>();
                for(UspMobGetPersonItemTotal usage : ItemUsages) {
                   Itemlabels[x] = usage.ItemName;
                    averageUsage+=usage.UsageAmount;
                   Itementries.add(new BarEntry(x, usage.UsageAmount));
                   x++;
                }
                averageUsage/=x;
                if(Itemlabels.length > 0) {
                  in.SetUpGraph(barChart, Itementries, Itemlabels);
                }

                SetUp(Itemlabels, dateV);
            }
        });

        return rootView;
    }

    public void SetUp(String[] l,String date){
        btnSelectDate.setVisibility(View.VISIBLE);
        if(l.length>0){
            barChart.setExtraOffsets(0f, 20f, 60f, 20f);
            barChart.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0);
            tvNodata.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.INVISIBLE);
            tvNodata.setText("The are no recordings for "+date);
            tvNodata.setVisibility(View.VISIBLE);
        }
    }



}
