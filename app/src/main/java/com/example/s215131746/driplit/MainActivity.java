package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "MainActivity";
private static  int timeOUt = 2000;

    LinearLayout l1;
    ConstraintLayout l2;
    Animation upToDown, downToUp;
    Intent loginScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LaunchMain();
    }
    public void LaunchMain()
    {

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (ConstraintLayout) findViewById(R.id.l2);
        upToDown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        l1.setAnimation(upToDown);

        downToUp = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l2.setAnimation(downToUp);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loginScreen = new Intent(getApplicationContext(), Mainmenu.class);
                startActivity(loginScreen);
            }

        },timeOUt);

    }

}
