package com.example.s215131746.driplit;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import viewmodels.PersonModel;

public class TabMenu extends AppCompatActivity {
    public PersonModel person = new PersonModel();
    GeneralMethods m;
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
    int i=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);

        m = new GeneralMethods(this);
        String[] p = m.Read(this.getString(R.string.person_file_name),",");
        person.fullName = p[PersonModel.FULLNAME];
        person.email = p[PersonModel.EMAIL];
        //person.Usagetarget = Integer.parseInt(p[PersonModel.USAGETARGET]);
        person.id =Integer.parseInt(p[PersonModel.ID]);
        person.userPassword = p[PersonModel.PASSWORD];
        try {
            person.getOldapproved = Integer.parseInt(p[PersonModel.OLDAPPROVED]);
        }catch (Exception e){
            Toast.makeText(this,"Please clear data for this app or login again",Toast.LENGTH_LONG).show();
        }
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
            for(i = 0; i <tabs.length;i++){
                if(tabName.equalsIgnoreCase(tabs[i]) ){
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
//        for(int i = 0; i <tabs.length-1;i++){
//            if(tabName.equalsIgnoreCase(tabs[i]) ){
//                mViewPager.setCurrentItem(i);
//
//            }
//        }
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
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
        int id = item.getItemId();
        switch (id){
            case R.id.action_reportedLeaks:

                Intent rLeaks = new Intent(this.getApplicationContext(), ReportedLeaks.class);
                Bundle bundle = new Bundle();
                bundle.putString("personID",""+person.id);
                rLeaks.putExtras(bundle);
                rLeaks.putExtras(getIntent());
                startActivity(rLeaks);
                return super.onOptionsItemSelected(item);

            case R.id.action_edit_profile:

                Intent ed = new Intent(this.getApplicationContext(), EditProfile.class);
                startActivity(ed);
                return super.onOptionsItemSelected(item);
            case R.id.action_help:

                if(mViewPager.getCurrentItem()==0){
                    Intent ih = new Intent(this.getApplicationContext(), IntakeHelper.class);
                    startActivity(ih);
                    return super.onOptionsItemSelected(item);
                }
                Intent report = new Intent(this.getApplicationContext(), HelpScreen.class);
                startActivity(report);
                return super.onOptionsItemSelected(item);
            case R.id.action_sign_out:
                Bundle z = new Bundle();
                Intent lo = new Intent(this.getApplicationContext(), Login.class);
                z.putString("out","out");
                lo.putExtras(z);
                lo.putExtras(getIntent());
                lo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lo);
                finish();

                return super.onOptionsItemSelected(item);
            case R.id.action_snitch_board:
                Intent snitchBoard = new Intent(this.getApplicationContext(), SnitchActivity.class);
                startActivity(snitchBoard);
                return super.onOptionsItemSelected(item);
            case R.id.homeAsUp:
                finish();
                return super.onOptionsItemSelected(item);
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
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

}
