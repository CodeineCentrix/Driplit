package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static android.support.v4.content.ContextCompat.startActivity;


public class GeneralMethods {
    Context context;
    public GeneralMethods(Context c)
    {
        context= c;
    }

    public void openWebPage(String url) {
       // Uri webpage = Uri.parse(url);
      //  Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
       // if (intent.resolveActivity(getPackageManager()) != null) {
       //     startActivity(context, intent);
      //  }
    }


}
