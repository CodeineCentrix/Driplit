package com.example.s215131746.driplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s216127904 on 2018/04/30.
 */
/**
 * CodeCentrix this class inherits from the fragment class because it is a tab. All tabs must inherit from this close
 *
**/
public class RecordWaterIntakeClass extends Fragment implements ImplementChange {
    Login l = new Login();
    TextView tvTotal;
    ArrayList<ItemUsageModel> listOfItem;
    DBAccess business = new DBAccess();
    ArrayList<ResidentUsageModel> usagForItem;
    String[] value;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //this line inflate this class with the record water intake layout
        View rootView = inflater.inflate(R.layout.activity_record_water_intake, container, false);

        ScaleImg(R.id.imgView);
        listOfItem = business.GetItems();
        value = l.readFromFile(getContext(),"person.txt").split(",");
        usagForItem = business.uspMobGetPersonItemTotal(value[2]);
        float totalUsage =0;
        for (ResidentUsageModel prev:usagForItem) {
                totalUsage = prev.AmountUsed;
        }

        tvTotal = rootView.findViewById(R.id.tvTotalQty);
        tvTotal.setText(""+totalUsage);
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
        return rootView;
    }
    public Bitmap ScaleImg(int pic)
    {
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
    @Override
    public void DoChanges(String value) {
        tvTotal.setText(value);
    }

    @Override
    public String GetValue() {
        return tvTotal.getText().toString();
    }

    public void ToHome()
    {
       // AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

       // builder.setMessage("Do you want to save usage")
       //         .setPositiveButton("Save", new DialogInterface.OnClickListener() {
       //             @Override
       //             public void onClick(DialogInterface dialog, int which) {
       //                 //Write code to save new recodings to the database
       //                 Toast.makeText(getContext(),"Recorded usage",Toast.LENGTH_SHORT).show();

       //             }
       //         })
       //         .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
       //             @Override
       //             public void onClick(DialogInterface dialog, int which) {

       //             }
       //         })
       //         .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
       //             @Override
       //             public void onClick(DialogInterface dialog, int which) {

       //             }
       //         });
       // AlertDialog startSaving = builder.create();
       // startSaving.show();

    }
}
