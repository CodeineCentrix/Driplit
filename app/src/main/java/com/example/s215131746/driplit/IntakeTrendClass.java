package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class IntakeTrendClass extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intake_trend, container, false);

        final BarChart bcTrend = rootView.findViewById(R.id.bcTrends);

        final Random rNum = new Random();

        TextView noTrend = rootView.findViewById(R.id.textView3);
        noTrend.setText("Trends");
        final List<BarEntry> entries = new ArrayList<>();
        final List<BarEntry> entriesWeeks = new ArrayList<>();

        final float[] y = {0};
        final String[] labels = {"Mon","Tue","Wen","Thu","Fri","Sta","Sun"};
        final String[] labelWeeks = {"Week 1","Week 2","Week 3","Week 4"};
        for(float x = 0; x< labels.length ;x++)
        {
            y[0] = rNum.nextInt(30)+2f;
            entries.add(new BarEntry(x,y));
        }
        for(float x = 0; x< labelWeeks.length ;x++)
        {
            y[0] = rNum.nextInt(30)+2f;
            entriesWeeks.add(new BarEntry(x, y[0]));
        }
        final TextView tvChartLAbel = rootView.findViewById(R.id.tvChartLAbel);

        SetUpGraph(bcTrend,entries,labels,tvChartLAbel);

        Button btnShow = rootView.findViewById(R.id.btnShowMore);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvChartLAbel.getText().toString()!="Weeks")
                {
                    SetUpGraph(bcTrend,entries,labels,tvChartLAbel);
                }
                else
                {
                    SetUpGraph(bcTrend,entriesWeeks,labelWeeks,tvChartLAbel);
                }
            }
        });

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
        if(tvChartLAbel.getText().toString()!="Weeks")
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
}