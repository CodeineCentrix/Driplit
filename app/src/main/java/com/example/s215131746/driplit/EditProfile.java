package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    EditText txtUsageTarget;
    EditText txtNewPassword;
    PersonModel person = new PersonModel();
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //code for back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        person = new PersonModel();
        m = new GeneralMethods(getApplicationContext());
        txtFullname = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtUserEmail);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsageTarget = findViewById(R.id.txtUsageTarget);

        business = new DBAccess();

        String[] p = m.Read(this.getString(R.string.person_file_name),",");
        txtFullname.setText(p[PersonModel.FULLNAME]);
        txtEmail.setText(p[PersonModel.EMAIL]);
        txtUsageTarget.setText(p[PersonModel.USAGETARGET]);
        person.id =Integer.parseInt(p[PersonModel.ID]);
        person.fullName = txtFullname.getText().toString();
        person.email = txtEmail.getText().toString();
        person.userPassword = p[PersonModel.PASSWORD];
        person.getOldapproved = Integer.parseInt(p[PersonModel.OLDAPPROVED]);
        //txtPassword.setText(person[3]);
    }
    public void ToUpdatePerson(View v){
        person.Usagetarget =Integer.parseInt(txtUsageTarget.getText().toString());
        person.fullName = txtFullname.getText().toString();
        boolean error = false;
        if(person.userPassword.equals(txtPassword.getText().toString())){
            String[] values = {""+person.fullName,person.userPassword,txtNewPassword.getText().toString()};
            //new password
            if(!values[values.length-1].equalsIgnoreCase("")){
                person.userPassword = values[values.length-1];
            }

            for(int i = 0; i<values.length-1;i++){
                if(values[i].equalsIgnoreCase("")){
                    error = true;
                }
            }
        }
        else {
            error = true;
        }
        if(!error){
            helpThread h = new helpThread(true,this);
            new Thread(h).start();

        }else {
            Toast.makeText(getApplicationContext(),"No Changes",Toast.LENGTH_SHORT).show();
        }
    }
    public void afterConnection(View v){
        m.writeToFile(person.toString(),"person.txt");
        Toast.makeText(getApplicationContext(),"Updated Profile",Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    class helpThread implements Runnable {
        Context context;
        boolean onCreate;
        boolean isConnecting;
        Snackbar mySnackbar;
        public helpThread() {

        }

        public helpThread(boolean onCreate,Context context) {
            this.onCreate = onCreate;
            this.context = context;
        }


        @Override
        public void run() {
            if(onCreate){

                isConnecting = business.isConnecting();
                if(isConnecting) {

                    business.MobUpdatePerson(person);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            afterConnection(txtNewPassword);
                        }
                    });
                }else {
                    mySnackbar = Snackbar.make(txtNewPassword,"No Connection", 8000);
                    mySnackbar.getView().setBackgroundColor(Color.RED);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mySnackbar.show();

                        }
                    });

                }
            }
        }
    }
}
