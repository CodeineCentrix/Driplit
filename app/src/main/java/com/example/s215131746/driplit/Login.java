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
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {
    Intent registerScreen, fodScreen;
    public  EditText email;
    public  EditText password;
    public CheckBox cbRemeber;
    DBAccess business;
    GeneralMethods m;

    TextView txtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new GeneralMethods(getApplicationContext());
        setContentView(R.layout.activity_login);
        cbRemeber = findViewById(R.id.cbRememberMe);
        email = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        //should be taken out before judging
        RemeberMe();
        if(cbRemeber.isChecked())
        {
            String[] details = m.Read("person.txt",",");
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
        m.openWebPage("http://sict-iis.nmmu.ac.za/codecentrix/Resources/View/Register.php?from=mobile");
        //registerScreen = new Intent(getApplicationContext(), Register.class);
        //startActivity(registerScreen);
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
        person = business.LoginPerson(person);
        if(person.fullName!=null && !person.fullName.equals(""))
        {
            Toast.makeText(this,    "Hello "+person.fullName,Toast.LENGTH_LONG).show();
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            m.writeToFile(person.id+","+ person.toString(),"person.txt");
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
    public void writeToFile(String data,Context context,String fileName) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFromFile(Context context,String fileName) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
    public String[] Read(Context context,String fileName,String splitter)
    {
       return m.readFromFile(fileName).split(splitter);
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
