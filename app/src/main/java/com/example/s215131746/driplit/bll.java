package com.example.s215131746.driplit;

import android.os.StrictMode;
import android.widget.Toast;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by s216127904 on 2018/05/01.
 */

public class bll {

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
    public String[] Person(String email)
    {
        String[] personDetais = new String[4];


        try
        {
            Connect();
            Statement st = connection.createStatement();
            resultSet = st.executeQuery("SELECT * FROM Person WHERE Email = '"+email+"' AND Deleted = 'false' ");

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
            resultSet = st.executeQuery("SELECT * FROM Person WHERE Email = "+params[0]+" AND Deleted = 'false' ");
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

    public boolean MobAddPerson(String fullName,String email,String userPassword,String phoneNumber ) throws SQLException {

        boolean done = false;
        String[] params = {fullName,email,userPassword,phoneNumber};
         for(int i = 0;i<params.length;i++) {
             if (params[i] == null || params[i] == "") {
                 done = false;
                 break;
             } else
                 done = true;
         }

         if (done==true)
            return  Insert("uspMobAddPerson",params);
         else
             return done;
    }
    public boolean MobDeletePerson(int personID, boolean delete) throws SQLException {

        String[] params = {""+personID,""+delete};
        return  Insert("uspMobAddPerson",params);
    }

    private boolean Insert(String Query,String[] params ) throws SQLException {
        boolean i = false;
        Connect();
        try
        {
            Statement st = connection.createStatement();

            i = st.execute("INSERT INTO Person (FullName,Email,UserPassword,PhoneNumber) VALUES ('"+params[0]+"','"+params[1]+"','"+params[2]+"','"+params[3]+"')");

        }
        catch (SQLException e)
        {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
        return  i;
    }
}
