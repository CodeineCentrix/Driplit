package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import viewmodels.PersonModel;
import viewmodels.UspMobGetPersonTotalUsage;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class IntakeTrendClass extends Fragment {

    final List<BarEntry> entries = new ArrayList<>();
    final ArrayList<Entry> lineEntry = new ArrayList<>();
    float averageUsage;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intake_trend, container, false);
        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);
        DBAccess business = new DBAccess();
        GeneralMethods m = new GeneralMethods(getContext());
        String index2 = m.Read(this.getString(R.string.person_file_name),",")[PersonModel.EMAIL];
        averageUsage =0;
        ArrayList<UspMobGetPersonTotalUsage> usages = business.GetPersonTotalUsageGetItems(index2);
        int i = usages.size();
        if(i>0){
            final String[] labels = new String[i];
            i = 0;
            for (UspMobGetPersonTotalUsage usage:usages) {
                labels[i] = usage.UsageDay;
                averageUsage+=usage.UsageAmount;
                entries.add(new BarEntry(i,usage.UsageAmount));
                lineEntry.add(new Entry(i,usage.UsageAmount));
                i++;
            }
            averageUsage/=i;
            SetUpGraph(bcTrend,entries,labels);
            //Line chart___________________________________________________________
            LineChart lineChart = rootView.findViewById(R.id.lineChart);
            // creating list of entry
            LineDataSet dataset = new LineDataSet(lineEntry, "line water usage");//loading the top labels and setting the bottom label
            lineChart.setData(new LineData(dataset)); // set the data and list of lables into chart
            IAxisValueFormatter formatter = setYaxis(labels);
            lineChart.getAxisLeft().setAxisMinimum(0f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.getXAxis().setValueFormatter(formatter);
            lineChart.getXAxis().setLabelCount(labels.length-1);
            dataset.setDrawFilled(true);
        }
        return rootView;
    }

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
    public void SetUpGraph(BarChart  bcTrend ,List<BarEntry> entries,String[] Labels ){
        if(entries.size()>0) {
            IAxisValueFormatter formatter = setYaxis(Labels);
            BarDataSet set = new BarDataSet(entries, "50 liters is the daily recommendation");
            set.setColor(R.color.black);// not sure if this worsks it sts the font color black
            BarData b = new BarData(set);

            b.setBarWidth(0.5f);//bar width
            bcTrend.setData(b);
            bcTrend.getXAxis().setGranularity(1f);//set the interval between labels so they do not duplicate
            bcTrend.getXAxis().setTextSize(10f);
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


}