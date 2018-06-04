package com.example.s215131746.driplit;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Login extends AppCompatActivity {
    Intent registerScreen, fodScreen;
    public  EditText email;
    public  EditText password;
    bll business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        //should be taken out before judging
        email.setText("kanye@gmail.com");
        password.setText("kanye");
        business = new bll();
    }
    public void ToRegisterScreen(View view)
    {
openWebPage("iis");
        //registerScreen = new Intent(getApplicationContext(), Register.class);
        //startActivity(registerScreen);
    }
    public void openWebPage(String url) {
         Uri webpage = Uri.parse(url);
          Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
             startActivity( intent);
         }
    }
    public void ToFODScreen(View view)
    {
        PersonModel person = new PersonModel();
        person.email = email.getText().toString();
        person.userPassword = password.getText().toString();
        person = business.LoginPerson(person);

        if(person.fullName!=null && !person.fullName.equals(""))
        {
            email.setText("");
            password.setText("");
            Toast.makeText(this,"Hello "+person.fullName,Toast.LENGTH_LONG).show();
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            startActivity(fodScreen);
        }
        else
        {
            TextView tvError = findViewById(R.id.tvError);
            tvError.setText(R.string.login_error);
            Toast.makeText(this,"You have enter an invalid email or password",Toast.LENGTH_LONG).show();
        }

    }

}
