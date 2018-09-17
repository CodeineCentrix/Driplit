package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.data.LineRadarDataSet;

import Adapters.SliderAdapter;

public class HelpScreen extends AppCompatActivity {
    Button btnBack;
    Button btnReportLeak;

    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackClicked();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);

        sliderAdpater = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdpater);
    }
    public void BackClicked() {
        Intent toMenu = new Intent(getApplicationContext(), Mainmenu.class);
        startActivity(toMenu);
        finish();
    }
    public void ToIntakeHelp(View view) {
        Intent ToIntakeHelp = new Intent(getApplicationContext(), IntakeHelper.class);
        startActivity(ToIntakeHelp);

    }

}
