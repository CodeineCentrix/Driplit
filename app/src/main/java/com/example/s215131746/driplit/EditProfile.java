package com.example.s215131746.driplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {
    bll business;
    EditText txtFullname;
    EditText txtEmail;
    EditText txtPhoneNumber;
    EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        txtFullname = findViewById(R.id.txtRegFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber =findViewById(R.id.txtPhoneNumber);
        txtPassword = findViewById(R.id.txtPassword);
    }
}
