package com.example.s215131746.driplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by s216127904 on 2018/07/19.
 */

public class DownLoadPicture extends AsyncTask<Void,Void,Bitmap> {
    String name;
    public DownLoadPicture(String name)
    {
        if(name==null){
            name = "icon/complogo";
        }
        this.name = name;
    }
    @Override
    public Bitmap doInBackground(Void... voids) {
        String login_url = "http://sict-iis.nmmu.ac.za/codecentrix/MobileConnectionString/"+name.trim()+".png";
        HttpURLConnection httpURLConnection =null;
        try {
            URL url = new URL(login_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000 * 30);
            httpURLConnection.setReadTimeout(1000 * 30);
//            String header = "Basic " + new String(android.util.Base64.encode("codecentrix:password".getBytes(), android.util.Base64.NO_WRAP));
//            String type = ""+httpURLConnection.getResponseCode();
//            InputStream error = httpURLConnection.getErrorStream();
//            httpURLConnection.addRequestProperty("Authorization", header);

            Bitmap pictureBitmap = BitmapFactory.decodeStream((InputStream) httpURLConnection.getContent(), null, null);
            httpURLConnection.disconnect();
            return pictureBitmap;
        }catch (Exception e){
            httpURLConnection.disconnect();
            e.printStackTrace();
            return  null;
        }

    }
}
