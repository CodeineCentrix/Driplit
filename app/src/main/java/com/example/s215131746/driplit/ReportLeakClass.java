package com.example.s215131746.driplit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import viewmodels.PersonModel;
import viewmodels.ReportLeakModel;

import static android.location.LocationManager.*;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class ReportLeakClass extends Fragment {
    ImageButton btnReport;
    Button btnReported;
    TextView txtAddress;
    TextView txtHead;
    TextView txtInstruction;
    DBAccess db = new DBAccess();
    GeneralMethods m ;
    Bitmap bitmapImage;
     Double longitude;
     Double latitude ;
     String id;
    String uploadImageName;
    String address;
    private int TakePicRequestCode;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.report_leak, container, false);

        GeneralMethods m = new GeneralMethods(rootView.getContext());
        Bitmap image = m.ScaleImg(R.drawable.report,rootView.getResources());
            btnReport = (ImageButton)rootView.findViewById(R.id.imgReportLeak);
            btnReport.setImageBitmap(image);
            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetLocation(v);
                }
            });
        return rootView;
    }
    public void onTakePicture(View view){
        Intent showCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        TakePicRequestCode = 10;
        startActivityForResult(showCamera, TakePicRequestCode);

    }
    public void GetLocation(final View view){

        //Getting the context and initializing LocationManager with location service.
        Context con = view.getContext();
        LocationManager lm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //Checking For Request Permissions
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            //Handling The Request Result
            //onRequestPermissionsResult(10, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, int grantResults[]);
            return;
        }
        if(lm.getProvider(GPS_PROVIDER) != null){
            m = new GeneralMethods(view.getContext());
            try{
                //Getting the last known location of the device
                Location location = lm.getLastKnownLocation(GPS_PROVIDER);
                Geocoder geocoder;
                List<Address> addresses;
                 longitude = location.getLongitude();
                 latitude = location.getLatitude();
                //Finding the IDs for the TextViews
                txtAddress = (TextView) getView().findViewById(R.id.txtAddress);
                txtHead = (TextView) getView().findViewById(R.id.txtHead);
                txtInstruction = (TextView) getView().findViewById(R.id.txtRLInstruct);
                //This section gets the address from the longitude and latitude.
                geocoder = new Geocoder(getContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);
                final String fullAddress = address;

             id = m.Read(this.getString(R.string.person_file_name), ",")[PersonModel.ID];
                if(address != null){
                    AlertDialog.Builder takePic = new AlertDialog.Builder(view.getContext());
                    takePic.setTitle("Take leak picture! ");
                    takePic.setMessage("Do you want to take a picture of leak?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onTakePicture(view);
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SaveLeak(longitude.toString(),latitude.toString(), fullAddress, Integer.parseInt(id));
                                }
                            });
                    AlertDialog b = takePic.create();
                    b.show();
                }
                else{
                    Toast.makeText(view.getContext(), " Geo location not found!", Toast.LENGTH_LONG).show();
                }

            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                Toast.makeText(view.getContext(), "Geo location not found!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(view.getContext(), "Geo location not found!\n Please Make Sure Your Location Is On", Toast.LENGTH_LONG).show();
        }
    }
    public void SaveLeak(String longitude,String latitude,String fullAddress, int id){
        txtHead.setVisibility(View.VISIBLE);
        txtAddress.setVisibility(View.VISIBLE);
        txtInstruction.setVisibility(View.INVISIBLE);
        txtAddress.setText(fullAddress);

        ReportLeakModel report = new ReportLeakModel();
        report.PersonID = id;
        report.Latitude = ""+latitude;
        report.Longitude = ""+longitude;
        report.Location = address;
        db.MobAddLeak(report);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TakePicRequestCode && data!=null){
            bitmapImage = (Bitmap) data.getExtras().get("data");
            if(bitmapImage!=null){
                uploadImageName = "leak"+ new SimpleDateFormat("yyyyMMddHHmmss'.PNG'").format(new Date());
                new UploadImage(uploadImageName,bitmapImage).doInBackground();
                ReportLeakModel report = new ReportLeakModel();
                report.PersonID = Integer.parseInt(id);
                report.Latitude = ""+latitude;
                report.Longitude = ""+longitude;
                report.Location = address;
                report.picPath = uploadImageName;
                db.MobAddLeak(report);
                txtHead.setVisibility(View.VISIBLE);
                txtAddress.setVisibility(View.VISIBLE);
                txtInstruction.setVisibility(View.INVISIBLE);
                txtAddress.setText(address);
            }
        }
    }

}