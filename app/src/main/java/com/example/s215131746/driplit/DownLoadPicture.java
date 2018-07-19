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
    protected Bitmap doInBackground(Void... voids) {
        String login_url = "http://sict-iis.nmmu.ac.za/codecentrix/MobileConnectionString/"+name.trim()+".png";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000 * 30);
            httpURLConnection.setReadTimeout(1000 * 30);

            return BitmapFactory.decodeStream((InputStream) httpURLConnection.getContent(),null,null);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }
}
