package com.example.s215131746.driplit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.Permissions;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class HelpScreen extends AppCompatActivity {


    Button btnBack;
    Button btnReportLeak;
    TextView txtCords;

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
