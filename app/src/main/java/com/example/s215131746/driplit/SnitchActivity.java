package com.example.s215131746.driplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import Adapters.SnitchAdapter;

public class SnitchActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snitch_list_layout);


        ListView snitchList = (ListView)findViewById(R.id.lstSnitchList);

        SnitchAdapter snitch = new SnitchAdapter();

        snitchList.setAdapter(snitch);

    }


}
