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

   EditText txtFullName, txtEmail, txtPassword, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


       Button btnRegister = findViewById(R.id.btnRegister);
       btnRegister.setOnClickListener(new View.OnClickListener() {//OnClick method for the Register Button
          @Override
          public void onClick(View v) {
              ToRegisterUser();
           }
       });
        //OnClick Code Ends Here
    }

    /**
     * Code for Registering
     */
    public boolean ToRegisterUser()
    {
        //*Getting values from the controls*//*
        txtFullName = (EditText)findViewById(R.id.txtRegFullName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtRegPassword);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        boolean i = false;
        bll businessLogic = new bll();
        try
        {
            String[] input = new String[4];

            input[0] = txtFullName.getText().toString();
            input[1] = txtEmail.getText().toString();
            input[2] = txtPassword.getText().toString();
            input[3] = txtPhone.getText().toString();
          i =  businessLogic.MobAddPerson(input[0],input[1] ,input[2] ,input[3] );
          if(i)
          {
              Toast.makeText(this,"Succesfully registered", Toast.LENGTH_SHORT).show();
          }
          else
          {
              Toast.makeText(this,"Please Enter missing values", Toast.LENGTH_SHORT).show();
          }
        }
        catch (SQLException e)
        {
            Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show();
        }

        return i;
    }
}
