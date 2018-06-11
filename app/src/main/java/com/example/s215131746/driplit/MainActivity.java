package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "MainActivity";
private static  int timeOUt = 2000;
    Button btnRetry;
    LinearLayout l1;
    ConstraintLayout l2;
    Animation upToDown, downToUp;
    Intent loginScreen;
    ProgressBar pb;
    TextView tvLoadError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRetry = findViewById(R.id.btnReTry);
        btnRetry.setVisibility(View.INVISIBLE);
        tvLoadError = findViewById(R.id.tvLoadError);
        pb = findViewById(R.id.progressBar);
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

        ToRetry();

    }
    public void ToRetry()
    {
//        if(haveNetworkConnection())
//        {
            btnRetry.setVisibility(View.INVISIBLE);
            tvLoadError.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginScreen = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginScreen);
                    finish();
                }
            },timeOUt);
//        }
//        else
//        {
//            pb.setVisibility(View.INVISIBLE);
//            btnRetry.setVisibility(View.VISIBLE);
//            tvLoadError.setVisibility(View.VISIBLE);
//        }
    }
    public void ToRetry(View view)
    {
        ToRetry();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        //boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
           // if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
           //     if (ni.isConnected())
           //         haveConnectedMobile = true;
        }
        return haveConnectedWifi; //|| haveConnectedMobile;
    }

}
