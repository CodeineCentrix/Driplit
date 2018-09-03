package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import viewmodels.Business;
import viewmodels.ItemUsageModel;
import viewmodels.PersonModel;
import viewmodels.ReportLeakModel;
import viewmodels.ResidentUsageModel;
import viewmodels.Surburb;
import viewmodels.TipModel;
import viewmodels.UspMobGetPersonItemTotal;
import viewmodels.UspMobGetPersonTotalUsage;

public class TabMenu extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);
        //_____________________
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Bundle tab = getIntent().getExtras();
        String tabName = tab.getString("Tab");
//        String[] tabs = {"Record","Report","Tips","Trend","ItemTrend","Edit"};
        String[] tabs = {"Record","Report","Tips","ItemTrend",""};
        DBAccess business = new DBAccess();
        boolean wifi = true;
        try {
            business.GetTips();
        }catch (NullPointerException e)        {
            wifi = false;
        }
        if(!wifi){
            Toast.makeText(this,"PLEASE TURN ON WIFI",Toast.LENGTH_SHORT).show();
        }else {
            for(int i = 0; i <tabs.length;i++){
                if(tabName.equalsIgnoreCase(tabs[i]) ){
                    mViewPager.setCurrentItem(i);
                }
            }
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
        for(int i = 0; i <tabs.length-1;i++){
            if(tabName.equalsIgnoreCase(tabs[i]) ){
                mViewPager.setCurrentItem(i);

            }
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }
    public void ToHome(View v){
        Intent showHomeMenu = new Intent(getApplicationContext(),Mainmenu.class);
        startActivity(showHomeMenu);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RecordWaterIntakeClass newInstance(int sectionNumber) {
            RecordWaterIntakeClass fragment = new RecordWaterIntakeClass();
            return fragment;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        //All our activity tabs should be here in the correct index
        @Override
        public Fragment getItem(int position) {
           //just in case
            if(position>getCount())
                position= 0 ;
            Fragment[] tabs =
                    {
                            new RecordWaterIntakeClass(),
                            new ReportLeakClass(),
                            new TipTrickClass(),
                            new IntakeTrendScroller(),
                            // new EditProfileClass()
                    } ;

            return tabs[position];
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

    public static class  DBAccess{

        private ResultSet outerResultSet;
        private static class DBHelper{
            private static String conString ;
            private static Connection connection;
            private static PreparedStatement st;
            private static ResultSet innerResultSet;
            private static String forName ;
            static String us ;
            static String password;

            private static boolean Connect(){
                boolean isConnecting=false;
                BackGroundConnection connectionProperties = new BackGroundConnection();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    String[] properties = connectionProperties.ConnectionProperties();
                    conString = properties[0];
                    forName =  properties[1];
                    us =  properties[2];
                    password =properties[3];
                    Class.forName(forName).newInstance();
                    connection = DriverManager.getConnection(conString,us,password);
                    if(connection!=null)
                    isConnecting = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return isConnecting;
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
            static String SetParaToPass(String sql, Object[] parameters){
                String usp = "{ CALL "+sql+"( ?";
                for(int i = 1 ; i<parameters.length;i++){
                    usp += ",?";
                }
                usp+= " )}";
                return usp;
            }
            static boolean NonQuery(String sql, Object[] parameters){
                if(Connect()){
                int i=0;
                try {
                     st = connection.prepareStatement(SetParaToPass(sql,parameters));
                    int count = 1;
                    for (Object para : parameters) {
                        BindParameter(count,para,st);
                        count++;
                    }
                    i = st.executeUpdate();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                return i==0;
                }else {
                    return false;
                }

            }
            static ResultSet Select(String sql){
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
                return innerResultSet;
            }
            static ResultSet SelectPara(String sql, Object[] parameters){
                try{
                    Connect();
                    st = connection.prepareStatement(SetParaToPass(sql,parameters));
                    int i = 1;
                    for (Object para: parameters) {
                        BindParameter(i,para,st);
                        i++;
                    }
                    innerResultSet = st.executeQuery();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                return innerResultSet;
            }
            private static void BindParameter(int parameterIndex, Object parameterObj, PreparedStatement preparedStatement) throws SQLException {
                //Hate this, I really do But the is no other way
                if (parameterObj == null) {
                    preparedStatement.setNull(parameterIndex, java.sql.Types.OTHER);
                } else {
                    if (parameterObj instanceof String) {
                        preparedStatement.setString(parameterIndex, (String) parameterObj);
                    }else if (parameterObj instanceof Integer) {
                        preparedStatement.setInt(parameterIndex, ((Integer) parameterObj).intValue());
                    } else if (parameterObj instanceof Long) {
                        preparedStatement.setLong(parameterIndex, ((Long) parameterObj).longValue());
                    } else if (parameterObj instanceof Float) {
                        preparedStatement.setFloat(parameterIndex, ((Float) parameterObj).floatValue());
                    }else if (parameterObj instanceof Boolean) {
                        preparedStatement.setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
                    } else if (parameterObj instanceof Byte) {
                        preparedStatement.setInt(parameterIndex, ((Byte) parameterObj).intValue());
                    } else if (parameterObj instanceof BigDecimal) {
                        preparedStatement.setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
                    } else if (parameterObj instanceof Short) {
                        preparedStatement.setShort(parameterIndex, ((Short) parameterObj).shortValue());
                    }  else if (parameterObj instanceof Double) {
                        preparedStatement.setDouble(parameterIndex, ((Double) parameterObj).doubleValue());
                    } else if (parameterObj instanceof byte[]) {
                        preparedStatement.setBytes(parameterIndex, (byte[]) parameterObj);
                    } else if (parameterObj instanceof java.sql.Date) {
                        preparedStatement.setDate(parameterIndex, (java.sql.Date) parameterObj);
                    } else if (parameterObj instanceof Time) {
                        preparedStatement.setTime(parameterIndex, (Time) parameterObj);
                    } else if (parameterObj instanceof Timestamp) {
                        preparedStatement.setTimestamp(parameterIndex, (Timestamp) parameterObj);
                    } else if (parameterObj instanceof BigInteger) {
                        preparedStatement.setString(parameterIndex, parameterObj.toString());
                    } else {
                        preparedStatement.setObject(parameterIndex, parameterObj);
                    }
                }
            }
        }
        public boolean isConnecting(){
            return DBHelper.Connect();
        }
        public PersonModel LoginPerson(PersonModel person){
           //this object acts like a sqlparameter like in c#
            Object[] paras = {person.email,person.userPassword};
            //pass the stored procedure name and the paras if you have parameter
            outerResultSet = DBHelper.SelectPara("uspMobGetPerson",paras);
           //the is alot that could go wrong when trying to connect to the database that is why the is a try catch
            try{
                //the outerResulSet is the table returned from the execution of the stored procedure
                outerResultSet.next();//Moves from row of Heading to row record
                person.id = outerResultSet.getInt("PersonID");//you need to specify not only the colunm name but also the data type to be return
                person.fullName = outerResultSet.getString("FullName");
                person.email = outerResultSet.getString("Email");
                person.userPassword = outerResultSet.getString("UserPassword");
                DBHelper.Close();//to closes the connection
            }catch (SQLException e){
                e.printStackTrace();// I propable should inform the user
            }
            return person;
        }
        public ArrayList<ItemUsageModel> GetItems(){
            ArrayList<ItemUsageModel> itemUsageModel = new ArrayList<>();
            try{
                outerResultSet = DBHelper.Select("{CALL uspMobGetWaterUsageItmes}");
                while(outerResultSet.next()){
                    ItemUsageModel item = new ItemUsageModel();
                    try {
                        DownLoadPicture d = new DownLoadPicture("icon/"+outerResultSet.getString("Icon"));
                        item.ItemIcon = d.doInBackground();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    item.ItemID = outerResultSet.getInt("ItemID");
                    item.ItemDescription = outerResultSet.getString("ItemDescription");
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
        public ArrayList<UspMobGetPersonTotalUsage> GetPersonTotalUsageGetItems(String email){
            ArrayList<UspMobGetPersonTotalUsage> usages = new ArrayList<>();
            try{
                Object[] paras = {email};
                outerResultSet = DBHelper.SelectPara(" uspMobGetPersonTotalUsage ",paras);
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
                outerResultSet = DBHelper.SelectPara(" uspMobGetPersonItemTotal ",paras);
                while (outerResultSet.next()){
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
                outerResultSet = DBHelper.SelectPara(" uspMobGetPersonItemTotalDate ",paras);
                while (outerResultSet.next()){
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
            try{
                outerResultSet = DBHelper.Select("{CALL uspMobGetTips}");
                while(outerResultSet.next()){
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

            catch (SQLException e){
                e.printStackTrace();
            }
            return Tips;
        }
        public ArrayList<TipModel> GetAdminTips(){
            ArrayList<TipModel> Tips = new ArrayList<>();
            try{
                outerResultSet = DBHelper.Select("{CALL uspMobAdminGetTips}");
                while(outerResultSet.next()){
                    TipModel tip = new TipModel();
                    tip.ID = outerResultSet.getInt("TTID");
                    tip.PersonName = "Adim";
                    tip.CatID = outerResultSet.getInt("CatID");
                    tip.PersonID =outerResultSet.getInt("PersonID");
                    tip.DatePosted = outerResultSet.getDate("DatePosted");
                    tip.FullName = outerResultSet.getString("FullName");
                    tip.TipDescription = outerResultSet.getString("TTdescription");
                    tip.Approved = outerResultSet.getBoolean("Approved");
                    tip.Readed = outerResultSet.getBoolean("Readed");
                    //item.ItemIcon = resultSet.getByte();
                    Tips.add(tip);
                }
                DBHelper.Close();
            }

            catch (SQLException e){
                e.printStackTrace();
            }
            return Tips;
        }
        public boolean MobUpdatePerson(PersonModel person ){
            Object[] paras = {person.id,person.fullName,person.userPassword};
            boolean isWorking = DBHelper.NonQuery(" uspMobUpdatePerson ",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean MobAddPerson(PersonModel person ){
            Object[] paras = {person.fullName,person.email,person.userPassword};
            boolean isWorking = DBHelper.NonQuery(" uspMobAddPerson",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean Test(int personID,String image ){
            Object[] paras = {personID,image};
            boolean isWorking = DBHelper.NonQuery(" uspMobTest",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean MobAddTip(TipModel tip ){
            Object[] paras = {tip.PersonID,tip.TipDescription};
            boolean isWorking = DBHelper.NonQuery(" uspMobAddTip ",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean MobDeletePerson(String email){
            Object[] paras = {email};
            boolean isWorking = DBHelper.NonQuery(" uspMobAddPerson",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean MobAddResidentUsage(ResidentUsageModel ResUsage){
            Object[] paras = {ResUsage.PersonID,ResUsage.ResDate,ResUsage.ResTime,ResUsage.AmountUsed,ResUsage.ItemID};
            boolean isWorking = DBHelper.NonQuery(" uspMobAddResidentUsage",paras);
            DBHelper.Close();
            return isWorking;
        }
        public boolean MobAddLeak(ReportLeakModel leak){
            Object[] paras = {leak.Latitude,leak.Longitude,leak.PersonID, leak.picPath, leak.Location};
            boolean isWorking = DBHelper.NonQuery(" uspAddLeak",paras);
            DBHelper.Close();
            return isWorking;
        }
        public TipModel GetRandomTips(){
            TipModel tip = new TipModel();
            try{
                outerResultSet = DBHelper.Select(" uspMobGetRandomTips");
                outerResultSet.next();
                tip.ID = outerResultSet.getInt("TTID");
                tip.TipDescription = outerResultSet.getString("TTdescription");
                    //item.ItemIcon = resultSet.getByte();
                DBHelper.Close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return tip;
        }
        public ArrayList<ReportLeakModel> GetReportedLeaks(){
            Location location = null;
            Geocoder geocoder;
            ArrayList<ReportLeakModel> leaks = new ArrayList<>();
            try
            {
                outerResultSet = DBHelper.Select("{CALL uspMobGetLeaks}");
                while(outerResultSet.next())
                {
                    ReportLeakModel leak = new ReportLeakModel();
                    leak.Location = outerResultSet.getString("Location");
                    //leak.Latitude = outerResultSet.getString("Latitude");
                    //leak.Longitude = outerResultSet.getString("Longitude");
                    leak.date = outerResultSet.getDate("DateReported");


                    //Double longitude = location.getLongitude();
                    //Double latitude = location.getLatitude();


                    //This section gets the address from the longitude and latitude.
                    //geocoder = new Geocoder(getContext(), Locale.getDefault());

                    //ddresses = geocoder.getFromLocation(latitude, longitude, 1);

                    //String address = addresses.get(0).getAddressLine(0);

                    //String fullAddress = address;

                    leaks.add(leak);
                }
                DBHelper.Close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            return leaks;
        }
        public boolean MobApproveTip(TipModel tip ){
            Object[] paras = {tip.ID,tip.Approved};
            boolean isWorking = DBHelper.NonQuery(" uspMobApproveTip ",paras);
            DBHelper.Close();
            return isWorking;
        }

        public ArrayList<ReportLeakModel> GetAdminReportedLeaks(){
            ArrayList<ReportLeakModel> leaks = new ArrayList<>();
            try
            {
                outerResultSet = DBHelper.Select("{CALL uspGetAdminReportedLeaks}");
                while(outerResultSet.next())
                {
                    ReportLeakModel leak = new ReportLeakModel();
                    leak.Location = outerResultSet.getString("Location");
                    //leak.Latitude = outerResultSet.getString("Latitude");
                    //leak.Longitude = outerResultSet.getString("Longitude");
                    leak.date = outerResultSet.getDate("DateReported");
                    leak.status = outerResultSet.getInt("StatusID");
                    leak.LeakID = outerResultSet.getInt("LeakID");

                    //Double longitude = location.getLongitude();
                    //Double latitude = location.getLatitude();


                    //This section gets the address from the longitude and latitude.
                    //geocoder = new Geocoder(getContext(), Locale.getDefault());

                    //ddresses = geocoder.getFromLocation(latitude, longitude, 1);

                    //String address = addresses.get(0).getAddressLine(0);

                    //String fullAddress = address;

                    leaks.add(leak);
                }
                DBHelper.Close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            return leaks;
        }

        public boolean MobApproveLeak(ReportLeakModel leak ){
            Object[] paras = {leak.LeakID,leak.status};
            boolean isWorking = DBHelper.NonQuery(" uspMobApproveLeak ",paras);
            DBHelper.Close();
            return isWorking;
        }
        public Business GetBusiness(){
            Business business = new Business();

            return  business;
        }
        public ArrayList< Surburb> WEBSuburbs(){
            ArrayList<Surburb> surburb = new ArrayList<>();
            try{

                outerResultSet = DBHelper.Select("uspWEBSuburbs");
                while (outerResultSet.next()) {

                    Surburb B = new Surburb();
                    B.ID = outerResultSet.getInt("SuburbID");
                    B.SurburbName = outerResultSet.getString("SuburbName");
                    surburb.add(B);
                }
            }catch (Exception e){

            }

            return  surburb;
        }
        public ArrayList< Surburb> WEBCities(){
            ArrayList<Surburb> surburb = new ArrayList<>();
            try{

                outerResultSet = DBHelper.Select("uspWEBCities");
                while (outerResultSet.next()) {

                    Surburb B = new Surburb();
                    B.ID = outerResultSet.getInt("CityID");
                    B.SurburbName = outerResultSet.getString("CityName");
                    surburb.add(B);
                }
            }catch (Exception e){

            }

            return  surburb;
        }

        public boolean WEBAddResident(String fullName,String email,String password,
                                      String houseNumber,String streetName,int suburID,int numberOfRes ){
            Object[] paras = { fullName, email, password,
                     houseNumber, streetName, suburID, numberOfRes };
            boolean isWorking = DBHelper.NonQuery(" uspWEBAddResident ",paras);
            DBHelper.Close();
            return isWorking;
        }
    }

    /**
     * Created by s216127904 on 2018/08/11.
     */

    public static class UploadImage extends AsyncTask<Void,Void,Void> {

        private String picName;
        private Bitmap image;
        public UploadImage (String PicName,Bitmap Image)
        {
            this.picName = PicName;
            this.image = Image;
        }
        @Override
        public Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            String compressedImageEncoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("name", picName);
            postDataParams.put("image", compressedImageEncoded);
            String saveUrl = "http://sict-iis.nmmu.ac.za/codecentrix/MobileConnectionString/SavePicture.php";
            performPostCall(saveUrl,postDataParams);

            return null;
        }
        public String  performPostCall(String requestURL,
                                       HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
        public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
        @Override
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    /**
     * Created by s216127904 on 2018/07/19.
     */

    public static class DownLoadPicture extends AsyncTask<Void,Void,Bitmap> {
        String name;
        public DownLoadPicture(String name)
        {
            if(name==null){
                name = "icon/complogo";
            }
            this.name = name;
        }
        @Override
        public Bitmap doInBackground(Void... voids) {
            String login_url = "http://sict-iis.nmmu.ac.za/codecentrix/MobileConnectionString/"+name.trim()+".png";
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(1000 * 30);
                httpURLConnection.setReadTimeout(1000 * 30);

                return BitmapFactory.decodeStream((InputStream) httpURLConnection.getContent(),null,null);
            }catch (Exception e){
                e.printStackTrace();
                return  null;
            }

        }
    }

    public static class GeneralMethods {
        Context context;
        public GeneralMethods(Context c)
        {
            context= c;
        }

        public void openWebPage(String url) {
           Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
           if ( intent.resolveActivity(context.getPackageManager()) != null) {
               context.startActivity( intent);
            }
        }

        public void writeToFile(String data,String fileName) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String readFromFile(String fileName) {
            String ret = "";
            try {if(context != null){
                InputStream inputStream = context.openFileInput(fileName);
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }}
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return ret;
        }
        public String[] Read(String fileName,String splitter){
            return readFromFile(fileName).split(splitter);
        }
        public String GetDate(){
            SimpleDateFormat df = new SimpleDateFormat("MMM dd");
            Date date = new Date();
            return df.format(date.getTime());
        }

        public Bitmap ScaleImg(int pic, Resources res){
            Bitmap scaledImg;
            BitmapFactory.Options op = new BitmapFactory.Options();

            op.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res,pic,op);



            op.inJustDecodeBounds = false;
            scaledImg = BitmapFactory.decodeResource(res,pic,op);

            return scaledImg;
        }

        public Bitmap ScaleImg(int pic, byte[] res){
            Bitmap scaledImg;
            BitmapFactory.Options op = new BitmapFactory.Options();

            op.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(res,0,res.length,op);

            int imgWidth = op.outWidth;
            if(imgWidth>1500){
                op.inSampleSize = 20;
            }else if(imgWidth>500){
                op.inSampleSize = 5;
            }else if(imgWidth>400){
                op.inSampleSize = 4;
            }else if(imgWidth>300){
                op.inSampleSize = 3;
            }else{
                op.inSampleSize = 2;
            }
            op.inJustDecodeBounds = false;
            scaledImg = BitmapFactory.decodeByteArray(res,0,res.length,op);

            return scaledImg;
        }
    }

    /**
     * Created by s216127904 on 2018/06/05.
     */

    public static class BackGroundConnection extends AsyncTask<String,String,String>  {

        public BackGroundConnection() {
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
            return doInBackground().split(",");
        }


    }
}
