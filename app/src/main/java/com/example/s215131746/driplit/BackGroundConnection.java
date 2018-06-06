package com.example.s215131746.driplit;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by s216127904 on 2018/06/05.
 */

public class BackGroundConnection extends AsyncTask<String,String,String> {

    BackGroundConnection() {
    }

    @Override
    protected String doInBackground(String... params) {
        String type = "";
        String login_url = "http://sict-iis.nmmu.ac.za/codecentrix/MobileConnectionString/ConnectionString.php";
        try {

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line+",";
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }

    @Override
    protected void onPostExecute(String s) {


    }
    public String[] conntion()
    {
        String[] connection = doInBackground().split(",");
      return connection;
    }
}