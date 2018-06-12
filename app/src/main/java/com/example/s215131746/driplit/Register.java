package com.example.s215131746.driplit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.String;

import viewmodels.PersonModel;

public class Register extends AppCompatActivity {
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
    }
    /**
     * Code for Registering
     */
    public void ToRegisterUser(View view)
    {
        PersonModel person = new PersonModel();
        person.fullName = txtFullName.getText().toString();
        person.email = txtEmail.getText().toString();
        person.userPassword = txtPassword.getText().toString();

        //Validation missing
        DBAccess bus_logic = new DBAccess();

            //validate EditText if they are not empty
            if (ValidPerson(person))
            {
                if (bus_logic.MobAddPerson(person))
                {
                    Toast.makeText(Register.this, "hello"+person.fullName, Toast.LENGTH_SHORT).show();
                    Intent showMainMene = new Intent(getApplicationContext(),Mainmenu.class);
                    startActivity(showMainMene);
                    finish();
                }
            }
            else
            {
                Toast.makeText(Register.this,  "One or more fields are empty ", Toast.LENGTH_LONG).show();
            }



    }

    public boolean ValidPerson(PersonModel person)
    {
        boolean error = true;
        TextView[] tvError = {findViewById(R.id.tvErrorFullName),findViewById(R.id.tvErrorEmail),findViewById(R.id.tvErrorPassword)};
        String[] values = {person.fullName,person.email,person.userPassword};
        for(int i = 0; i<tvError.length;i++)
        {
            if(values[i].equalsIgnoreCase(""))
            {
                error = false;
                tvError[i].setText("*");
                tvError[i].setTextColor(Color.RED);
            }
            else
            {
                tvError[i].setTextColor(Color.TRANSPARENT);
            }
        }
        return  error;
    }
}
