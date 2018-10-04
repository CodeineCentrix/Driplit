package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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
    ArrayList<UspMobGetPersonTotalUsage> usages;
    ArrayList<UspMobGetPersonItemTotal> ItemUsages;
    private ImageView[] moreOrLess=null;
    private   TextView[] desc ;
    PieChart pie;
    String []person =null;
    List<PieEntry> pieEntries;
    private String longestLabel;
    ArrayList<Integer> colors;
    private Legend legend;
    ArrayList<ResidentUsageModel> MobGetPersonUsage;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_intake_trend_scroller, container, false);
        //_______________________________________________________________
         pie = rootView.findViewById(R.id.pies);
         pie.setHoleColor(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimaryDark));
        pie.setUsePercentValues(false);
        pie.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int i =Math.round(h.getDataIndex());
                if(i>=0&&i<Itemlabels.length) {
                    Toast.makeText(rootView.getContext(), Itemlabels[i], Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected() {

            }
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

        colors = new ArrayList<Integer>();

        for (int c : GeneralMethods.MATERIAL_COLORS)
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
        final DecimalFormat dc = new DecimalFormat("0.0");
        //from item trend
        cvDate  = rootView.findViewById(R.id.cvDate);
        tvNodata = rootView.findViewById(R.id.tvNoData);
       // barChart =  rootView.findViewById(R.id.bcT);
        btnCalender = rootView.findViewById(R.id.btnSelectDate);
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
               // barChart.setVisibility(View.VISIBLE);

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
        LinearLayout linearLayout2 = rootView.findViewById(R.id.linearLayout2);
        Animation upToDown = AnimationUtils.loadAnimation(rootView.getContext(),R.anim.uptodown);
        //linearLayout2.setAnimation(upToDown);

        Animation anime = AnimationUtils.makeInAnimation(rootView.getContext(),false);
        anime.setDuration(2000);
        linearLayout2.setAnimation(anime);

        return rootView;
    }
    public void dateSelected(String date){

        String dateV = date, index2 = person[PersonModel.EMAIL];

        btnCalender.setVisibility(View.VISIBLE);

        averageUsage =0;

        pieChartData = business.uspMobGetPersonItemTotalDate(index2,dateV);

        int x = pieChartData.size();
        final String[] Itemlabels = new String[x];
        x = 0;
        ArrayList<BarEntry> Itementries = new ArrayList<>();
        pieEntries = new ArrayList<>();
        float smallerSlice =99;
        for(UspMobGetPersonItemTotal usage : pieChartData) {
            Itemlabels[x] = usage.ItemName;
            averageUsage+=usage.UsageAmount;
            //Itementries.add(new BarEntry(x, usage.UsageAmount));
            smallerSlice = Math.min(smallerSlice,usage.UsageAmount);
            pieEntries.add(new PieEntry(usage.UsageAmount,  usage.ItemName));
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
           // barChart.getXAxis().setLabelRotationAngle(90f);//rotate the labels of calender graph
        }

        LoadDayGraph(Itemlabels, dateV);
    }
    public void afterConnection(final View rootView){
        cvDate.setVisibility(View.INVISIBLE);
        cvDate.setMaxDate(cvDate.getDate());
        averageUsage =0;
        int i = ItemUsages.size();
        Itemlabels = new String[i];
        if(i>0){
            i = 0;
            Itementries = new ArrayList<>();
            for (UspMobGetPersonItemTotal usage : ItemUsages) {
                Itemlabels[i] = usage.ItemName;
                averageUsage+=usage.UsageAmount;
                //Itementries.add(new BarEntry(i, usage.UsageAmount));
                i++;
            }


            averageUsage/=i;
           // SetUpGraph(barChart, Itementries, Itemlabels,averageUsage);
            Date date = new Date();
            date.setTime(cvDate.getDate());
            LoadDayGraph(Itemlabels, df.format(date));
            pie.setVisibility(View.VISIBLE);
        }
        else {
            pie.setVisibility(View.GONE);}

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


//        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//
//                int i =Math.round(e.getX());
//                try {
//                    Toast.makeText(rootView.getContext(),
//                            Itemlabels[i-1],Toast.LENGTH_LONG).show();
//                }catch (Exception ex){
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
        //end


        //from trends
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);

        i = usages.size();
        LineChart lineChart = rootView.findViewById(R.id.lineChart);
        if(i>0) {
            final String[] labels = new String[i+1];
            labels[0]="";
            entries.add(new BarEntry(0, 0));
            i = 1;
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
            averageUsage/=i-1;

            if(entries.size()>0) {
                IAxisValueFormatter formatter = setYaxis(labels);
                BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");

                final DecimalFormat dc = new DecimalFormat("0.0");
                MyBarDataSet setColor = new MyBarDataSet(entries,"Average water usage: "+dc.format(averageUsage),set);
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
                try{
                    l.getEntries()[0].label = "Below "+person[PersonModel.USAGETARGET]+" Litres";
                    l.getEntries()[1].label ="Above "+person[PersonModel.USAGETARGET]+" Litres";
                }catch (Exception e){}
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
                // bcTrend.getAxisLeft().setAxisMinimum(0f);
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

            }else {

                bcTrend.setNoDataText("No water usage recoded");
            }
            //bcTrend.getXAxis().setEnabled(false); top legends
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
            Legend l = lineChart.getLegend();
            l.setWordWrapEnabled(true);
            l.setTypeface(Typeface.DEFAULT);
            l.setFormSize(0f);
            l.setTextSize(0f);

            lineChart.setData(new LineData(dataset)); // set the data and list of labels into chart
            IAxisValueFormatter formatter = setYaxis(labels);
            //lineChart.getAxisLeft().setAxisMinimum(0f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.getXAxis().setEnabled(false);
            lineChart.getDescription().setEnabled(false);
            lineChart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lineChart.getAxisLeft().setTextSize(16f);
            lineChart.getLineData().setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
            lineChart.getLineData().setValueTextSize(16f);
            lineChart.getLineData().setHighlightEnabled(false);
            YAxis y = lineChart.getAxisLeft();
           try {
               LimitLine ll = new LimitLine(Integer.parseInt(person[PersonModel.USAGETARGET]), "My "+person[PersonModel.USAGETARGET]+" Litre Target ");
               ll.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
               ll.setTextSize(16f);
               ll.setLineColor(ContextCompat.getColor(getContext(), R.color.red));
               y.addLimitLine(ll);
           }catch (Exception e){

           }
            lineChart.getXAxis().setValueFormatter(formatter);
            lineChart.getXAxis().setLabelCount(labels.length - 1);
            dataset.setDrawFilled(true);
            lineChart.buildDrawingCache();

            //end
            lineChart.invalidate();
        }else{

            lineChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bcTrend.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            lineChart.setVisibility(View.GONE);
            bcTrend.setVisibility(View.GONE);
            RelativeLayout pieRelatie  = rootView.findViewById(R.id.pieRelatie);
            pieRelatie.setVisibility(View.GONE);
        }
    }
    //from item trend
    public void LoadDayGraph(String[] l, String date){
        btnCalender.setVisibility(View.VISIBLE);
        if(l.length>0){
//            pie.setExtraOffsets(0f, 60f, 20f, 20f);
//            pie.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0);
            tvNodata.setVisibility(View.INVISIBLE);
            pie.setVisibility(View.VISIBLE);
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
    //Bar graph set up
    public void SetUpGraph(BarChart bcTrend , List<BarEntry> entries,final String[] Labels ,float avg){
        if(entries.size()>0) {
            IAxisValueFormatter formatter = setYaxis(Labels);
            BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");
            bcTrend.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                    int i =Math.round(e.getX());
                    try {
                        Toast.makeText(rootView.getContext(),
                                Labels[i],Toast.LENGTH_LONG).show();
                    }catch (Exception ex){

                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
            final DecimalFormat dc = new DecimalFormat("0.0");
            MyBarDataSet setColor = new MyBarDataSet(entries,"Average water usage: "+dc.format(avg),set);
//            setColor.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
//                    ContextCompat.getColor(getContext(), R.color.red)});

            ArrayList<BarDataSet> dataSets = new ArrayList<>();
            dataSets.add(setColor);
            BarData b = new BarData(dataSets.get(0));
            //bcTrend.getAxis(null).setTextColor(R.color.colorAccent);
            b.setBarWidth(0.5f);//bar width
            bcTrend.setData(b);
            Legend l = bcTrend.getLegend();

            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(0f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            bcTrend.getXAxis().setGranularity(1f);//set the interval between labels so they do not duplicate
            // bcTrend.getAxisLeft().setAxisMinimum(0f);
            bcTrend.getAxisLeft().setDrawGridLines(false);//removes the grid lines of the bar graph
            bcTrend.getXAxis().setAxisLineWidth(10f);
            bcTrend.getXAxis().mLabelWidth = 50;
            bcTrend.invalidate();
            bcTrend.getXAxis().setLabelRotationAngle(25);
            longestLabel = bcTrend.getXAxis().getLongestLabel();
            bcTrend.getAxisRight().setEnabled(false);
            bcTrend.getXAxis().setValueFormatter(formatter);// uses the an interface to get labels
            bcTrend.getXAxis().setLabelCount(Labels.length);
            bcTrend.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            bcTrend.getBarData().setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
            bcTrend.fitScreen();//obviuosly its to make sure the graph fits the screen
            bcTrend.invalidate();
            bcTrend.getXAxis().setTextSize(16f);
            bcTrend.getBarData().setValueTextSize(16f);//Set font size
            bcTrend.setVisibleXRangeMaximum(5f);// sets the number of bars to be shown at a given moment
            bcTrend.moveViewToX(entries.size());
        }
    }
    public void SetUpGraph(PieChart pie , List<PieEntry> pieEntries){
        PieDataSet pieSet = new PieDataSet(pieEntries, "");
        pieSet.setColors(new int[]{ContextCompat.getColor(rootView.getContext(), R.color.green),
                ContextCompat.getColor(rootView.getContext(), R.color.red),
                ContextCompat.getColor(rootView.getContext(), R.color.blue)});
        pie.setUsePercentValues(true);
        PieData data = new PieData(pieSet);
        pieSet.setSliceSpace(10f);
        pie.setData(data);
        pie.getData().setValueTextSize(16f);
        pie.getData().setValueTextColor(ContextCompat.getColor(rootView.getContext(), R.color.white));
        pie.invalidate();
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
                    String email = m.Read(context.getString(R.string.person_file_name),",")[PersonModel.EMAIL];
                    String dateV = df.format(date);


                    ItemUsages = business.uspMobGetPersonItemTotal(email);
                    pieChartData = business.uspMobGetPersonItemTotalDate(email, dateV);
                    usages = business.GetPersonTotalUsageGetItems(email);
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
