package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Adapters.ItemListAdapter;
import Adapters.TipListAdapter;
import Interfaces.ImplementChange;
import viewmodels.ItemUsageModel;
import viewmodels.PersonModel;
import viewmodels.UspMobGetPersonItemTotal;

/**
 * Created by s216127904 on 2018/04/30.
 */
/**
 * CodeCentrix this class inherits from the fragment class because it is a tab. All tabs must inherit from this close
 *
**/
public class RecordWaterIntakeClass extends Fragment implements ImplementChange {
    GeneralMethods m ;
    TextView tvTotal;
    ArrayList<ItemUsageModel> listOfItem;
    DBAccess business = new DBAccess();
    ArrayList<UspMobGetPersonItemTotal> usagForItem;
    String[] value;
    View mainView;
    Handler handler = new Handler();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //this line inflate this class with the record water intake layout
        final View rootView = inflater.inflate(R.layout.activity_record_water_intake, container, false);
        m = new GeneralMethods(getContext());
        value = m.Read(rootView.getContext().getString(R.string.person_file_name),",");
        mainView = rootView;
        ScaleImg(R.id.imgView);
        helpThread h = new helpThread(true,rootView.getContext());
        new Thread(h).start();
        LinearLayout linearLayout = rootView.findViewById(R.id.linearLayout);
        Animation anime = AnimationUtils.makeInAnimation(rootView.getContext(),true);
        anime.setDuration(2000);
        linearLayout.setAnimation(anime);
        return rootView;
    }
    public Bitmap ScaleImg(int pic){
        Bitmap scaledImg;
        BitmapFactory.Options op = new BitmapFactory.Options();

        op.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),pic,op);

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
        scaledImg = BitmapFactory.decodeResource(getResources(),pic,op);

        return scaledImg;
    }
    public void afterConnection(View rootView){

        float totalUsage =0;
        for (UspMobGetPersonItemTotal prev:usagForItem) {
            totalUsage += prev.UsageAmount;
        }
        final DecimalFormat dc = new DecimalFormat("0.0");
        tvTotal = rootView.findViewById(R.id.tvTotalQty);
        tvTotal.setText(dc.format(totalUsage));
        final ItemListAdapter listAdapter = new ItemListAdapter(getContext(),this,
                listOfItem,usagForItem);
        //finding and inflating list view
        ListView lvItemList = rootView.findViewById(R.id.lvItemList);
        lvItemList.setAdapter(listAdapter);
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //after an item has been clicked the the following line will either make the bottom controllers visible or invisible
                listAdapter.setVisibility(i);
            }
        });
    }
    @Override
    public void DoChanges(String value) {
        tvTotal.setText(value);
    }

    @Override
    public String GetValue() {
        return tvTotal.getText().toString();
    }
    class helpThread implements Runnable {
        Context context;
        boolean onCreate;
        boolean isConnecting;
        Snackbar mySnackbar;
        public helpThread() {

        }

        public helpThread(boolean onCreate,Context context) {
            this.onCreate = onCreate;
            this.context = context;
        }


        @Override
        public void run() {
            if(onCreate){

                isConnecting = business.isConnecting();
                if(isConnecting) {

                    listOfItem = business.GetItems();
                    usagForItem = business.uspMobGetPersonItemTotal(value[PersonModel.EMAIL]);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            afterConnection(mainView);
                        }
                    });
                }else {
                    mySnackbar = Snackbar.make(mainView,"No Connection", 8000);
                    mySnackbar.getView().setBackgroundColor(Color.RED);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mySnackbar.show();

                        }
                    });

                }
            }
        }
    }
}
