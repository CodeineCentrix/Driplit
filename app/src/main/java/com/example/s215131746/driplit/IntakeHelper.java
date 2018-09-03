package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

public class IntakeHelper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_helper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //code for back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ImageView[] intakeHelp = new ImageView[]{
                        findViewById(R.id.imageView1),
                        findViewById(R.id.imageView2),
                        findViewById(R.id.imageView3),
                        findViewById(R.id.imageView4),
                        findViewById(R.id.imageView5),
                        findViewById(R.id.imageView6),
                        findViewById(R.id.imageView7)
        };
        int[] pic = new int[]{
                R.drawable.record_intake1,
                R.drawable.record_intake2,
                R.drawable.record_intake3,
                R.drawable.record_intake4,
                R.drawable.record_intake5,
                R.drawable.record_intake6,
                R.drawable.record_intake7,
        };
        TabMenu.GeneralMethods m = new TabMenu.GeneralMethods(getApplicationContext());
        for (int i=0;i<intakeHelp.length;i++){
            intakeHelp[i].setImageBitmap(m.ScaleImg(pic[i],getResources()));
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
