package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Login extends AppCompatActivity {
    Intent registerScreen, fodScreen;
    public  EditText email;
    public  EditText password;
    DBAccess business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        //should be taken out before judging
        email.setText("kanye@gmail.com");
        password.setText("kanye");
        business = new DBAccess();

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
            Toast.makeText(this,    "Hello "+person.fullName,Toast.LENGTH_LONG).show();
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            writeToFile(person.id+","+ person.toString(),getApplicationContext(),"person.txt");
            startActivity(fodScreen);
            finish();
        }
        else
        {
            TextView tvError = findViewById(R.id.tvError);
            tvError.setText(R.string.login_error);
            Toast.makeText(this,"Invalide email or password",Toast.LENGTH_SHORT).show();
        }

    }
    public void writeToFile(String data,Context context,String fileName) {
        BackGroundConnection connectionProperties = new BackGroundConnection();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFromFile(Context context,String fileName) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
