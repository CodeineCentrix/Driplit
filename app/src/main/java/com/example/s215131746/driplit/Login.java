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
    public static final String EXTRAEmail = "com.example.s215131746.driplit.email";
    public static final String EXTRAPassword = "com.example.s215131746.driplit.password";
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
        EditText password = findViewById(R.id.txtPassword);
        //email.setText("kanye@gmail.com");
        //password.setText("kanye");
        String Semail = email.getText().toString();
        String Spassword = password.getText().toString();
        String[] loginDetaisl = business.Person(Semail,Spassword);
        if(loginDetaisl[0]!=null && loginDetaisl[0]!="")
        {
            Intent intent = new Intent(this,TabMenu.class);
            intent.putExtra(EXTRAEmail,Semail);
            intent.putExtra(EXTRAPassword,Spassword);
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            startActivity(fodScreen);
            email.setText("");
            password.setText("");
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
