package com.example.s215131746.driplit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent ShowIntake = new Intent(getApplicationContext(),RecordWaterIntake.class);
        startActivity(ShowIntake);

    }
}
