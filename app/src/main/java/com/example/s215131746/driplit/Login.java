package com.example.s215131746.driplit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
    Bitmap bitmapImage;
    int usage,oldTips;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new GeneralMethods(getApplicationContext());
        txtFeedback = findViewById(R.id.txtFeedback);
        setContentView(R.layout.activity_login);
        cbRemeber = findViewById(R.id.cbRememberMe);
        email = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        RemeberMe();
        usage = 60;
        oldTips =0;
        if(cbRemeber.isChecked()) {
            details = m.Read(this.getString(R.string.person_file_name), ",");
            email.setText(details[PersonModel.EMAIL]);
            password.setText(details[PersonModel.PASSWORD]);
            try {
                usage = Integer.parseInt(details[PersonModel.USAGETARGET]);
                oldTips = Integer.parseInt(details[PersonModel.OLDAPPROVED]);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            Bundle tab = getIntent().getExtras();
            if(tab==null) {
                if(details.length>PersonModel.OLDAPPROVED) {
                    Intent fod = new Intent(getApplicationContext(), FODScreen.class);
                    startActivity(fod);
                }
            }
        }
        business = new DBAccess();
    }
   
    public void RemeberMe(String answ){
        m.writeToFile(answ,"Remember.txt");
    }
    public void RemeberMe(){
        if(m.readFromFile("Remember.txt").equals("yes"))
         cbRemeber.setChecked(true);
        else
            cbRemeber.setChecked(false);
    }
    public void ToRegisterScreen(View view){
        //m.openWebPage("http://sict-iis.nmmu.ac.za/codecentrix/IT2/Resources/View/log_in.php?from=mobile");

       Uri webpage = Uri.parse("http://sict-iis.nmmu.ac.za/codecentrix/IT2/Controller/MainController.php?action=register_page&from=mobile");
       Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if ( intent.resolveActivity(getPackageManager()) != null) {
           startActivity( intent);
       }
      /*  registerScreen = new Intent(getApplicationContext(), Register2.class);
        startActivity(registerScreen);*/
    }
    public void ToFODScreen(View view){
        if(cbRemeber.isChecked())
            RemeberMe("yes");
        else
            RemeberMe("no");
        PersonModel person = new PersonModel();
        person.isAdmin =false;
        person.email = email.getText().toString();
        person.Usagetarget = usage;
        person.getOldapproved = oldTips;
        if(person.email.contains("@driplit.drip")){
            person.isAdmin = true;
        }
        person.userPassword = password.getText().toString();

       helpThread h = new helpThread(person);
       new Thread(h).start();
    }
    public void GetLocation(final View view) {
        TextView txtFeedback;
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
            try{
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String fullAddress = address;
                if(address != null)
                {
                    AlertDialog.Builder takePic = new AlertDialog.Builder(view.getContext());
                    takePic.setTitle("Take leak picture! ");
                    takePic.setMessage("Do you want to take a picture of leak?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onTakePicture( view);
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView txtFeedback = findViewById(R.id.txtFeedback);
                                    txtFeedback.setText("");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView txtFeedback = findViewById(R.id.txtFeedback);
                                    txtFeedback.setText("Leak Successfully Reported!");
                                }
                            });
                    AlertDialog b = takePic.create();
                    b.show();


                }
                else{
                    Toast.makeText(view.getContext(), R.string.geo, Toast.LENGTH_LONG);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            /*txtFeedback = findViewById(R.id.txtFeedback);
            txtFeedback.setText("OOPS! Something went wrong!\n Please Make Sure Your Location Is On");*/
            Toast.makeText(this, R.string.geo+"\n Please Make Sure Your Location Is On", Toast.LENGTH_LONG).show();

        }




    }
    public void onTakePicture(View view){

        Intent showCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(showCamera, 10);

    }
    public void afterConnection(PersonModel person){
        if(person.fullName!=null && !person.fullName.equals("")){
            if(person.isAdmin){
                Toast.makeText(this,"Its good to see you admin",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,    "Hello "+person.fullName,Toast.LENGTH_SHORT).show();
            fodScreen = new Intent(getApplicationContext(), FODScreen.class);
            startActivity(fodScreen);
        }else{
            TextView tvError = findViewById(R.id.tvError);
            tvError.setText(R.string.login_error);
            Toast.makeText(this,"Invalide email or password",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && data!=null){
            bitmapImage = (Bitmap) data.getExtras().get("data");
            TextView txtFeedback = findViewById(R.id.txtFeedback);
            txtFeedback.setText("Leak Successfully Reported!");
        }
    }
    class helpThread implements Runnable {
        boolean isConnecting;
        Snackbar mySnackbar;
        PersonModel person;
        public helpThread(PersonModel person) {
            this.person = person;
        }

        @Override
        public void run() {
            isConnecting = business.isConnecting();
            if(isConnecting) {
                person = business.LoginPerson(person);
                //writes the persons details to a screen which gets continually used
                m.writeToFile(person.toString(),"person.txt");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        afterConnection(person);
                    }
                });
            }else{
                mySnackbar = Snackbar.make(email,"No Connection", 10000);
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
