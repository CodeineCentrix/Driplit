package com.example.s215131746.driplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        bll business = new bll();
        EditText email = findViewById(R.id.txtUsername);

        String value = email.getText().toString();
        String[] loginDetaisl = business.Person(value);
        if(loginDetaisl[0]!=null)
        {
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            startActivity(fodScreen);

            Toast.makeText(this,"Hello "+loginDetaisl[0],Toast.LENGTH_LONG).show();
        }
        else
        {
            TextView tvError = findViewById(R.id.tvError);
            tvError.setText("You have entered an invalid email or password");
            Toast.makeText(this,"You have enter an invalid email or password",Toast.LENGTH_LONG).show();
        }

    }

}
