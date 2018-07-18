package com.example.s215131746.driplit;

import android.arch.lifecycle.ReportFragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Mainmenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fodscreen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        GeneralMethods m = new GeneralMethods(getApplicationContext());
        String[] details = m.Read("person.txt",",");
        NavigationView navigationView = findViewById(R.id.nav_view);


        View header = navigationView.getHeaderView(0);
        TextView tvFullName = (TextView) header.findViewById(R.id.nav_tvFullName);
        TextView tvEmail = header.findViewById(R.id.nav_tvEmail);
        tvEmail.setText(details[2]);
        tvFullName.setText(details[1]);
        navigationView.setNavigationItemSelectedListener(this);


    }

    //Trying to link onClick of EditProfile button to Fragment EditProfile to display EditProfile intent.
    public void EditProfileScreen(View view)
    {

        Intent showEditProfile = new Intent(getApplicationContext(),EditProfile.class);
        //SetBundel(showEditProfile,"Edit");
        showEditProfile.putExtras(getIntent());
        startActivity(showEditProfile);

    }
    public void TrendScreen(View view)
    {
        Intent showTrend = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showTrend,"ItemTrend");
    }
    public void TipScreen(View view)
    {
        Intent showTips = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showTips,"Tips");
    }
    public void RecordScreen(View view)
    {
        Intent showRecord = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showRecord,"Record");
    }
    public void ReportScreen(View view)
    {
        Intent showReport = new Intent(getApplicationContext(),TabMenu.class);
        SetBundel(showReport,"Report");
    }
    public void FactOfTheDayScreen(View view)
    {
        Intent showFOD = new Intent(getApplicationContext(),FODScreen.class);
        startActivity(showFOD);
    }
    public void SetBundel(Intent in,String tabName)
    {
        Bundle i = new Bundle();
        i.putString("Tab", tabName);
        in.putExtras(i);
        in.putExtras(getIntent());
        DBAccess business = new DBAccess();
        boolean wifi = true;
        try {
            business.GetTips();
        }catch (NullPointerException e)
        {
            wifi = false;
        }

        //  }
        if(!wifi)
        {
            Toast.makeText(this,"PLEASE TURN ON WIFI",Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(in);
            finish();
        }

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            Intent signOut = new Intent(getApplicationContext(),Login.class);
            startActivity(signOut);
            finish();
        }
        else if(id == R.id.nav_help)
        {
            Intent help = new Intent (getApplicationContext(), HelpScreen.class);
            startActivity(help);
            finish();
        }
        else if(id == R.id.nav_aboutus)
        {
            Intent aboutUs = new Intent (getApplicationContext(), AboutUs.class);
            startActivity(aboutUs);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fodscreen, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
