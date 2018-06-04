package com.example.s215131746.driplit;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter {

    private ArrayList<ItemUsageModel> ItemUsage;
    private int[] ItemIcon;
    private String[] ItemName;
    private String[] ItemUsageAvg;
    private String[] ItemID;
    private LinearLayout loDropHide;
    String[] stringsQTY;
    LinearLayout[] LoDropHides;
    Context context;
    private TextView tvTimesUsedORActual;
    LayoutInflater mInflater;
    ImplementChange parentCange;

    public ItemListAdapter(Context c,RecordWaterIntakeClass rwic,ArrayList<ItemUsageModel> itemUsage)
    {
        ItemUsage = itemUsage;
        parentCange = rwic;
        int i =itemUsage.size();
        ItemIcon = new int[i];
        ItemName = new String[i];
        ItemUsageAvg = new String[i];
        ItemID = new String[i];
        for (ItemUsageModel item:itemUsage) {
            i--;
            //ItemIcon must be bytes
            ItemIcon[i] = i;
            ItemID[i] = ""+item.ItemID;
            ItemName[i] = item.ItemDiscriotn;
            ItemUsageAvg[i] = ""+item.ItemAverage;
        }
        mInflater =(LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        stringsQTY = new String[getCount()];
        LoDropHides = new LinearLayout[getCount()];
    }
    @Override
    public int getCount() {
        return ItemName.length;
    }

    @Override
    public Object getItem(int position) {
        return ItemName[position];
    }
    public String getItemAvg(int position) {
        return ItemUsageAvg[position];
    }
    public float getUsage(int position,float TotalQty)
    {
       float qty = Float.parseFloat (stringsQTY[position]);
        qty = qty*Float.parseFloat (ItemUsageAvg[position]);
        qty = qty + TotalQty;
        return  qty;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.water_intake_item_layout_v2,null);
        tvTimesUsedORActual = v.findViewById(R.id.tvLabel);
        //text views
        final TextView tvQty = v.findViewById(R.id.tvQty);
        stringsQTY[position] = tvQty.getText().toString();
        final TextView tvItemName = v.findViewById(R.id.tvItemName);
        tvItemName.setText(ItemName[position]);
        final TextView tvUsed = v.findViewById(R.id.tvUsedToday);
        //text edits
        final EditText txtItemUsage = v.findViewById(R.id.txtItemUsage);
        SetUsageEditText(txtItemUsage,position,Integer.parseInt(stringsQTY[position]));
        //imgIcons = v.findViewById(R.id.imgItemIcon);
        //imgIcons.setImageResource(ItemIcon[position]);
        loDropHide = v.findViewById(R.id.loDropHide);
        LoDropHides[position] = loDropHide;
       setVisibility(position);
     //Buttons
        final Button btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int increasUsage =Integer.parseInt( tvQty.getText().toString()) + 1;
                ChangeUsage(tvQty,position,txtItemUsage,tvUsed,increasUsage);
            }
        });
        final Button btnMinus = v.findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int decreasUsage =Integer.parseInt( tvQty.getText().toString()) - 1;
                if(decreasUsage>-1)
                {
                    ChangeUsage(tvQty,position,txtItemUsage,tvUsed,decreasUsage);
                }
            }
        });
       final Button  btnRecord = v.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordUsage(v,position,txtItemUsage,tvUsed);
                tvQty.setText("0");
            }
        });
        return v;
    }

    public void setVisibility(int position )
    {
        if(LoDropHides[position].getVisibility()==View.INVISIBLE)
            LoDropHides[position].setVisibility(View.VISIBLE);
        else
            LoDropHides[position].setVisibility(View.INVISIBLE);
    }
    public void SetUsageEditText(EditText txtItemUsage,int position,int Quatity)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float i = Float.parseFloat(ItemUsageAvg[position])*Quatity;
        txtItemUsage.setText(""+df.format(i));
    }
    public void ChangeUsage(TextView tvQty,int position,EditText txtItemUsage,TextView tvUsed,int value)
    {
        tvQty.setText("" + value);
        stringsQTY[position] = ""+value;
        float i = Float.parseFloat(ItemUsageAvg[position]) * value;
        txtItemUsage.setText(""+i);

    }
    public void RecordUsage(View v,int position,EditText txtItemUsage,TextView tvUsed )
    {
        //itemRecorded value must be saved to the database
        float itemRecorded = Float.parseFloat (tvUsed.getText().toString());
        tvUsed.setText(""+getUsage(position,itemRecorded));
        //TT must be a value from the database
        float totalValue = Float.parseFloat (parentCange.GetValue());
        totalValue += getUsage(position,itemRecorded);
        if(totalValue>0)
        {
            parentCange.DoChanges(""+totalValue);
            Toast.makeText(v.getContext(),"Intake Record Successful",Toast.LENGTH_LONG).show();
        }
        SetUsageEditText(txtItemUsage,position,0);
    }
}
