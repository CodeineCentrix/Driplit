package com.example.s215131746.driplit;

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
public class RecordWaterIntakeClass extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //this line inflate this class with the record water intake layout
        View rootView = inflater.inflate(R.layout.activity_record_water_intake, container, false);
        //here is the attempt to create and n tier architecture
        bll business = new bll();
        String[] ItemName = business.getItemName();
        String[] Avg = business.getItemAverageUse();
        int[] icon = business.getItemIcon();

        final TextView tvTotal = rootView.findViewById(R.id.tvTotalQty);
        final ItemListAdapter listAdapter = new ItemListAdapter(getContext(),icon,ItemName,Avg);
        ListView lvItemList = rootView.findViewById(R.id.lvItemList);

        //this line is to inflate the listview with the list view adapter
        lvItemList.setAdapter(listAdapter);
        //this is when someone clicks on the item on the list view
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                //after an item has been clicked the the following line will either make the bottom controllers visible or invisible
                listAdapter.setVisibility(i);
                if(listAdapter.isVisibie(i)==true)
                {
                    //the following code is to add to the total
                    int tt = Integer.parseInt(tvTotal.getText().toString());
                    tt = listAdapter.getUsage(i,tt);
                    String stt = ""+tt;
                    if(stt.length()==1)
                    {
                        tvTotal.setText("00"+stt);
                    }
                    else if(stt.length()==2)
                    {
                        tvTotal.setText("0"+stt);
                    }
                    else
                    {
                        tvTotal.setText(stt);
                    }

                    Toast.makeText(getContext(),"Record successfull",Toast.LENGTH_LONG).show();
                }

            }
        });
        return rootView;
    }

}
