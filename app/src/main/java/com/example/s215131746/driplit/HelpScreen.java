package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.LineRadarDataSet;

import Adapters.SliderAdapter;

public class HelpScreen extends AppCompatActivity {

    Button btnReportLeak;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdpater;
    private TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //code for back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);

        sliderAdpater = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdpater);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(dotListener);
    }
    //Method To Create Dots On Help Screen
    public void addDotsIndicator(int position)    {
        dots = new TextView[2];
        dotLayout.removeAllViews(); //preventing other dots from being created

        for(int i  = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;")); //Code to create the dots
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white));

            dotLayout.addView(dots[i]);


        }

        if(dots.length > 0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.blue));
        }
    }
    ViewPager.OnPageChangeListener dotListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void BackClicked() {

        finish();
    }
    public void ToIntakeHelp(View view) {
        Intent ToIntakeHelp = new Intent(getApplicationContext(), IntakeHelper.class);
        startActivity(ToIntakeHelp);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
