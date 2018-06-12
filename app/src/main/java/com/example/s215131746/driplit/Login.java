package com.example.s215131746.driplit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import viewmodels.PersonModel;

public class Login extends AppCompatActivity {
    Intent registerScreen, fodScreen;
    public  EditText email;
    public  EditText password;
    public CheckBox cbRemeber;
    DBAccess business;
    GeneralMethods m;
    String[] details;
    TextView txtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new GeneralMethods(getApplicationContext());
       txtFeedback = findViewById(R.id.txtFeedback);
        setContentView(R.layout.activity_login);
        cbRemeber = findViewById(R.id.cbRememberMe);
        email = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        //should be taken out before judging
        RemeberMe();
        if(cbRemeber.isChecked())
        {
            details = m.Read("person.txt",",");
            email.setText(details[2]);
            password.setText(details[3]);
        }
        business = new DBAccess();
    }
    public void RemeberMe(String answ)
    {
        m.writeToFile(answ,"Remember.txt");
    }
    public void RemeberMe()
    {
        if(m.readFromFile("Remember.txt").equals("yes"))
        cbRemeber.setChecked(true);
        else
            cbRemeber.setChecked(false);
    }

    public void ToRegisterScreen(View view)
    {
        //m.openWebPage("http://sict-iis.nmmu.ac.za/codecentrix/IT2/Resources/View/log_in.php?from=mobile");

//        Uri webpage = Uri.parse("http://sict-iis.nmmu.ac.za/codecentrix/IT2/Controller/MainController.php?action=register_page&from=mobile");
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        if ( intent.resolveActivity(getPackageManager()) != null) {
//            startActivity( intent);
//        }
        registerScreen = new Intent(getApplicationContext(), Register2.class);
        startActivity(registerScreen);
    }
    public void openWebPage(String url) {

         Uri webpage = Uri.parse(url);
          Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
             startActivity( intent);
         }
    }
    public void ToFODScreen(View view)
    {
        if(cbRemeber.isChecked())
            RemeberMe("yes");
        else
            RemeberMe("no");
        PersonModel person = new PersonModel();
        person.email = email.getText().toString();
        person.userPassword = password.getText().toString();
//        if(person.email.equals(details[2])&&person.userPassword.equals(details[3]))
//        {
//            person.id = Integer.parseInt(details[0]);
//            person.fullName = details[1];
//            person.email = details[2];
//            person.userPassword =  details[3];
//            Toast.makeText(this,"PLEASE TURN ON WIFI",Toast.LENGTH_SHORT).show();
      //  }else{
        boolean wifi = true;
            try {
                person = business.LoginPerson(person);
                m.writeToFile(person.id+","+ person.toString(),"person.txt");
            }catch (NullPointerException e)
            {
                wifi = false;
            }

      //  }
        if(!wifi)
        {
            Toast.makeText(this,"PLEASE TURN ON WIFI",Toast.LENGTH_SHORT).show();
        }
        else if(person.fullName!=null && !person.fullName.equals(""))
        {
            Toast.makeText(this,    "Hello "+person.fullName,Toast.LENGTH_LONG).show();
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);

            startActivity(fodScreen);
            finish();
        }
        else
        {
            TextView tvError = findViewById(R.id.tvError);
            tvError.setText(R.string.login_error);
            Toast.makeText(this,"Invalide email or password",Toast.LENGTH_SHORT).show();
        }

    }




    public void GetLocation(View view) {

        Context con = view.getContext();
        LocationManager lm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //Checking For Request Permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);

            //Handling The Request Result
            //onRequestPermissionsResult(10, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, int grantResults[]);
            return;
        }


        if(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
        {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Geocoder geocoder;
            List<Address> addresses;
            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();

            geocoder = new Geocoder(this, Locale.getDefault());
            try
            {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                String address = addresses.get(0).getAddressLine(0);

                String fullAddress = address;
                if(address != null)
                {
                    txtFeedback = findViewById(R.id.txtFeedback);
                    txtFeedback.setText("Leak Successfully Reported!");
                }
                else
                {
                    Toast.makeText(view.getContext(), "OOPS! Something went wrong!", Toast.LENGTH_LONG);
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            txtFeedback.setText("OOPS! Something went wrong!\n Please Make Sure Your Location Is On");
        }




    }
}
