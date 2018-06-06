package com.example.s215131746.driplit;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLException;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.String.*;

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
    private ArrayList<ResidentUsageModel> PreviousUsage;

    public ItemListAdapter(Context c,RecordWaterIntakeClass rwic,ArrayList<ItemUsageModel> itemUsage,
                           ArrayList<ResidentUsageModel> previousUsage)
    {
        context = c;
        PreviousUsage = previousUsage;
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
                setVisibility(position);
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
    private float GetAllUsage()
    {
        float all=0;
        for (ResidentUsageModel res: PreviousUsage) {
            all+=res.AmountUsed;
        }
        return all;
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
    public void RecordUsage(View v, final int position, EditText txtItemUsage, final TextView tvUsed )
    {
        final Login l = new Login();
        l.writeToFile("do",context.getApplicationContext(),"Recording.txt");
        final int duration = 7000;
        final float Used = Float.parseFloat(txtItemUsage.getText().toString());
        if(Used>0)
        {
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String date = df.format(Calendar.getInstance().getTime());
                final DecimalFormat dc = new DecimalFormat("0.0");
            final DBAccess business = new DBAccess();
            final ResidentUsageModel resUsing = new ResidentUsageModel();
            resUsing.ItemID =Integer.parseInt( ItemID[position]);
            resUsing.AmountUsed = Used;
            float x = Float.parseFloat(tvUsed.getText().toString());
            x+=resUsing.AmountUsed;
            tvUsed.setText(""+x);
            txtItemUsage.setText("0");
            String value = dc.format(Float.parseFloat(parentCange.GetValue())+Used);
            parentCange.DoChanges(value);
            try {
                resUsing.ResDate = new Date(df.parse(date).getTime());
                df = new SimpleDateFormat("hh:mm:ss");
                date = df.format(Calendar.getInstance().getTime());
                resUsing.ResTime = new Time(df.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String[] person = l.readFromFile(context.getApplicationContext(),"person.txt").split(",");
            resUsing.PersonID = Integer.parseInt(person[0]);
            Handler h = new Handler();
            Snackbar mySnackbar = Snackbar.make(v,R.string.record_successful, duration);
            mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.writeToFile("undo",context.getApplicationContext(),"Recording.txt");
                    String value = dc.format(Float.parseFloat(parentCange.GetValue())-Used);
                    parentCange.DoChanges(value);
                    tvUsed.setText(""+Used);
                    float x = Float.parseFloat(tvUsed.getText().toString());
                    x-=resUsing.AmountUsed;
                    tvUsed.setText(""+x);
                }


            });
            mySnackbar.show();


           h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(l.readFromFile(context.getApplicationContext(),"Recording.txt").equals("do"))
                    {
                        business.MobAddResidentUsage(resUsing);
                    }

                }
                //1 second wait before saving to the database
            },duration);

           }


    }


}
