package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class IntakeHelper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_helper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ImageView[] intakeHelp = new ImageView[]{
//                        findViewById(R.id.imageView1),
//                        findViewById(R.id.imageView2),
//                        findViewById(R.id.imageView3),
//                        findViewById(R.id.imageView4),
//                        findViewById(R.id.imageView5),
//                        findViewById(R.id.imageView6),
//                        findViewById(R.id.imageView7)
//        };
//        int[] pic = new int[]{
//                R.drawable.record_intake1,
//                R.drawable.record_intake2,
//                R.drawable.record_intake3,
//                R.drawable.record_intake4,
//                R.drawable.record_intake5,
//                R.drawable.record_intake6,
//                R.drawable.record_intake7,
//        };
//        GeneralMethods m = new GeneralMethods(getApplicationContext());
//        for (int i=0;i<intakeHelp.length;i++){
//            intakeHelp[i].setImageBitmap(m.ScaleImg(pic[i],getResources()));
//        }
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent (getApplicationContext(), HelpScreen.class);
                startActivity(help);
            finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showRecord = new Intent(getApplicationContext(),TabMenu.class);
                Bundle i = new Bundle();
                i.putString("Tab", "Record");
                showRecord.putExtras(i);
                showRecord.putExtras(getIntent());
                startActivity(showRecord);
                finish();
            }
        });
    }
}
