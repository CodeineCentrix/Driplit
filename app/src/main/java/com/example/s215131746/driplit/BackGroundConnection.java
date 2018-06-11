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
import java.util.ArrayList;

/**
 * Created by s216127904 on 2018/06/05.
 */

public class BackGroundConnection extends AsyncTask<String,String,String> implements IDBAccess {
    private IDBAccess db;
    BackGroundConnection() {
        db = new DBAccess();
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

    @Override
    public PersonModel LoginPerson(PersonModel person) {
        return db.LoginPerson(person);
    }

    @Override
    public ArrayList<ItemUsageModel> GetItems() {
        return db.GetItems();
    }

    @Override
    public ArrayList<UspMobGetPersonItemTotal> UspMobGetPersonItemTotal(String email) {
        return db.UspMobGetPersonItemTotal(email);
    }

    @Override
    public ArrayList<UspMobGetPersonTotalUsage> GetPersonTotalUsageGetItems(String email) {
        return db.GetPersonTotalUsageGetItems(email);
    }

    @Override
    public ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotal(String email) {
        return db.uspMobGetPersonItemTotal(email);
    }

    @Override
    public ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotalDate(String email, String date) {
        return db.uspMobGetPersonItemTotalDate(email,date);
    }

    @Override
    public ArrayList<TipModel> GetTips() {
        return db.GetTips();
    }

    @Override
    public boolean uspMobUpdatePerson(PersonModel person) {
        return db.uspMobUpdatePerson(person);
    }

    @Override
    public boolean MobAddPerson(PersonModel person) {
        return db.MobAddPerson(person);
    }

    @Override
    public boolean MobAddTip(TipModel tip) {
        return db.MobAddTip(tip);
    }

    @Override
    public boolean MobDeletePerson(String email) {
        return db.MobDeletePerson(email);
    }

    @Override
    public boolean MobAddResidentUsage(ResidentUsageModel ResUsage) {
        return db.MobAddResidentUsage(ResUsage);
    }
}