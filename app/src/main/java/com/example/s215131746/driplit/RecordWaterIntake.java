package com.example.s215131746.driplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordWaterIntake extends AppCompatActivity {
    LinearLayout loDropHide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_water_intake);
        String[] ItemName = {"Bath","Shower","Toilet","Garden","Dish Washer","Washing Machine"};
        String[] Avg = {"5","6","2","2","5","8"};
        int[] icon = {5,6,2,2,5,8};
        final TextView tvTotal = findViewById(R.id.tvTotalQty);
        final ItemListAdapter listAdapter = new ItemListAdapter(this,icon,ItemName,Avg);
        ListView lvItemList = findViewById(R.id.lvItemList);

        lvItemList.setAdapter(listAdapter);
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RecordWaterIntake.this," "+ listAdapter.getItem(i),Toast.LENGTH_LONG).show();
                listAdapter.setVisible(view);

                final EditText txtUsage = view.findViewById(R.id.txtItemUsage);

                final Button btnDone; btnDone = view.findViewById(R.id.btnDone);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = Integer.parseInt(tvTotal.getText().toString())+ Integer.parseInt(txtUsage.getText().toString());
                    tvTotal.setText(""+p);
                    }
                });
            }
        });




    }
}
