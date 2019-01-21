package com.example.s215131746.driplit;

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

    public BackGroundConnection() {
    }

    @Override
    protected String doInBackground(String... params) {
        String type = "";
        String login_url = "http://codecentrix.gearhostpreview.com/index.php";
        try {

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String header = "Basic " + new String(android.util.Base64.encode("codecentrix:password".getBytes(), android.util.Base64.NO_WRAP));
            httpURLConnection.addRequestProperty("Authorization", header);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            type = ""+httpURLConnection.getResponseCode();
            InputStream error = httpURLConnection.getErrorStream();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append(",");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result.toString();
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
    public String[] ConnectionProperties()
    {
        String [] dbProps = new String[] {
                "jdbc:jtds:sqlserver://den1.mssql7.gear.host:1433/codecentrix", "net.sourceforge.jtds.jdbc.Driver",
                "codecentrix", "Dk8r4_uxM!Ln"};
       // return doInBackground().split(","); //uncomment to take back to original statee
        return dbProps;
    }


}
