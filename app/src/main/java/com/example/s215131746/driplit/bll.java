package com.example.s215131746.driplit;

import android.app.DownloadManager;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.lang.reflect.*;


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
    private String fullName;
    private String email;
    private String userPassword;
    private String phoneNumber;

    public bll()
    {
        Select();
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
    public String getPassword()
    {
        return null;
    }
    public String getUsername()
    {
        return  null;
    }
    public void LoadConnection()
    {
        Connect();
    }
    private void Connect()
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String user = "codecentrix";
             String password = "password";

            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://sict-sql.nmmu.ac.za:1433/Codecentrix" , user, password);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String[] Person(String email,String password)
    {
        String[] personDetais = new String[4];


        try
        {


            PreparedStatement st = connection.prepareStatement("{CALL uspMobGetPerson (?,?)}");
            st.setString(1,email);
            st.setString(2,password);

            resultSet = st.executeQuery();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try {
        while(resultSet.next())
        {
            personDetais[0] = (resultSet.getString("FullName").toString());
            personDetais[1] = (resultSet.getString("PhoneNumber"));
            personDetais[2] = (resultSet.getString("Email"));
            personDetais[3] = (resultSet.getString("UserPassword"));
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return personDetais;
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
    private void Select()
    {

        try
        {
            Connect();
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












    public boolean MobAddPerson(String fullName,String email,String userPassword,String phoneNumber ) throws SQLException {

        //boolean done = false;
        //SqlParameter p = new SqlParameter
        PersonModel person = new PersonModel();

        Field[] para = person.getClass().getDeclaredFields();
        if(para==null)
            return Insert("uspMobAddPerson", para);
        else
            return false;

    }
    public boolean MobDeletePerson() throws SQLException {

       // Field[] params = {""+personID,""+delete};
       // return  Insert("uspMobAddPerson",params);
        return false;
    }































    private boolean Insert(String Query,Field[] params ) throws SQLException {
        boolean i = false;
        Connect();
        try
        {
            PreparedStatement st = connection.prepareStatement(Query);

            st.execute("INSERT INTO Person (FullName,Email,UserPassword,PhoneNumber) VALUES ('"+params[0]+"','"+params[1]+"','"+params[2]+"','"+params[3]+"')");
            i = true;
        }
        catch (SQLException e)
        {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
        return  i;
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

