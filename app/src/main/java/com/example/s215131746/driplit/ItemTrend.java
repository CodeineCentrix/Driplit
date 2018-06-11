package com.example.s215131746.driplit;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by s216127904 on 2018/06/08.
 */

public class ItemTrend extends android.support.v4.app.Fragment {
    IntakeTrendClass in = new IntakeTrendClass();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_trend, container, false);
        final CalendarView cvDate  = rootView.findViewById(R.id.cvDate);
        final HorizontalBarChart barChart =  rootView.findViewById(R.id.bcT);
        final DBAccess business = new DBAccess();
        final GeneralMethods m = new GeneralMethods(rootView.getContext());
        ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.UspMobGetPersonItemTotal(m.Read("person.txt",",")[2]);
        int i = ItemUsages.size();
        final String[] Itemlabels = new String[i];
        i = 0;
        ArrayList<BarEntry> Itementries = new ArrayList<>();
        for (UspMobGetPersonItemTotal usage:ItemUsages) {
            Itemlabels[i] = usage.ItemName;
            Itementries.add(new BarEntry(i,usage.UsageAmount));
            i++;
        }
        final TextView t = rootView.findViewById(R.id.textView);
        t.setText("Day");
        cvDate.setVisibility(View.INVISIBLE);

        final Button btnSelectDate = rootView.findViewById(R.id.btnSelectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barChart.getVisibility()==View.VISIBLE)
                {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String date = df.format(cvDate.getDate());
                    cvDate.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.INVISIBLE);
                    btnSelectDate.setVisibility(View.INVISIBLE);
                }
                else
                {

                }
            }
        });
      cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
          @Override
          public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {



              cvDate.setVisibility(View.INVISIBLE);
              barChart.setVisibility(View.VISIBLE);
              SimpleDateFormat df = new SimpleDateFormat("MMM dd");
              Calendar calendar = Calendar.getInstance();
              calendar.set(year, month, day);
              Date date = calendar.getTime();
              //String de = String.valueOf(d.getGregorianChange());
              String dateV = df.format(date);

              btnSelectDate.setVisibility(View.VISIBLE);

              ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.uspMobGetPersonItemTotalDate(m.Read("person.txt",",")[2],dateV);
              int x = ItemUsages.size();
              final String[] Itemlabels = new String[x];
              x = 0;
              ArrayList<BarEntry> Itementries = new ArrayList<>();
              for (UspMobGetPersonItemTotal usage:ItemUsages) {
                  Itemlabels[x] = usage.ItemName;
                  Itementries.add(new BarEntry(x,usage.UsageAmount));
                  x++;
              }
              try{
                  in.SetUpGraph(barChart,Itementries,Itemlabels,t);
              }catch (ArrayIndexOutOfBoundsException e)
              {

              }
              barChart.setExtraOffsets(2f, 20f, 60f, 20f);
          }


      });
        try{
            in.SetUpGraph(barChart,Itementries,Itemlabels,t);
        }catch (ArrayIndexOutOfBoundsException e)
        {

        }

        barChart.setExtraOffsets(0f, 20f, 60f, 20f);
        AxisBase axis = barChart.getAxis(YAxis.AxisDependency.LEFT);
        axis.setAxisMinimum(0);
        return rootView;
    }


}
