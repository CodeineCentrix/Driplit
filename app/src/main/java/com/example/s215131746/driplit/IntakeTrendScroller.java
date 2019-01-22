package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viewmodels.PersonModel;
import viewmodels.ResidentUsageModel;
import viewmodels.UspMobGetPersonItemTotal;
import viewmodels.UspMobGetPersonTotalUsage;

public class IntakeTrendScroller extends android.support.v4.app.Fragment  {
    //from item trend
    TextView tvNodata;
    //BarChart barChart;
    ArrayList<BarEntry> Itementries;
    DBAccess business = new DBAccess();
    SimpleDateFormat df = new SimpleDateFormat("MMM dd");
    CalendarView cvDate = null;
    ImageButton btnCalender;
    float averageUsage;
    String[] Itemlabels;
    //end
     GeneralMethods m;
    //from trends
    final List<BarEntry> entries = new ArrayList<>();
    final ArrayList<Entry> lineEntry = new ArrayList<>();
    //end
    public View rootView;
    Handler h = new Handler();
    ArrayList<UspMobGetPersonItemTotal> pieChartData;
    ArrayList<UspMobGetPersonTotalUsage> barAndLineData;
    private ImageView[] moreOrLess=null;
    private   TextView[] desc ;
    PieChart pie;
    String []person =null;
    List<PieEntry> pieEntries;
    final DecimalFormat dc = new DecimalFormat("0.0");
    ArrayList<Integer> colors;
    private Legend legend;
    LayoutInflater inf;
    ViewGroup con;
    ArrayList<UspMobGetPersonTotalUsage> barAndLineDataAll;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_intake_trend_scroller, container, false);
        //_______________________________________________________________
        inf = inflater;
        con = container;
        pie = rootView.findViewById(R.id.pies);
         pie.setHoleColor(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimaryDark));
        pie.setUsePercentValues(false);
        pie.getDescription().setEnabled(false);
        pie.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                View t = inf.inflate(R.layout.water_intake_item_layout, con, false);
                Toast toast = Toast.makeText(rootView.getContext(), pe.getLabel().trim(), Toast.LENGTH_SHORT);
