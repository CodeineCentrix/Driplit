package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HelpScreen extends AppCompatActivity {
    Button btnBack;
    Button btnReportLeak;
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
    }
    public void BackClicked() {
        Intent toMenu = new Intent(getApplicationContext(), Mainmenu.class);
        startActivity(toMenu);
        finish();
    }
}
