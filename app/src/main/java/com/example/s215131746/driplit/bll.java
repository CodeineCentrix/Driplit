package com.example.s215131746.driplit;

import android.app.DownloadManager;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.renderscript.Element;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.lang.reflect.*;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by s216127904 on 2018/05/01.
 */

public class  bll {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String errorMassage;
    private void Connect(){
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
    //This method should be called login because its for login
    public PersonModel LoginPerson(PersonModel person){
        try
        {
            Connect();
            PreparedStatement st = connection.prepareStatement("{CALL uspMobGetPerson (?,?)}");
            st.setString(1,person.email);
            st.setString(2,person.userPassword);
            resultSet = st.executeQuery();
            resultSet.next();//Moves from row of Heading to row record
            person.fullName = resultSet.getString("FullName");
            person.phoneNumber = resultSet.getString("PhoneNumber");
            person.email = resultSet.getString("Email");
            person.userPassword = resultSet.getString("UserPassword");
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
    public ArrayList<ItemUsageModel> GetItems(){
        ArrayList<ItemUsageModel> itemUsageModel = new ArrayList<>();
        try
        {
            resultSet = GetAll("{CALL uspMobGetWaterUsageItmes}");
            while(resultSet.next()){
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
        return itemUsageModel;
    }
    public float uspMobGetPersonItemTotal(String userEmail,int itemID){
        float TotalUsage =0;
        try
        {
            Connect();
            PreparedStatement st = connection.prepareStatement("{CALL uspMobGetPersonItemTotal (?,?)}");
            st.setString(1,userEmail);
            st.setInt(2,itemID);
            resultSet = st.executeQuery();
            resultSet.next();
            TotalUsage = resultSet.getFloat("TotalUsageForItem");
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
    public ArrayList<TipModel> GetTips(){
        ArrayList<TipModel> Tips = new ArrayList<>();
        try
        {
            resultSet = GetAll("{CALL uspMobGetTips}");
            while(resultSet.next())
            {
                TipModel tip = new TipModel();
                tip.ID = resultSet.getInt("TTID");
                tip.CatID = resultSet.getInt("CatID");
                tip.PersonID =resultSet.getInt("PersonID");
                tip.TipDescription = resultSet.getString("TTdescription");
                //item.ItemIcon = resultSet.getByte();
                Tips.add(tip);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Tips;
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

    private boolean NonQuery(String sql,Object[] parameters){
        Connect();
        int i=0;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int count = 1;
            for (Object para : parameters) {
                setObject(i,para,st);
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
    private ResultSet GetAll(String sql){
        try
        {
            Connect();
            PreparedStatement st = connection.prepareStatement(sql);
            resultSet = st.executeQuery();
            st.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {

        }
        return resultSet;
    }
    private ResultSet GetAllWithPara(String sql,Object[] parameters){
        try
        {
            Connect();
            PreparedStatement st = connection.prepareStatement(sql);
            int i = 1;
            for (Object para: parameters) {
                setObject(i,para,st);
                i++;
            }
            resultSet = st.executeQuery();
            st.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {

        }
        return resultSet;
    }
    private void setObject(int parameterIndex, Object parameterObj,PreparedStatement preparedStatement) throws SQLException {
        //Hate this, I really do But the is no other way
            if (parameterObj == null) {
                preparedStatement.setNull(parameterIndex, java.sql.Types.OTHER);
            } else {
                if (parameterObj instanceof Byte) {
                    preparedStatement.setInt(parameterIndex, ((Byte) parameterObj).intValue());
                } else if (parameterObj instanceof String) {
                    preparedStatement.setString(parameterIndex, (String) parameterObj);
                } else if (parameterObj instanceof BigDecimal) {
                    preparedStatement.setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
                } else if (parameterObj instanceof Short) {
                    preparedStatement.setShort(parameterIndex, ((Short) parameterObj).shortValue());
                } else if (parameterObj instanceof Integer) {
                    preparedStatement.setInt(parameterIndex, ((Integer) parameterObj).intValue());
                } else if (parameterObj instanceof Long) {
                    preparedStatement.setLong(parameterIndex, ((Long) parameterObj).longValue());
                } else if (parameterObj instanceof Float) {
                    preparedStatement.setFloat(parameterIndex, ((Float) parameterObj).floatValue());
                } else if (parameterObj instanceof Double) {
                    preparedStatement.setDouble(parameterIndex, ((Double) parameterObj).doubleValue());
                } else if (parameterObj instanceof byte[]) {
                    preparedStatement.setBytes(parameterIndex, (byte[]) parameterObj);
                } else if (parameterObj instanceof java.sql.Date) {
                    preparedStatement.setDate(parameterIndex, (java.sql.Date) parameterObj);
                } else if (parameterObj instanceof Time) {
                    preparedStatement.setTime(parameterIndex, (Time) parameterObj);
                } else if (parameterObj instanceof Timestamp) {
                    preparedStatement.setTimestamp(parameterIndex, (Timestamp) parameterObj);
                } else if (parameterObj instanceof Boolean) {
                    preparedStatement.setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
                } else if (parameterObj instanceof BigInteger) {
                    preparedStatement.setString(parameterIndex, parameterObj.toString());
                } else {
                    preparedStatement.setObject(parameterIndex, parameterObj);
                }
            }
        }

    //This background class is needed to stop the app from freezing every time we retrieve data
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

