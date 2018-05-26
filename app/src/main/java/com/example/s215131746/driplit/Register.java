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
    String fullname, email, password, phoneNumber;

   EditText txtFullName, txtEmail, txtPassword, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //*Getting values from the controls*//*
        txtFullName = findViewById(R.id.txtRegFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtRegPassword);
        txtPhone = findViewById(R.id.txtPhone);

       Button btnRegister = findViewById(R.id.btnRegister);
       btnRegister.setOnClickListener(new View.OnClickListener() {//OnClick method for the Register Button
          @Override
          public void onClick(View v) {
              fullname = txtFullName.getText().toString();
              email = txtEmail.getText().toString();
              password = txtPassword.getText().toString();
              phoneNumber = txtPhone.getText().toString();

              bll bus_logic = new bll();
              boolean i;
              try
              {
                  i = bus_logic.MobAddPerson(fullname, email, password, phoneNumber);
                  if (i)
                  {
                      Toast.makeText(Register.this, "Shit finally WORKS", Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                      Toast.makeText(Register.this,  "Shit still won't WORK", Toast.LENGTH_SHORT).show();
                  }
              }
              catch(SQLException s)
              {
                  Toast.makeText(Register.this, s+"", Toast.LENGTH_SHORT).show();
              }


           }
       });
        //OnClick Code Ends Here
    }

    /**
     * Code for Registering
     */
    public boolean ToRegisterUser()
    {

        boolean i = false;
        bll businessLogic = new bll();
        try
        {
            fullname = txtFullName.getText().toString();
            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();
            phoneNumber = txtPhone.getText().toString();
          i =  businessLogic.MobAddPerson(fullname, email, password, phoneNumber);
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
