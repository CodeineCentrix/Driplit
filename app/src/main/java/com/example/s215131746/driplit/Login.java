package com.example.s215131746.driplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {
    Intent registerScreen, fodScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void ToRegisterScreen(View view)
    {
        registerScreen = new Intent(getApplicationContext(), Register.class);
        startActivity(registerScreen);
    }
    public void ToFODScreen(View view)
    {
        fodScreen = new Intent(getApplicationContext(), FODScreen.class);
        startActivity(fodScreen);
    }
}
