package com.example.s215131746.driplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import viewmodels.PersonModel;

public class EditProfile extends AppCompatActivity {
    GeneralMethods m;
    EditText txtFullname;
    EditText txtEmail;
    DBAccess business;
    EditText txtPassword;
    PersonModel person = new PersonModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        person = new PersonModel();
        m = new GeneralMethods(getApplicationContext());
        txtFullname = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtUserEmail);

        txtPassword = findViewById(R.id.txtPassword);
        business = new DBAccess();

        String[] p = m.Read("person.txt",",");
        txtFullname.setText(p[1]);
        txtEmail.setText(p[2]);

        person.id =Integer.parseInt(p[0]);
        person.fullName = p[1];
        person.userPassword = p[3];
        //txtPassword.setText(person[3]);

    }
    public void ToHome(View v)
    {
        Intent mainMenu = new Intent(getApplicationContext(), Mainmenu.class);
        startActivity(mainMenu);
        finish();
    }
    public void ToUpdatePerson(View v)
    {
        //if(person.userPassword==txtPassword && txtNewPassword.getText() != "")
        //person.userPassword = txtNewPassword.getText()
//        boolean error = false;
//        String[] values = {""+person.id, person.fullName,person.userPassword};
//        for(int i = 0; i<values.length;i++)
//        {
//            if(values[i].equalsIgnoreCase(""))
//            {
//                error = true;
//
//            }
//            else
//            {
//
//            }
//        }
//        if(!error)
//            business.uspMobUpdatePerson(person);
        Toast.makeText(getApplicationContext(),"Updated Profile",Toast.LENGTH_SHORT).show();
        ToHome(v);
    }
}
