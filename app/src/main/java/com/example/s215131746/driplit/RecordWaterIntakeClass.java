package com.example.s215131746.driplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by s216127904 on 2018/04/30.
 */
/**
 * CodeCentrix this class inherits from the fragment class because it is a tab. All tabs must inherit from this close
 *
**/
public class RecordWaterIntakeClass extends Fragment implements ImplementChange {
     TextView tvTotal;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //this line inflate this class with the record water intake layout
        View rootView = inflater.inflate(R.layout.activity_record_water_intake, container, false);
        ScaleImg(R.id.imgView);
        //here is the attempt to create and n tier architecture
        bll business = new bll();
        business.LoadConnection();
        String[] ItemName = business.getItemName();
        String[] Avg = business.getItemAverageUse();
        int[] icon = business.getItemIcon();

        tvTotal = rootView.findViewById(R.id.tvTotalQty);
        final ItemListAdapter listAdapter = new ItemListAdapter(getContext(),icon,ItemName,Avg,this);
        ListView lvItemList = rootView.findViewById(R.id.lvItemList);

        //this line is to inflate the listview with the list view adapter
        lvItemList.setAdapter(listAdapter);
        //this is when someone clicks on the item on the list view
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
}
