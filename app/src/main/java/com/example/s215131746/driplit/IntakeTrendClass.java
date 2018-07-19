package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.List;

import viewmodels.UspMobGetPersonTotalUsage;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class IntakeTrendClass extends Fragment {
     BarChart barChart = null;
     List<BarEntry> barEntries = new ArrayList<>();
    final List<BarEntry> entries = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intake_trend, container, false);
        GeneralMethods m = new GeneralMethods(getContext());
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        barChart = rootView.findViewById(R.id.bcT);
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
//        final String[] labelWeeks = {"Week 1","Week 2","Week 3","Week 4"};
//        for(float x = 0; x< labelWeeks.length ;x++){
//            y = rNum.nextInt(30)+2f;
//            entriesWeeks.add(new BarEntry(x, y));
//        }
        final TextView tvChartLAbel = rootView.findViewById(R.id.tvChartLAbel);
        try{
            SetUpGraph(bcTrend,entries,labels,tvChartLAbel);
        } catch (ArrayIndexOutOfBoundsException e){}
        LineChart lineChart = rootView.findViewById(R.id.lineChart);
// creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        i = 0;
        for (UspMobGetPersonTotalUsage usage:usages) {
            labels[i] = usage.UsageDay;
            entries.add(new Entry(i,usage.UsageAmount));
            i++;
        }
        LineDataSet dataset = new LineDataSet(entries, "Water usage");
        // creating labels
        LineData data = new LineData(dataset);
        lineChart.setData(data); // set the data and list of lables into chart

       try{
           IAxisValueFormatter   formatter = setYaxis(labels);
           AxisBase leftY = lineChart.getAxisLeft();
           leftY.setAxisMinimum(0f);
           lineChart.getAxisRight().setEnabled(false);
           XAxis x = lineChart.getXAxis();
           x.setValueFormatter(formatter);
           x.setLabelCount(labels.length-1);
       }catch (ArrayIndexOutOfBoundsException e){ }

        dataset.setDrawFilled(true);
        return rootView;
    }

    public IAxisValueFormatter setYaxis(final String[] labels){
        final IAxisValueFormatter[] formatter = {new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (labels.length > (int) value && value>-1)
                    return labels[(int) value];
                else
                    return "";
            }
        }};
        return formatter[0];
    }
    public void SetUpGraph(BarChart  bcTrend ,List<BarEntry> entries,String[] Labels,TextView tvChartLAbel ){
        List<BarEntry> entry;
        IAxisValueFormatter formatter;
        float distance =0;
        int labelCount = 0;
        formatter = setYaxis(Labels);
        labelCount = Labels.length;
        if(tvChartLAbel.getText().toString()=="Days"){
            distance = 0.7f;
            tvChartLAbel.setText("Weeks");
        }else{
            tvChartLAbel.setText("Days");
            distance = 0.5f;
        }
        entry = entries;
        BarDataSet set = new BarDataSet(entry,"Usage");
        set.setColor(R.color.black);
        BarData b = new BarData(set);
        //b.setValueTextSize(20f);
        b.setBarWidth(distance);
        bcTrend.setData(b);
        AxisBase leftY = bcTrend.getAxisLeft();
        leftY.setAxisMinimum(0f);
        leftY.setDrawGridLines(false);
        bcTrend.invalidate();
        bcTrend.getAxisRight().setEnabled(false);
        XAxis xAxis = bcTrend.getXAxis();
        //xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelCount(labelCount);
        //bcTrend.setVisibleYRangeMaximum(50f, YAxis.AxisDependency.RIGHT);
        bcTrend.fitScreen();
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