package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viewmodels.PersonModel;
import viewmodels.UspMobGetPersonItemTotal;
import viewmodels.UspMobGetPersonTotalUsage;

public class IntakeTrendScroller extends android.support.v4.app.Fragment  {
    //from item trend
    TextView tvNodata;
    HorizontalBarChart barChart;
    ArrayList<BarEntry> Itementries;
    DBAccess business = new DBAccess();
    SimpleDateFormat df = new SimpleDateFormat("MMM dd");
    CalendarView cvDate = null;
    Button btnSelectDate ;
    float averageUsage;
    //end
     GeneralMethods m;
    //from trends
    final List<BarEntry> entries = new ArrayList<>();
    final ArrayList<Entry> lineEntry = new ArrayList<>();
    //end
    public View rootView;
    Handler h = new Handler();
    ArrayList<UspMobGetPersonItemTotal> IitemUsages;
    ArrayList<UspMobGetPersonTotalUsage> usages;
    ArrayList<UspMobGetPersonItemTotal> ItemUsages;
    private ImageView[] moreOrLess=null;
    private   TextView[] desc ;
    String []person =null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_intake_trend_scroller, container, false);
       moreOrLess = new ImageView[]{
               rootView.findViewById(R.id.imgLessTop),
               rootView.findViewById(R.id.imgMoreTop),
               rootView.findViewById(R.id.imgLessLine),
               rootView.findViewById(R.id.imgMoreLine),
               rootView.findViewById(R.id.imgLessBottom),
               rootView.findViewById(R.id.imgMoreBottom),
       };

       desc = new TextView[]{
               rootView.findViewById(R.id.tvTop),
               rootView.findViewById(R.id.tvLine),
               rootView.findViewById(R.id.tvBottom)
       };
        final DecimalFormat dc = new DecimalFormat("0.0");
        //from item trend
        cvDate  = rootView.findViewById(R.id.cvDate);
        tvNodata = rootView.findViewById(R.id.tvNoData);
        barChart =  rootView.findViewById(R.id.bcT);
        btnSelectDate = rootView.findViewById(R.id.btnSelectDate);
        m = new GeneralMethods(rootView.getContext());
        try {
            person = m.Read(rootView.getContext().getString(R.string.person_file_name)
                    , ",");
        }catch (Exception e){

        }
        helpThread h = new helpThread(true,rootView.getContext());
        new Thread(h).start();
        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                cvDate.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date date = calendar.getTime();
                //String de = String.valueOf(d.getGregorianChange());
                dateSelected(df.format(date));
            }
        });
        moreOrLess[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOrLess(v);
            }
        });
        moreOrLess[1].setVisibility(View.GONE);
        moreOrLess[3].setVisibility(View.GONE);
        moreOrLess[5].setVisibility(View.GONE);

        return rootView;
    }
    public void dateSelected(String date){

        String dateV = date, index2 = person[PersonModel.EMAIL];

        btnSelectDate.setVisibility(View.VISIBLE);

        averageUsage =0;

        IitemUsages = business.uspMobGetPersonItemTotalDate(index2,dateV);

        int x = IitemUsages.size();
        final String[] Itemlabels = new String[x];
        x = 0;
        ArrayList<BarEntry> Itementries = new ArrayList<>();
        for(UspMobGetPersonItemTotal usage : IitemUsages) {
            Itemlabels[x] = usage.ItemName;
            averageUsage+=usage.UsageAmount;
            Itementries.add(new BarEntry(x, usage.UsageAmount));
            x++;
        }
        averageUsage/=x;
        if(Itemlabels.length > 0) {
            SetUpGraph(barChart, Itementries, Itemlabels,averageUsage);
        }

        SetUp(Itemlabels, dateV);
    }
    public void afterConnection(final View rootView){
        cvDate.setVisibility(View.INVISIBLE);


        cvDate.setMaxDate(cvDate.getDate());
        int i = ItemUsages.size();
        averageUsage =0;
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
            SetUpGraph(barChart, Itementries, Itemlabels,averageUsage);
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


        //end


        //from trends
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        DBAccess business = new DBAccess();


        i = usages.size();
        LineChart lineChart = rootView.findViewById(R.id.lineChart);
        if(i>0) {
            final String[] labels = new String[i];
            i = 0;
            averageUsage=0;
            entries.clear();
            lineEntry.clear();
            for (UspMobGetPersonTotalUsage usage : usages) {
                labels[i] = usage.UsageDay;
                averageUsage+=usage.UsageAmount;
                entries.add(new BarEntry(i, usage.UsageAmount));
                lineEntry.add(new Entry(i, usage.UsageAmount));
                i++;
            }
            averageUsage/=i;
            SetUpGraph(bcTrend, entries, labels,averageUsage);
            bcTrend.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String date;
                    int i =Math.round(e.getX());
                    try {
                        date = labels[i];
                        dateSelected( date);
                    }catch (Exception ex){

                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
            //Line chart___________________________________________________________

            // creating list of entry

            LineDataSet dataset = new LineDataSet(lineEntry, "line water usage");//loading the top labels and setting the bottom label
            lineChart.setData(new LineData(dataset)); // set the data and list of labels into chart
            IAxisValueFormatter formatter = setYaxis(labels);
            //lineChart.getAxisLeft().setAxisMinimum(0f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.getXAxis().setEnabled(false);
            YAxis y = lineChart.getAxisLeft();
           try {
               LimitLine ll = new LimitLine(Integer.parseInt(person[PersonModel.USAGETARGET]), "My Max Target ");
               y.addLimitLine(ll);
           }catch (Exception e){

           }
            lineChart.getXAxis().setValueFormatter(formatter);
            lineChart.getXAxis().setLabelCount(labels.length - 1);
            dataset.setDrawFilled(true);
            lineChart.getXAxis().setTextColor(R.color.colorAccent);
            //end
        }else{
            lineChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bcTrend.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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
    public void SetUpGraph(BarChart bcTrend , List<BarEntry> entries, String[] Labels ,float avg){
        if(entries.size()>0) {
            IAxisValueFormatter formatter = setYaxis(Labels);
            BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");

            final DecimalFormat dc = new DecimalFormat("0.0");
            MyBarDataSet setColor = new MyBarDataSet(entries,"Average water usage: "+dc.format(avg),set);
            setColor.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                    ContextCompat.getColor(getContext(), R.color.red)});
            ArrayList<BarDataSet> dataSets = new ArrayList<>();
            dataSets.add(setColor);
            BarData b = new BarData(dataSets.get(0));
            //bcTrend.getAxis(null).setTextColor(R.color.colorAccent);
            b.setBarWidth(0.5f);//bar width
            bcTrend.setData(b);

            bcTrend.getXAxis().setGranularity(1f);//set the interval between labels so they do not duplicate
            // bcTrend.getAxisLeft().setAxisMinimum(0f);
            bcTrend.getAxisLeft().setDrawGridLines(false);//removes the grid lines of the bar graph
            bcTrend.invalidate();
            bcTrend.getAxisRight().setEnabled(false);
            bcTrend.getXAxis().setValueFormatter(formatter);// uses the an interface to get labels
            bcTrend.getXAxis().setLabelCount(Labels.length);
            bcTrend.fitScreen();//obviuosly its to make sure the graph fits the screen
            bcTrend.invalidate();

            bcTrend.setVisibleXRangeMaximum(5f);// sets the number of bars to be shown at a given moment
            bcTrend.moveViewToX(entries.size());
        }
    }
    //end
    class helpThread implements Runnable {
        Context context;
        boolean onCreate;
        boolean isConnecting;
        Snackbar mySnackbar;
        public helpThread() {

        }

        public helpThread(boolean onCreate,Context context) {
            this.onCreate = onCreate;
            this.context = context;
        }


        @Override
        public void run() {
            if(onCreate){

                isConnecting = business.isConnecting();
                if(isConnecting) {
                    Calendar calendar = Calendar.getInstance();

                    Date date = calendar.getTime();
                    String index2 = m.Read(context.getString(R.string.person_file_name),",")[PersonModel.EMAIL];


                    String dateV = df.format(date);
                    ItemUsages = business.uspMobGetPersonItemTotal(index2);
                    IitemUsages = business.uspMobGetPersonItemTotalDate(index2, dateV);
                    usages = business.GetPersonTotalUsageGetItems(index2);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            afterConnection(rootView);
                        }
                    });
                }else {
                    mySnackbar = Snackbar.make(tvNodata,"No Connection", 8000);
                    mySnackbar.getView().setBackgroundColor(Color.RED);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            mySnackbar.show();
                        }
                    });
                }
            }else{
            }
        }
    }
    private class MyBarDataSet extends BarDataSet {

        BarDataSet set;
        public MyBarDataSet(List<BarEntry> yVals, String label, BarDataSet set) {
            super(yVals, label);
            this.set = set;
        }

        @Override
        public int getColor(int index) {
           try {
               int target = Integer.parseInt(
                       m.Read(rootView.getContext().getString(R.string.person_file_name)
                               ,",")[PersonModel.USAGETARGET]);
               if(this.getEntryForIndex(index).getY() < target) // less than 95 green
                   return mColors.get(0);
               else // greater or equal than 100 red
                   return mColors.get(1);
           }catch (Exception e){
               e.printStackTrace();
           }

        return -1;
        }

    }
    public void MoreOrLess(View view){



        switch(view.getId()){
            case R.id.imgLessTop:
                desc[0].setVisibility(View.GONE);
                moreOrLess[0].setVisibility(View.GONE);
                moreOrLess[1].setVisibility(View.VISIBLE);
                break;
            case R.id.imgMoreTop:
                desc[0].setVisibility(View.VISIBLE);
                moreOrLess[0].setVisibility(View.VISIBLE);
                moreOrLess[1].setVisibility(View.GONE);
                break;
            case R.id.imgLessLine:
                desc[1].setVisibility(View.GONE);
                moreOrLess[3].setVisibility(View.VISIBLE);
                moreOrLess[2].setVisibility(View.GONE);
                break;
            case R.id.imgMoreLine:
                desc[1].setVisibility(View.VISIBLE);
                moreOrLess[3].setVisibility(View.GONE);
                moreOrLess[2].setVisibility(View.VISIBLE);
                break;
            case R.id.imgLessBottom:
                desc[2].setVisibility(View.GONE);
                moreOrLess[5].setVisibility(View.VISIBLE);
                moreOrLess[4].setVisibility(View.GONE);
                break;
            case R.id.imgMoreBottom:
                desc[2].setVisibility(View.VISIBLE);
                moreOrLess[5].setVisibility(View.GONE);
                moreOrLess[4].setVisibility(View.VISIBLE);
                break;
        }
    }
}
