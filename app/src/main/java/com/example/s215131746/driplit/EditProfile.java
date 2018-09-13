package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
    public void onImageGalleryClicked(View v) {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, 10);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(requestCode==10 && data!=null){
            bitmapImage = (Bitmap) data.getExtras().get("data");
            imgRacer.setImageBitmap(bitmapImage);
            loImage.setVisibility(navigationView.VISIBLE);

        }else*/
        if(resultCode == RESULT_OK) {

            // if we are here, everything processed successfully.
            if (requestCode == 10) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    // show the image to the user

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
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
