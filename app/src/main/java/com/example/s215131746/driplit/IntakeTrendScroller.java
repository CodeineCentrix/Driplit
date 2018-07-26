package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import viewmodels.UspMobGetPersonItemTotal;
import viewmodels.UspMobGetPersonTotalUsage;

public class IntakeTrendScroller extends android.support.v4.app.Fragment  {
    //from item trend
    TextView tvNodata;
    HorizontalBarChart barChart;
    ArrayList<BarEntry> Itementries;
    final DBAccess business = new DBAccess();
    SimpleDateFormat df = new SimpleDateFormat("MMM dd");
    CalendarView cvDate = null;
    Button btnSelectDate ;
    //end

    //from trends
    final List<BarEntry> entries = new ArrayList<>();
    final ArrayList<Entry> lineEntry = new ArrayList<>();
    //end
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_intake_trend_scroller, container, false);


        //from item trend
        cvDate  = rootView.findViewById(R.id.cvDate);
        tvNodata = rootView.findViewById(R.id.tvNoData);
        barChart =  rootView.findViewById(R.id.bcT);
        btnSelectDate = rootView.findViewById(R.id.btnSelectDate);
        final GeneralMethods m = new GeneralMethods(rootView.getContext());
        cvDate.setVisibility(View.INVISIBLE);
        ArrayList<UspMobGetPersonItemTotal> ItemUsages = business.UspMobGetPersonItemTotal(m.Read("person.txt",",")[2]);
        cvDate.setMaxDate(cvDate.getDate());
        int i = ItemUsages.size();

        final String[] Itemlabels = new String[i];
        if(i>0){
            i = 0;
            Itementries = new ArrayList<>();
            for (UspMobGetPersonItemTotal usage : ItemUsages) {
                Itemlabels[i] = usage.ItemName;
                Itementries.add(new BarEntry(i, usage.UsageAmount));
                i++;
            }
            SetUpGraph(barChart, Itementries, Itemlabels);
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
                    Itementries.add(new BarEntry(x, usage.UsageAmount));
                    x++;
                }
                if(Itemlabels.length > 0) {
                    SetUpGraph(barChart, Itementries, Itemlabels);
                }

                SetUp(Itemlabels, dateV);
            }
        });
        //end


        //from trends
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        DBAccess business = new DBAccess();

        String index2 = m.Read("person.txt",",")[2];

        ArrayList<UspMobGetPersonTotalUsage> usages = business.GetPersonTotalUsageGetItems(index2);
       i = usages.size();
        LineChart lineChart = rootView.findViewById(R.id.lineChart);
        if(i>0) {
            final String[] labels = new String[i];
            i = 0;
            for (UspMobGetPersonTotalUsage usage : usages) {
                labels[i] = usage.UsageDay;
                entries.add(new BarEntry(i, usage.UsageAmount));
                lineEntry.add(new Entry(i, usage.UsageAmount));
                i++;

                SetUpGraph(bcTrend, entries, labels);
                //Line chart___________________________________________________________

                // creating list of entry

                LineDataSet dataset = new LineDataSet(lineEntry, "line water usage");//loading the top labels and setting the bottom label
                lineChart.setData(new LineData(dataset)); // set the data and list of labels into chart
                IAxisValueFormatter formatter = setYaxis(labels);
                lineChart.getAxisLeft().setAxisMinimum(0f);
                lineChart.getAxisRight().setEnabled(false);
                lineChart.getXAxis().setEnabled(false);
                lineChart.getXAxis().setValueFormatter(formatter);
                lineChart.getXAxis().setLabelCount(labels.length - 1);
                dataset.setDrawFilled(true);
                //end
            }

        }else{
            lineChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bcTrend.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return rootView;
    }
    //from item trend
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
    //end

    //from trends
    //Label returner
    public IAxisValueFormatter setYaxis(final String[] labels){
        return new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (labels.length > (int) value && value>-1 )
                    return labels[(int) value];
                else
                    return "";
            }
        };

    }
    //Bar graph set up
    public void SetUpGraph(BarChart bcTrend , List<BarEntry> entries, String[] Labels ){
        if(entries.size()>0) {
            IAxisValueFormatter formatter = setYaxis(Labels);
            BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");

            MyBarDataSet setColor = new MyBarDataSet(entries,"",set);
            setColor.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                    ContextCompat.getColor(getContext(), R.color.red)});
            ArrayList<BarDataSet> dataSets = new ArrayList<>();
            dataSets.add(setColor);


            BarData b = new BarData(dataSets.get(0));

            b.setBarWidth(0.5f);//bar width
            bcTrend.setData(b);
            bcTrend.getXAxis().setGranularity(1f);//set the interval between labels so they do not duplicate
            bcTrend.getAxisLeft().setAxisMinimum(0f);
            bcTrend.getAxisLeft().setDrawGridLines(false);//removes the grid lines of the bar graph
            bcTrend.invalidate();
            bcTrend.getAxisRight().setEnabled(false);
            bcTrend.getXAxis().setValueFormatter(formatter);// uses the an interface to get labels
            bcTrend.getXAxis().setLabelCount(Labels.length);
            bcTrend.fitScreen();//obviuosly its to make sure the graph fits the screen
            bcTrend.invalidate();
            bcTrend.setVisibleXRangeMaximum(5);// sets the number of bars to be shown at a given moment
            bcTrend.moveViewToX(entries.size());
        }
    }
    //end





    public class MyBarDataSet extends BarDataSet {

        BarDataSet set;
        public MyBarDataSet(List<BarEntry> yVals, String label, BarDataSet set) {
            super(yVals, label);
            this.set = set;
        }

        @Override
        public int getColor(int index) {
           try {
               if(this.getEntryForIndex(index).getY() < 60) // less than 95 green
                   return mColors.get(0);
               else // greater or equal than 100 red
                   return mColors.get(1);
           }catch (Exception e){
               e.printStackTrace();
           }

        return -1;
        }

    }
}
