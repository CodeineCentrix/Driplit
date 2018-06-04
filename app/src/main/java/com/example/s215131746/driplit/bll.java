package com.example.s215131746.driplit;

import android.app.DownloadManager;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.lang.reflect.*;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by s216127904 on 2018/05/01.
 */

public class  bll {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String errorMassage;
    String[] ItemNames;
    String[] Averages;


    public bll()
    {
    }
    public String[] getItemName()
    {
        return ItemNames;
    }
    public String[] getItemAverageUse()
    {
        return  Averages;
    }
    public int[] getItemIcon()
    {
        int[] icon = {5,6,2,2,5,8,15,6,2,2,5,8,15,6,2,2,5,8,15,6,2,2,5,8,15,6,2,2,5,8,1};
        return  icon;
    }

    private void Connect()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String us = "codecentrix";
            String password="password";
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://sict-sql.nmmu.ac.za:1433/Codecentrix",us,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //public void Con()
    //{

     //   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
     //   StrictMode.setThreadPolicy(policy);
     //   try {
     //   //     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
     //      // URL url = new URL("https","sict-iis.nmmu.ac.za",445,"View");
     //       URL url = new URL("http://sict-iis.nmmu.ac.za/codecentrix/View/");
     //       HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
     //       try {
     //           InputStream in = new BufferedInputStream(urlConnection.getInputStream());

     //       } finally {
     //           urlConnection.disconnect();
     //       }


     //    } catch (Exception e) {
     //        e.printStackTrace();
     //    }


    //}
    //This method should be called login because its for login
    public PersonModel Person(PersonModel person)
    {
        try
        {
            Connect();
            PreparedStatement st = connection.prepareStatement("{CALL uspMobGetPerson (?,?)}");
            st.setString(1,person.email);
            st.setString(2,person.userPassword);

            resultSet = st.executeQuery();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try {
        while(resultSet.next())
        {
            person.fullName = (resultSet.getString("FullName").toString());
            person.phoneNumber = (resultSet.getString("PhoneNumber"));
            person.email = (resultSet.getString("Email"));
            person.userPassword = (resultSet.getString("UserPassword"));
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            connection =null;
        }
        return person;
    }
    private void SelectParas(String query,String[] params)
    {
        Connect();
        try
        {
            Statement st = connection.createStatement();
            resultSet = st.executeQuery("uspGetWaterUsageItmes");
        }
        catch (SQLException e)
        {
        }
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> Avg = new ArrayList<>();

        try {
            while(resultSet.next())
            {

                name.add(resultSet.getString("ItemDescription").toString());
                Avg.add(""+resultSet.getFloat("ItemAverageAmount"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        String[] ItemName = new String[name.size()];
        ItemNames = name.toArray(ItemName);
        String[] Average = new String[Avg.size()];
        Averages = Avg.toArray(Average);
    }
    //this Select method is for the fields foe water intake

    public ArrayList<ItemUsageModel> GetItems()
    {
        ArrayList<ItemUsageModel> itemUsageModel = new ArrayList<>();
        try
        {
            Connect();
            PreparedStatement st = connection.prepareCall("{CALL uspMobGetWaterUsageItmes}");
            resultSet = st.executeQuery();
            while(resultSet.next())
            {
                ItemUsageModel item = new ItemUsageModel();
                item.ItemID = resultSet.getInt("ItemID");
                item.ItemDiscriotn = resultSet.getString("ItemDescription");
                item.ItemAverage =resultSet.getFloat("ItemAverageAmount");
                //item.ItemIcon = resultSet.getByte();
                itemUsageModel.add(item);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
        return itemUsageModel;
    }

    public float GetUserTotalUsage(String userEmail)
    {
        float TotalUsage =0;
        try
        {
            Connect();
            PreparedStatement st = connection.prepareCall("{CALL uspMobGetWaterUsageItmes}");
            resultSet = st.executeQuery();
            resultSet.next();
            TotalUsage = resultSet.getFloat("TotalUsage");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
        return TotalUsage;
    }
    public boolean MobAddPerson(PersonModel person ) throws SQLException {
        Connect();
        int i=0;
        try {
            PreparedStatement st = connection.prepareStatement("{CALL uspMobAddPerson(?.?,?,?)}");
            int count = 1;
            for (Object p : person.getClass().getDeclaredFields()) {
                st.setString(count, p.toString());
            }
            i = st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
            return i==0;
    }
    public boolean MobDeletePerson() throws SQLException {

       // Field[] params = {""+personID,""+delete};
       // return  Insert("uspMobAddPerson",params);
        return false;
    }

    public class backGround extends AsyncTask<URL,String,String>{

        @Override
        protected String doInBackground(URL... urls) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

