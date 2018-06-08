package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.List;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class IntakeTrendClass extends Fragment {
     BarChart barChart = null;
     List<BarEntry> barEntries = new ArrayList<>();
    final List<BarEntry> entries = new ArrayList<>();
    final List<BarEntry> entriesWeeks = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intake_trend, container, false);


        GeneralMethods m = new GeneralMethods(getContext());


        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        barChart = rootView.findViewById(R.id.bcT);
        final Random rNum = new Random();


        float y = 0;
        DBAccess business = new DBAccess();
        ArrayList<UspMobGetPersonTotalUsage> usages = business.GetPersonTotalUsageGetItems(m.Read("person.txt",",")[2]);
        int i = usages.size();
        final String[] labels = new String[i];
        i = 0;
        for (UspMobGetPersonTotalUsage usage:usages) {
            labels[i] = usage.UsageDay;
            entries.add(new BarEntry(i,usage.UsageAmount));
            i++;
        }
//
//        final String[] labelWeeks = {"Week 1","Week 2","Week 3","Week 4"};
//
//
//        for(float x = 0; x< labelWeeks.length ;x++)
//        {
//            y = rNum.nextInt(30)+2f;
//            entriesWeeks.add(new BarEntry(x, y));
//        }
        final TextView tvChartLAbel = rootView.findViewById(R.id.tvChartLAbel);

        SetUpGraph(bcTrend,entries,labels,tvChartLAbel);

        Button btnShow = rootView.findViewById(R.id.btnShowMode);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvChartLAbel.getText().toString()!="Weeks")
                {
                   // SetUpGraph(bcTrend,entriesWeeks,labelWeeks,tvChartLAbel);
                }
                else
                {
                    SetUpGraph(bcTrend,entries,labels,tvChartLAbel);
                }
            }
        });

        HorizontalBarChart barChart =  rootView.findViewById(R.id.bcT);

        ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.UspMobGetPersonItemTotal(m.Read("person.txt",",")[2]);
         i = ItemUsages.size();
        final String[] Itemlabels = new String[i];
        i = 0;
        ArrayList<BarEntry> Itementries = new ArrayList<>();
        for (UspMobGetPersonItemTotal usage:ItemUsages) {
            Itemlabels[i] = usage.ItemName;
            Itementries.add(new BarEntry(i,usage.UsageAmount));
            i++;
        }

        SetUpGraph(barChart,Itementries,labels,tvChartLAbel);


        return rootView;
    }







    public IAxisValueFormatter setYaxis(final String[] labels)
    {
        final IAxisValueFormatter[] formatter = {new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (labels.length > (int) value)
                    return labels[(int) value];
                else
                    return "";
            }
        }};
        return formatter[0];
    }
    public void SetUpGraph(BarChart  bcTrend ,List<BarEntry> entries,String[] Labels,TextView tvChartLAbel)
    {

        List<BarEntry> entry;
        IAxisValueFormatter formatter;
        float distance =0;
        int labelCount = 0;
        if(tvChartLAbel.getText().toString()=="Days")
        {
            formatter = setYaxis(Labels);
            distance = 0.7f;
            tvChartLAbel.setText("Weeks");
            labelCount = Labels.length;
            entry = entries;
        }
        else
        {
            formatter = setYaxis(Labels);
            labelCount = Labels.length;
            tvChartLAbel.setText("Days");
            distance = 0.5f;
            entry = entries;

        }
        BarDataSet set = new BarDataSet(entry,"Usage");
        set.setColor(R.color.black);
        BarData b = new BarData(set);

        b.setBarWidth(distance);
        bcTrend.setData(b);
        bcTrend.setFitBars(true);
        bcTrend.invalidate();
        XAxis xAxis = bcTrend.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelCount(labelCount);
        bcTrend.invalidate();
    }
    public void createRandomBarGraph(String Date1, String Date2){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ArrayList<String> dates = new ArrayList<>();
        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);


            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;
            Random random = new Random();
            for(int j = 0; j< dates.size();j++){
                max = 100f;
                value = random.nextFloat()*max;
                barEntries.add(new BarEntry(value,j));
            }

        }catch(ParseException e){
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData( barDataSet);
        barChart.setData(barData);

    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate =  new SimpleDateFormat("yyy/MM/dd").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }

}