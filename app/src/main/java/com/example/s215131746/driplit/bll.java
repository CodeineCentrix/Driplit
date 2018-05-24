package com.example.s215131746.driplit;

import android.os.StrictMode;

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

}
