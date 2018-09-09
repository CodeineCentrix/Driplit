package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import Helpers.BackgroundTip;
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

        if(oldDate.equals("") || !oldDate.equals(todayDate)){
            TipModel ranTip = business.GetRandomTips();
            m.writeToFile(todayDate+","+ranTip.TipDescription,"Tip.txt");
            txtTod.setText(ranTip.TipDescription);
        }else if(oldDate.equals(todayDate)){
            txtTod.setText(m.Read("Tip.txt",",")[1]);
        }

    }
    public void ToHome(View v){
        Intent back = new Intent(this, BackgroundTip.class);
        startService(back);
        Intent mainMenu = new Intent(getApplicationContext(), Mainmenu.class);
        startActivity(mainMenu);
        finish();
    }

}
