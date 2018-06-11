package com.example.s215131746.driplit;

import android.app.AppOpsManager;
import android.content.res.Resources;
import android.os.StrictMode;
import android.support.annotation.XmlRes;
import android.util.Xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

public class DBAccess implements IDBAccess{

    private ResultSet outerResultSet;
    private static class DBHelper{

        private static String conString ;
        private static Connection connection;
        private static PreparedStatement st;
        private static ResultSet innerResultSet;
        private static String forName ;
        static String us ;
        static String password;
        private static void Connect(){
            BackGroundConnection connectionProperties = new BackGroundConnection();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String[] properties = connectionProperties.conntion();
                conString = properties[0];
                forName =  properties[1];
                us =  properties[2];
                password =properties[3];
                Class.forName(forName).newInstance();
                connection = DriverManager.getConnection(conString,us,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private static void Close(){

            try {
                innerResultSet.close();
                st.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        public static boolean NonQuery(String sql,Object[] parameters){
            Connect();
            int i=0;
            try {
                 st = connection.prepareStatement(sql);
                int count = 1;
                for (Object para : parameters) {
                    setObject(count,para,st);
                    count++;
                }
                i = st.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return i==0;
        }
        public static ResultSet Select(String sql){
            try
            {
                Connect();
                 st = connection.prepareStatement(sql);
                innerResultSet = st.executeQuery();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally {

            }
            return innerResultSet;
        }
        public static ResultSet SelectPara(String sql,Object[] parameters){
            try
            {
                Connect();
                st = connection.prepareStatement(sql);
                int i = 1;
                for (Object para: parameters) {
                    setObject(i,para,st);
                    i++;
                }
                ResultSet r =  st.executeQuery();
                innerResultSet = r;

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally {

            }
            return innerResultSet;
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
        outerResultSet = DBHelper.SelectPara("{CALL uspMobGetPerson (?,?)}",paras);
        try{
            outerResultSet.next();//Moves from row of Heading to row record
            person.id = outerResultSet.getInt("PersonID");
            person.fullName = outerResultSet.getString("FullName");

            person.email = outerResultSet.getString("Email");
            person.userPassword = outerResultSet.getString("UserPassword");
            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return person;
    }
    public ArrayList<ItemUsageModel> GetItems(){
        ArrayList<ItemUsageModel> itemUsageModel = new ArrayList<>();
        try{
            outerResultSet = DBHelper.Select("{CALL uspMobGetWaterUsageItmes}");
            while(outerResultSet.next()){
                ItemUsageModel item = new ItemUsageModel();
                item.ItemID = outerResultSet.getInt("ItemID");
                item.ItemDiscriotn = outerResultSet.getString("ItemDescription");
                item.ItemAverage =outerResultSet.getFloat("ItemAverageAmount");
                //item.ItemIcon = resultSet.getByte();
                itemUsageModel.add(item);
            }
            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return itemUsageModel;
    }
    public ArrayList<UspMobGetPersonItemTotal> UspMobGetPersonItemTotal(String email){
        ArrayList<UspMobGetPersonItemTotal> usages = new ArrayList<>();
        try{
            Object[] paras = {email};
            outerResultSet = DBHelper.SelectPara("{CALL [UspMobGetPersonItemTotal](?)}",paras);
            while(outerResultSet.next()){
                UspMobGetPersonItemTotal usage = new UspMobGetPersonItemTotal();
                usage.ItemName = outerResultSet.getString("Item");
                usage.UsageAmount = outerResultSet.getFloat("TotalUsageForItem");
                usages.add(usage);
            }
            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return usages;
    }
    public ArrayList<UspMobGetPersonTotalUsage> GetPersonTotalUsageGetItems(String email){
        ArrayList<UspMobGetPersonTotalUsage> usages = new ArrayList<>();
        try{
            Object[] paras = {email};
            outerResultSet = DBHelper.SelectPara("{CALL uspMobGetPersonTotalUsage(?)}",paras);
            while(outerResultSet.next()){
                UspMobGetPersonTotalUsage usage = new UspMobGetPersonTotalUsage();
                usage.UsageDay = outerResultSet.getString("UsageDay");
                usage.UsageAmount = outerResultSet.getFloat("UsageAmount");
                //item.ItemIcon = resultSet.getByte();
                usages.add(usage);
            }
            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return usages;
    }
    public ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotal(String userEmail){
        ArrayList<UspMobGetPersonItemTotal> TotalUsage = new ArrayList<>();

        try{
            Object[] paras = {userEmail};
            outerResultSet = DBHelper.SelectPara("{CALL uspMobGetPersonItemTotal (?)}",paras);
            while (outerResultSet.next())
            {
                UspMobGetPersonItemTotal use = new UspMobGetPersonItemTotal();
                use.ItemID =outerResultSet.getInt("ItemID");
                use.ItemName = outerResultSet.getString("Item");
                use.UsageAmount = outerResultSet.getFloat("TotalUsageForItem");
                TotalUsage.add(use);
            }

            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return TotalUsage;
    }
    public ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotalDate(String userEmail, String date){
        ArrayList<UspMobGetPersonItemTotal> TotalUsage = new ArrayList<>();

        try{
            Object[] paras = {userEmail,date};
            outerResultSet = DBHelper.SelectPara("{CALL uspMobGetPersonItemTotalDate (?,?)}",paras);
            while (outerResultSet.next())
            {
                UspMobGetPersonItemTotal use = new UspMobGetPersonItemTotal();
                use.ItemID =outerResultSet.getInt("ItemID");
                use.ItemName = outerResultSet.getString("Item");
                use.UsageAmount = outerResultSet.getFloat("TotalUsageForItem");
                TotalUsage.add(use);
            }

            DBHelper.Close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return TotalUsage;
    }
    public ArrayList<TipModel> GetTips(){
        ArrayList<TipModel> Tips = new ArrayList<>();
        try
        {
            outerResultSet = DBHelper.Select("{CALL uspMobGetTips}");
            while(outerResultSet.next())
            {
                TipModel tip = new TipModel();
                tip.ID = outerResultSet.getInt("TTID");
                tip.PersonName = outerResultSet.getString("FullName");
                tip.CatID = outerResultSet.getInt("CatID");
                tip.PersonID =outerResultSet.getInt("PersonID");
                tip.DatePosted = outerResultSet.getDate("DatePosted");
                tip.FullName = outerResultSet.getString("FullName");
                tip.TipDescription = outerResultSet.getString("TTdescription");
                //item.ItemIcon = resultSet.getByte();
                Tips.add(tip);
            }
            DBHelper.Close();
        }
        catch (SQLException e)
        {

            e.printStackTrace();
        }
        return Tips;
    }
    public boolean uspMobUpdatePerson(PersonModel person ){
        Object[] paras = {person.id,person.fullName,person.userPassword};
        boolean isWorking = DBHelper.NonQuery("{CALL uspMobAddPerson(?,?,?)}",paras);
        DBHelper.Close();
        return isWorking;
    }
    public boolean MobAddPerson(PersonModel person ){
        Object[] paras = {person.fullName,person.email,person.userPassword};
        boolean isWorking = DBHelper.NonQuery("{CALL uspMobAddPerson(?,?,?)}",paras);
        DBHelper.Close();
        return isWorking;
    }
    public boolean MobAddTip(TipModel tip ){
        Object[] paras = {tip.PersonID,tip.TipDescription};
        boolean isWorking = DBHelper.NonQuery("{CALL uspMobAddTip(?,?)}",paras);
        DBHelper.Close();
        return isWorking;
    }
    public boolean MobDeletePerson(String email){
        Object[] paras = {email};
        boolean isWorking = DBHelper.NonQuery("{CALL uspMobAddPerson(?)}",paras);
        DBHelper.Close();
        return isWorking;
    }
    public boolean MobAddResidentUsage(ResidentUsageModel ResUsage){
        Object[] paras = {ResUsage.PersonID,ResUsage.ResDate,ResUsage.ResTime,ResUsage.AmountUsed,ResUsage.ItemID};
        boolean isWorking = DBHelper.NonQuery("{CALL uspMobAddResidentUsage(?,?,?,?,?)}",paras);
        DBHelper.Close();
        return isWorking;
    }

}
