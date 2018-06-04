package com.example.s215131746.driplit;

import android.app.AppOpsManager;
import android.os.StrictMode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class BDAccess {

    private ResultSet resultSet;
    private static class DBHelper{
        private static String conString =  "jdbc:jtds:sqlserver://sict-sql.nmmu.ac.za:1433/Codecentrix";
        private static Connection connection;
        private static ResultSet resultSet;
        private static String forName = "net.sourceforge.jtds.jdbc.Driver";
         static String us = "codecentrix";
         static String password="password";
        private static void Connect(){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Class.forName(forName).newInstance();
                connection = DriverManager.getConnection(conString,us,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static boolean NonQuery(String sql,Object[] parameters){
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
        public static ResultSet Select(String sql){
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
        public static ResultSet SelectPara(String sql,Object[] parameters){
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
        private static void setObject(int parameterIndex, Object parameterObj,PreparedStatement preparedStatement) throws SQLException {
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

    }

    public PersonModel LoginPerson(PersonModel person){
        Object[] paras = {person.email,person.userPassword};
        resultSet = DBHelper.SelectPara("{CALL uspMobGetPerson (?,?)}",paras);
        try{
            resultSet.next();//Moves from row of Heading to row record
            person.fullName = resultSet.getString("FullName");
            person.phoneNumber = resultSet.getString("PhoneNumber");
            person.email = resultSet.getString("Email");
            person.userPassword = resultSet.getString("UserPassword");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return person;
    }
    public ArrayList<ItemUsageModel> GetItems(){
        ArrayList<ItemUsageModel> itemUsageModel = new ArrayList<>();
        try{
            resultSet = DBHelper.Select("{CALL uspMobGetWaterUsageItmes}");
            while(resultSet.next()){
                ItemUsageModel item = new ItemUsageModel();
                item.ItemID = resultSet.getInt("ItemID");
                item.ItemDiscriotn = resultSet.getString("ItemDescription");
                item.ItemAverage =resultSet.getFloat("ItemAverageAmount");
                //item.ItemIcon = resultSet.getByte();
                itemUsageModel.add(item);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return itemUsageModel;
    }
    public float uspMobGetPersonItemTotal(String userEmail,int itemID){
        float TotalUsage =0;
        try{
            Object[] paras = {userEmail,itemID};
            resultSet = DBHelper.SelectPara("{CALL uspMobGetPersonItemTotal (?,?)}",paras);
            resultSet.next();
            TotalUsage = resultSet.getFloat("TotalUsageForItem");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return TotalUsage;
    }
    public ArrayList<TipModel> GetTips(){
        ArrayList<TipModel> Tips = new ArrayList<>();
        try
        {
            resultSet = DBHelper.Select("{CALL uspMobGetTips}");
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
    public boolean MobAddPerson(PersonModel person ){
        Object[] paras = {person.fullName,person.email,person.userPassword,person.phoneNumber};
        return DBHelper.NonQuery("{CALL uspMobAddPerson(?,?,?,?)}",paras);
    }
    public boolean MobDeletePerson(String email){
        Object[] paras = {email};
        return DBHelper.NonQuery("{CALL uspMobAddPerson(?)}",paras);
    }
}