//                toast.getView().setBackgroundColor(GeneralMethods.MATERIAL_COLORS[Math.round(h.getX())]);
//                TextView name = t.findViewById(R.id.tvItemName);
//                toast.setGravity(Gravity.BOTTOM,0,0);
//                name.setText(pe.getLabel().trim());
//                name.setTextColor(GeneralMethods.MATERIAL_COLORS[Math.round(h.getX())]);
//                name.setBackgroundColor(  ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//                toast.setView(t);
                toast.getView().setBackgroundColor(GeneralMethods.MATERIAL_COLORS[Math.round(h.getX())]);
                toast.show();

            }
            @Override
            public void onNothingSelected() {}
        });
        legend = pie.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setFormSize(16f);
        legend.setTextSize(16f);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.white));
        legend.setDrawInside(false);
        colors = new ArrayList<>();
        for (int c : GeneralMethods.MATERIAL_COLORS)//pie colors
            colors.add(c);

        //______________________________________________________________

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
        cvDate  = rootView.findViewById(R.id.cvDate);
        tvNodata = rootView.findViewById(R.id.tvNoData);
        btnCalender = rootView.findViewById(R.id.btnSelectDate);
        m = new GeneralMethods(rootView.getContext());
        person = m.Read(rootView.getContext().getString(R.string.person_file_name), ",");
        helpThread h = new helpThread(true,rootView.getContext());
        new Thread(h).start();
        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                cvDate.setVisibility(View.INVISIBLE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                dateSelected(df.format(calendar.getTime()));
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
        moreOrLess[0].setVisibility(View.GONE);
        moreOrLess[2].setVisibility(View.GONE);
        moreOrLess[4].setVisibility(View.GONE);
        LinearLayout linearLayout2 = rootView.findViewById(R.id.linearLayout2);
        Animation anime = AnimationUtils.makeInAnimation(rootView.getContext(),false);
        anime.setDuration(1000);
        linearLayout2.setAnimation(anime);
        return rootView;
    }
    public void dateSelected(String date){

        String dateV = date, index2 = person[PersonModel.EMAIL];

        btnCalender.setVisibility(View.VISIBLE);
        pieChartData = business.uspMobGetPersonItemTotalDate(index2,dateV);
        int x = pieChartData.size();
        Itemlabels = new String[x];
        x = 0;
        pieEntries = new ArrayList<>();
        float smallerSlice =99;
        for(UspMobGetPersonItemTotal usage : pieChartData) {
            Itemlabels[x] = usage.ItemName;
            smallerSlice = Math.min(smallerSlice,usage.UsageAmount);
            pieEntries.add(new PieEntry(m.round(usage.UsageAmount),  usage.ItemName));
            x++;
        }
        averageUsage/=x;
        if(Itemlabels.length > 0) {
           // SetUpGraph(barChart, Itementries, Itemlabels,averageUsage);
            PieDataSet pieSet = new PieDataSet(pieEntries, "");
            pieSet.setColors(colors);
            pie.setUsePercentValues(false);
            PieData data = new PieData(pieSet);
            if(smallerSlice<1){
                pieSet.setSliceSpace(0f);
            }else {
                pieSet.setSliceSpace(10f);
            }
            pie.setData(data);
            pie.getData().setValueTextSize(16f);
            pie.getData().setValueTextColor(ContextCompat.getColor(rootView.getContext(), R.color.white));
            pie.invalidate();
        }
        LoadDayGraph(Itemlabels, dateV);
    }
    public void afterConnection(final View rootView){
        PieChart();

        BarGraph();
        LineGraph();
    }
    //from item trend
    public void LoadDayGraph(String[] l, String date){
        btnCalender.setVisibility(View.VISIBLE);
        if(l.length>0){
            tvNodata.setVisibility(View.INVISIBLE);
            pie.setVisibility(View.VISIBLE);
//            pie.getData().setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    DecimalFormat mFormat;
//                    mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
//                    return mFormat.format(value) + " L"; // e.g. append a dollar-sign
//                }
//            });
        }else{
            pie.setVisibility(View.INVISIBLE);
            tvNodata.setText("The are no recordings for "+date);
            tvNodata.setVisibility(View.VISIBLE);
        }
    }
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
    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " $"; // e.g. append a dollar-sign
        }
    }
    class helpThread implements Runnable {
        Context context;
        boolean onCreate;
        boolean isConnecting;
        Snackbar mySnackbar;
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
                    pieChartData = business.uspMobGetPersonItemTotalDate(
                            person[PersonModel.EMAIL],
                            df.format(calendar.getTime())
                    );

                   barAndLineDataAll = business.MobGetOverallTrend();
                    barAndLineData = business.GetPersonTotalUsageGetItems(person[PersonModel.EMAIL]);
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
    public void LineGraph(){
        int i = barAndLineData.size();
        LineChart lineChart = rootView.findViewById(R.id.lineChart);
        if(i>0) {
            i = 1;

            //Line chart___________________________________________________________

            // creating list of entry
            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            i=0;
            String[] Linelabels = new String[barAndLineDataAll.size()];
            ArrayList<Entry> newlineEntry = new ArrayList<>();
            lineEntry.clear();
            for (UspMobGetPersonTotalUsage usage : barAndLineDataAll) {
                Linelabels[i] = usage.UsageDay;
                float off = m.round(usage.UsageAmount);
                newlineEntry.add(new Entry(i, off));
                for (UspMobGetPersonTotalUsage lineUsage : barAndLineData) {
                    if(usage.UsageDay.equals(lineUsage.UsageDay)){
                        off = m.round(lineUsage.UsageAmount);
                        lineEntry.add(new Entry(i, off));
                        barAndLineData.remove(lineUsage);
                        break;
                    }
                }
                i++;
            }
            LineDataSet dataset = new LineDataSet(lineEntry, "line water usage");//loading the top labels and setting the bottom label

            LineDataSet dataset2 = new LineDataSet(newlineEntry, "line water usage of others");//loading the top labels and setting the bottom label
            dataset.setColor(ContextCompat.getColor(getContext(), R.color.green));
            dataset.setCircleColor(ContextCompat.getColor(getContext(), R.color.green));
            dataset2.setColor(ContextCompat.getColor(getContext(), R.color.blue));
            dataset2.setCircleColor(ContextCompat.getColor(getContext(), R.color.blue));
            datasets.add(dataset2);
            datasets.add(dataset);

            LineData lData = new LineData(datasets);
            Legend l = lineChart.getLegend();

            lineChart.setData(lData); // set the data and list of labels into chart

            IAxisValueFormatter formatter = setYaxis(Linelabels);
            lineChart.getAxisRight().setEnabled(false);
//            lineChart.getXAxis().setEnabled(false);

            lineChart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lineChart.getAxisLeft().setTextSize(16f);
//            lineChart.getLineData().setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
//            lineChart.getLineData().setValueTextSize(16f);
            lineChart.getLineData().setHighlightEnabled(false);
            lineChart.setVisibleXRangeMaximum(7f);

            lineChart.moveViewToX(entries.size());
            YAxis y = lineChart.getAxisLeft();
            try {
                LimitLine ll = new LimitLine(Integer.parseInt(person[PersonModel.USAGETARGET]), "My "+person[PersonModel.USAGETARGET]+" Litre Target ");
                ll.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                ll.setTextSize(16f);
                ll.setLineColor(ContextCompat.getColor(getContext(), R.color.red));
                y.addLimitLine(ll);
            }catch (Exception ignored){}
            lineChart.getXAxis().setValueFormatter(formatter);
            lineChart.getXAxis().setLabelCount(Linelabels.length - 1);
            dataset.setDrawFilled(false);
            lineChart.buildDrawingCache();
            //end
//            lineChart.getLineData().setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    DecimalFormat mFormat;
//                    mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
//                    return mFormat.format(value) + " L"; // e.g. append a dollar-sign
//                }
//            });
            lineChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    DecimalFormat mFormat;
                    mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
                    return mFormat.format(value) + " L"; // e.g. append a dollar-sign
                }
            });
            YAxis leftAxis = lineChart.getAxisLeft();
            //leftAxis.setAxisMaxValue(220f); // to set maximum yAxis
            leftAxis.setAxisMinValue(0.1f); // to set minimum yAxis
            leftAxis.setStartAtZero(false);
            //lineChart.animateX(2500, Easing.EasingOption.EaseInCubic);
            lineChart.getXAxis().setGranularity(1f);
            lineChart.invalidate();
        }else{
            lineChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            lineChart.setVisibility(View.GONE);

            RelativeLayout pieRelatie  = rootView.findViewById(R.id.pieRelatie);
            pieRelatie.setVisibility(View.GONE);
        }
    }
    public void BarGraph() {
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        int i = barAndLineData.size();
        if (i > 0) {
            final String[] labels = new String[i + 1];
            labels[0] = ">>>";
            entries.add(new BarEntry(0, 0));
            i = 1;
            averageUsage = 0;
            entries.clear();
            lineEntry.clear();
            for (UspMobGetPersonTotalUsage usage : barAndLineData) {
                labels[i] = usage.UsageDay;
                averageUsage += usage.UsageAmount;
                float off = 0;
                try{
                    off = m.round(usage.UsageAmount);
                }catch (Exception In){

                }
                entries.add(new BarEntry(i, off));


                i++;
            }
            averageUsage /= i - 1;

            if (entries.size() > 0) {
                IAxisValueFormatter formatter = setYaxis(labels);
                BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");


                MyBarDataSet setColor = new MyBarDataSet(entries, "Average water usage: " + dc.format(averageUsage), set);
                setColor.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.red)});

                ArrayList<BarDataSet> dataSets = new ArrayList<>();
                dataSets.add(setColor);
                BarData b = new BarData(dataSets.get(0));

                b.setBarWidth(0.5f);//bar width
