package com.example.s215131746.driplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {
    bll business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        EditText txtFullname = findViewById(R.id.txtRegFullName);
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPhoneNumber =findViewById(R.id.txtPhoneNumber);
        EditText txtPassword = findViewById(R.id.txtPassword);
        if (business!=null)
        business = new bll();

            String[] person = business.Person("anathi.roux@gmail.com");


            txtFullname.setText(person[0]);
            txtEmail.setText(person[1]);
            txtPhoneNumber.setText(person[2]);
            txtPassword.setText(person[3]);

    }
}
