package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import viewmodels.PersonModel;
import viewmodels.Surburb;

public class Register2 extends AppCompatActivity {
    EditText txtFullname;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtHouseNumber,txtHouseStreet;
    EditText txtNewPassword;
    String city,type;
    int suburb;
    GeneralMethods m;
    RadioButton rdMain,rdResident;
    PersonModel person = new PersonModel();
    DBAccess dbAccess = new DBAccess();
    Handler h = new Handler();
    boolean isRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tvCity = findViewById(R.id.tvCity);
        Spinner spCity = findViewById(R.id.spCity);
        TextView tvSuburb = findViewById(R.id.tvSuburb);
        Spinner spSuburb = findViewById(R.id.spSuburb);
        SetControllers();
        final ArrayList<Surburb> surburb = dbAccess.WEBSuburbs();
        final ArrayList<Surburb> cities = dbAccess.WEBCities();

         m = new GeneralMethods(getApplicationContext());

        String[] details = m.Read("person.txt",",");
        final ArrayAdapter<Surburb> cityAdapter = new ArrayAdapter<Surburb>(
                this,android.R.layout.simple_spinner_item,cities){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setText(cities.get(position).SurburbName);
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setText(cities.get(position).SurburbName);
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };



//        spinnerArrayAdapter.addAll(details);
        spCity.setAdapter(cityAdapter);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){
                    //tvCity.setText("Selected : " + selectedItemText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final ArrayAdapter<Surburb> burbAdapter = new ArrayAdapter<Surburb>(
                this,android.R.layout.simple_spinner_item,surburb){
            @Override
            public boolean isEnabled(int position){
             /*   if(position == 0)
                {
                    return false;
                }
                else
                {*/
                    return true;
//                }
            }

            @Override
            public int getCount() {
                return surburb.size();
            }

            @Nullable
            @Override
            public Surburb getItem(int position) {
                return surburb.get(position);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setText(surburb.get(position).SurburbName);

                }
                else {
                    tv.setText(surburb.get(position).SurburbName);

                }
                return view;
            }
        };
        spSuburb.setAdapter(burbAdapter);
        spSuburb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > -1){
                    suburb = surburb.get(position).ID;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void ToRegister(View view){
        if(!txtFullname.getText().toString().equals("")) {
            if(txtEmail.getText().toString().contains("@")
                    && txtEmail.getText().toString().contains(".")) {
                if(!txtHouseNumber.getText().toString().equals("")) {
                    if(!txtPassword.getText().toString().equals("")) {
                        if(txtPassword.getText().toString().equals(txtNewPassword.getText()
                        .toString())) {
                            Runnable r = new helpThread(this);
                            new Thread(r).start();
                        }else {
                            txtNewPassword.requestFocus();
                            txtNewPassword.setError("Passwords do not match");
                        }
                    }else {
                        txtPassword.requestFocus();
                        txtPassword.setError("Required");
                    }
                }else {
                    txtHouseNumber.requestFocus();
                    txtHouseNumber.setError("Required");
                }
            }else {
                txtEmail.requestFocus();
                txtEmail.setError("Incorrect format");
            }
        }else{
            txtFullname.requestFocus();
            txtFullname.setError("Required");
        }
    }
    public void SetControllers(){
        txtFullname = findViewById(R.id.txtRegFullName);
        txtEmail = findViewById(R.id.txtRegEmail);
        txtPassword = findViewById(R.id.txtRegPassword);
        txtHouseNumber = findViewById(R.id.txtRegHouseNumber);
        txtHouseStreet = findViewById(R.id.txtRegStreetName);
        txtNewPassword = findViewById(R.id.txtRegReEnterPassword);
        rdMain = findViewById(R.id.rbMainRes);
        rdResident = findViewById(R.id.rbResident);
        rdResident.setChecked(true);
    }
    class helpThread implements Runnable {
        Context context;
        public helpThread(Context context) {
            this.context = context;

        }

        @Override
        public void run() {

            try {
                isRegistered = dbAccess.isConnecting();
                if (isRegistered){
                    isRegistered = dbAccess.WEBAddResident(txtFullname.getText().toString(), txtEmail.getText().toString(),
                            txtPassword.getText().toString(), txtHouseNumber.getText().toString(),
                            txtHouseStreet.getText().toString(), suburb, 1);
                if (isRegistered) {
                    PersonModel person = new PersonModel();
                    person.isAdmin = false;
                    person.email = txtEmail.getText().toString();
                    person.Usagetarget = 60;
                    person.getOldapproved = 0;
                    person.userPassword = txtPassword.getText().toString();
                    person = dbAccess.LoginPerson(person);
                    //writes the persons details to a screen which gets continually used
                    m.writeToFile(person.toString(), "person.txt");

                }
            }

            } catch (Exception e) {
                e.printStackTrace();
            }
            h.post(new Runnable() {
                @Override
                public void run() {
                    if(!isRegistered) {
                        try {

                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }else {
                        Toast.makeText(context,"Registration failed",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}


