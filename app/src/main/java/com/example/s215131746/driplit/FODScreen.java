package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import viewmodels.TipModel;

public class FODScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_fodscreen);
        TextView txtTod = findViewById(R.id.txtTod);
        DBAccess business = new DBAccess();

        GeneralMethods m = new GeneralMethods(getApplicationContext());
        String oldDate =m.Read("Tip.txt",",")[0], todayDate =  m.GetDate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //code for back button
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
        String tip;
        try {
             tip= m.Read("Tip.txt", ",")[1];
        }catch (Exception e)
        {
            tip=null;
        }
        if(oldDate.equals("") || !oldDate.equals(todayDate) || tip.equals("null") ){
            TipModel ranTip = business.GetRandomTips();
            m.writeToFile(todayDate+","+ranTip.TipDescription,"Tip.txt");
            txtTod.setText(ranTip.TipDescription);
        }else if(oldDate.equals(todayDate)){
            txtTod.setText(tip);
        }

    }
    public void ToHome(View v){
       /* Intent back = new Intent(this, BackgroundTip.class);
        startService(back);*/
        Bundle tab = getIntent().getExtras();
        if(tab==null){
            Intent intent = new Intent(this, TabMenu.class);
            Bundle i = new Bundle();
            i.putString("Tab", "Record");
            intent.putExtras(i);
            startActivity(intent);
        }
        finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
