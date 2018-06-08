package com.example.s215131746.driplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {
    GeneralMethods m;
    EditText txtFullname;
    EditText txtEmail;

    EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        m = new GeneralMethods(getApplicationContext());
        txtFullname = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtUserEmail);

        txtPassword = findViewById(R.id.txtPassword);


        String[] person = m.Read("person.txt",",");
        txtFullname.setText(person[1]);
        txtEmail.setText(person[2]);
        //txtPassword.setText(person[3]);

    }
    public void ToHome(View v)
    {
        Intent mainMenu = new Intent(getApplicationContext(), Mainmenu.class);
        startActivity(mainMenu);
        finish();
    }
}