//                TextView xAxisName =new TextView(this.getContext());
//                ((ViewGroup) xAxisName.getParent()).removeView(xAxisName);
//                xAxisName.setText("Date");
//                xAxisName.setWidth(bcTrend.getWidth());
//                xAxisName.setGravity(Gravity.CENTER);
//
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//                params.setMargins(0, 10, 0, 20);
//
//
//
//                bcTrend.addView(xAxisName,params);
                bcTrend.setData(b);
                bcTrend.setExtraOffsets(10, 30, 10, 8);
                bcTrend.setFitBars(true);
                bcTrend.getDescription().setText("");

                Legend l = bcTrend.getLegend();
                l.mNeededHeight = 200;
                l.setForm(Legend.LegendForm.CIRCLE);
                l.setFormSize(16);
                l.setTextSize(16f);
                try {
                    l.getEntries()[0].label = "Below " + person[PersonModel.USAGETARGET] + " Litres";
                    l.getEntries()[1].label = "Above " + person[PersonModel.USAGETARGET] + " Litres";
                } catch (Exception e) {
                }
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);

                l.setDrawInside(false);
                l.setYEntrySpace(2f);
                l.setXEntrySpace(2f);
                l.setStackSpace(50f);
                l.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                l.setWordWrapEnabled(true);

                bcTrend.getXAxis().setGranularity(1f);//set the interval between labels so they do not duplicate
                bcTrend.getAxisLeft().setDrawGridLines(false);//removes the grid lines of the bar graph

                bcTrend.getAxisLeft().setEnabled(false);
                bcTrend.getAxisRight().setEnabled(false);
                bcTrend.getXAxis().setValueFormatter(formatter);// uses the an interface to get labels
                bcTrend.getXAxis().setLabelCount(labels.length);
                bcTrend.getXAxis().setTextSize(16f);
                bcTrend.getXAxis().setAxisMinimum(0);
                bcTrend.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                bcTrend.getBarData().setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
                bcTrend.getBarData().setValueTextSize(16f);//Set font size
                bcTrend.fitScreen();//obviuosly its to make sure the graph fits the screen
                bcTrend.invalidate();//load graph to show data
                bcTrend.setVisibleXRangeMaximum(5f);// sets the number of bars to be shown at a given moment
                bcTrend.moveViewToX(entries.size());// To show last bar instead of firs
                bcTrend.getBarData().setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        DecimalFormat mFormat;
                        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
                        return mFormat.format(value) + " L"; // e.g. append a dollar-sign
                    }
                });
            } else {
                bcTrend.setNoDataText("No water usage recoded");
            }
            bcTrend.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String date;
                    int i = Math.round(e.getX());
                    try {
                        date = labels[i];
                        dateSelected(date);
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }else{
            bcTrend.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bcTrend.setVisibility(View.GONE);
            RelativeLayout pieRelatie  = rootView.findViewById(R.id.pieRelatie);
            pieRelatie.setVisibility(View.GONE);
        }
    }
    public void PieChart(){
        cvDate.setVisibility(View.INVISIBLE);
        cvDate.setMaxDate(cvDate.getDate());
        int i = pieChartData.size();
        Itemlabels = new String[i];
        Date date = new Date();
        if(i>0){
            i = 0;
            Itementries = new ArrayList<>();
            for (UspMobGetPersonItemTotal usage : pieChartData) {
                Itemlabels[i] = usage.ItemName;
                i++;
            }

            date.setTime(cvDate.getDate());
            LoadDayGraph(Itemlabels, df.format(date));
            pie.setVisibility(View.VISIBLE);
        }
        else {
            tvNodata.setText("The are no recordings for "+df.format(date));
            tvNodata.setVisibility(View.VISIBLE);
            pie.setVisibility(View.INVISIBLE);}

        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDate.setVisibility(View.VISIBLE);
                btnCalender.setVisibility(View.INVISIBLE);
                if (pie.getVisibility() == View.VISIBLE) {
                    pie.setVisibility(View.INVISIBLE);
                    v.setVisibility(View.INVISIBLE);
                } else {
                    tvNodata.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
