package com.example.s215131746.driplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.String;
import java.sql.SQLException;

public class Register extends AppCompatActivity {
    /*Variables to store register details*/
    String fullname, email, password, confirmPassword;

   EditText txtFullName, txtEmail, txtPassword, txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


       Button btnRegister = findViewById(R.id.btnRegister);
       btnRegister.setOnClickListener(new View.OnClickListener() {//OnClick method for the Register Button
          @Override
          public void onClick(View v) {
              RegisterUser();
           }
       });
        //OnClick Code Ends Here
    }

    /**
     * Code for Registering
     */
    public boolean RegisterUser()
    {
        //*Getting values from the controls*//*
        txtFullName = (EditText)findViewById(R.id.txtRegUsername);
        txtEmail = (EditText)findViewById(R.id.txtPhoneNumber);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPass);
        boolean i = false;
        bll businessLogic = new bll();
        try
        {
          i =  businessLogic.MobAddPerson(txtFullName.getText().toString(), txtEmail.toString(), txtPassword.toString(), txtConfirmPassword.toString());
        }
        catch (SQLException e)
        {
            Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show();
        }

        return i;
    }
}
