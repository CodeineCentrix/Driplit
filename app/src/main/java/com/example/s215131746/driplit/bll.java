package com.example.s215131746.driplit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by s216127904 on 2018/05/01.
 */

public class bll {
    public String[] getItemName()
    {
        String[] ItemName = {"Bath","Shower","Toilet","Garden","Dish Washer","Washing Machine","Brush teeth"};
        return ItemName;
    }
    public String[] getItemAverageUse()
    {
        String[] Avg = {"5","6","2","2","5","8","1"};
        return  Avg;
    }
    public int[] getItemIcon()
    {
        int[] icon = {5,6,2,2,5,8,1};
        return  icon;
    }
    public String getPassword()
    {
        return null;
    }
    public String getUsername()
    {
        return  null;
    }
    public Bitmap ScaleImg(int pic, Resources res)
    {
        Bitmap scaledImg;
        BitmapFactory.Options op = new BitmapFactory.Options();

        op.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,pic,op);

        int imgWidth = op.outWidth;
        if(imgWidth>1500)
        {
            op.inSampleSize = 20;
        }
        else if(imgWidth>500)
        {
            op.inSampleSize = 5;
        }
        else if(imgWidth>400)
        {
            op.inSampleSize = 4;
        }
        else if(imgWidth>300)
        {
            op.inSampleSize = 3;
        }
        else
        {
            op.inSampleSize = 2;
        }


        op.inJustDecodeBounds = false;
        scaledImg = BitmapFactory.decodeResource(res,pic,op);

        return scaledImg;
    }

}
