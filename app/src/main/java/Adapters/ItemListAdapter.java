package Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s215131746.driplit.R;
import com.example.s215131746.driplit.RecordWaterIntakeClass;
import com.example.s215131746.driplit.TabMenu;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Interfaces.ImplementChange;
import viewmodels.ItemUsageModel;
import viewmodels.PersonModel;
import viewmodels.ResidentUsageModel;
import viewmodels.UspMobGetPersonItemTotal;

public class ItemListAdapter extends BaseAdapter {
    private TabMenu.GeneralMethods generalMethods;
    private ArrayList<ItemUsageModel> ItemUsage;
    private ArrayList<UspMobGetPersonItemTotal> PreviousUsage;
    private float[] personUsage;
    private String[] stringsQTY;
    private LinearLayout[] LoDropHides;
    private LinearLayout[] loHeading;
    private LayoutInflater mInflater;
    private ImplementChange parentCange;
    private TextView[] tvUsed;
    private Context context;
    public ItemListAdapter(Context c, RecordWaterIntakeClass rwic, ArrayList<ItemUsageModel> itemUsage,
                           ArrayList<UspMobGetPersonItemTotal> previousUsage){
        context = c;
        PreviousUsage = previousUsage;
        ItemUsage = itemUsage;
        parentCange = rwic;
        int i =itemUsage.size();
        personUsage = new float[i];
        tvUsed = new TextView[i];
        //Loading previous usage for specific items
        for(i =0;i<ItemUsage.size();i++) {
            for (int z = 0; z < PreviousUsage.size(); z++) {
                int x = ItemUsage.get(i).ItemID, y = previousUsage.get(z).ItemID;
                if (x == y) {
                    personUsage[i] = previousUsage.get(z).UsageAmount;
                    break;
                }
            }
        }
        mInflater =(LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        stringsQTY = new String[getCount()];
        LoDropHides = new LinearLayout[getCount()];
        loHeading = new LinearLayout[getCount()];
        generalMethods = new TabMenu.GeneralMethods(c);
    }
    @Override
    public int getCount() {
        return ItemUsage.size();
    }
    @Override
    public Object getItem(int position) {
        return ItemUsage.get(position).ItemDescription;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.water_intake_item_layout_v2,null);

        //Icon
        final ImageView icon = v.findViewById(R.id.imgItemIcon);
        try {
              icon.setImageBitmap(ItemUsage.get(position).ItemIcon);
        }catch (Exception e){
            e.printStackTrace();
        }

        //text views
        final TextView tvQty = v.findViewById(R.id.tvQty);
        stringsQTY[position] = tvQty.getText().toString();
        final TextView tvItemName = v.findViewById(R.id.tvItemName);
        tvItemName.setText(ItemUsage.get(position).ItemDescription);
        tvUsed[position] = v.findViewById(R.id.tvUsedToday);
        String value = ""+ personUsage[position];
        tvUsed[position].setText(value);
        //text edits
        final EditText txtItemUsage = v.findViewById(R.id.txtItemUsage);
        SetUsageEditText(txtItemUsage,position,Integer.parseInt(stringsQTY[position]));
        //imgIcons = v.findViewById(R.id.imgItemIcon);
        //imgIcons.setImageResource(ItemIcon[position]);
        LoDropHides[position] = v.findViewById(R.id.loDropHide);
        loHeading[position] = v.findViewById(R.id.loHeading);
        setVisibility(position);
        //Buttons
        final Button btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int increasUsage =Integer.parseInt( tvQty.getText().toString()) + 1;
                ChangeUsage(tvQty,position,txtItemUsage,increasUsage);
            }
        });
        final Button btnMinus = v.findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int decreasUsage =Integer.parseInt( tvQty.getText().toString()) - 1;
                if(decreasUsage>-1){
                    ChangeUsage(tvQty,position,txtItemUsage,decreasUsage);
                }
            }
        });
        final Button  btnRecord = v.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordUsage(v,position,txtItemUsage,tvUsed[position]);
                tvQty.setText("0");
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(position);
                    }
                    //allow the user to see the recorded amount
                },500);
            }
        });
        return v;
    }
    public void setVisibility(int position ){
        if(LoDropHides[position].getVisibility()==View.GONE){
            LoDropHides[position].setVisibility(View.VISIBLE);
            loHeading[position].setBackgroundColor(context.getResources().getColor(R.color.darkerblue));
        }else{
            loHeading[position].setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            LoDropHides[position].setVisibility(View.GONE);
        }
    }
    private void SetUsageEditText(EditText txtItemUsage,int position,int Quatity){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float i = ItemUsage.get(position).ItemAverage*Quatity;
        txtItemUsage.setText(""+df.format(i));
    }
    private void ChangeUsage(TextView tvQty,int position,EditText txtItemUsage,int value){
        String v = ""+value;
        tvQty.setText(v);
        stringsQTY[position] = ""+value;
        float i = ItemUsage.get(position).ItemAverage * value;
        v =""+i;
        txtItemUsage.setText(v);
    }
    private void RecordUsage(View v, final int position, EditText txtItemUsage, final TextView tvUsed ){
        generalMethods.writeToFile("do","Recording.txt");
        final int duration = 3000;
        final float Used = Float.parseFloat(txtItemUsage.getText().toString());
        if(Used>0){
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String date = df.format(Calendar.getInstance().getTime());
            final DecimalFormat dc = new DecimalFormat("0.0");
            final TabMenu.DBAccess business = new TabMenu.DBAccess();
            final ResidentUsageModel resUsing = new ResidentUsageModel();
            resUsing.ItemID =ItemUsage.get(position).ItemID;
            resUsing.AmountUsed = Used;
            float x = Float.parseFloat(tvUsed.getText().toString());
            x+=resUsing.AmountUsed;
            String value = ""+x;
            tvUsed.setText(value);
            txtItemUsage.setText("0");
            try {
                value = dc.format(Float.parseFloat(parentCange.GetValue())+Used);
            }catch (NumberFormatException e){
                Toast.makeText(context,"Please use US numbering system",Toast.LENGTH_SHORT);
                String[] GetValue = parentCange.GetValue().split(",");
                String get =String.valueOf (Float.parseFloat(GetValue[1])/10 + Float.parseFloat(GetValue[0]));
                value = dc.format(Float.parseFloat(get)+Used);
            }
            parentCange.DoChanges(value);
            try {
                resUsing.ResDate = new Date(df.parse(date).getTime());
                df = new SimpleDateFormat("hh:mm:ss");
                date = df.format(Calendar.getInstance().getTime());
                resUsing.ResTime = new Time(df.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String[] person = generalMethods.Read("person.txt",",");
            resUsing.PersonID = Integer.parseInt(person[PersonModel.ID]);
            Handler h = new Handler();
            Snackbar mySnackbar = Snackbar.make(v,R.string.record_successful, duration);
            mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generalMethods.writeToFile("undo","Recording.txt");
                    String value;
                    try {
                        value =  dc.format(Float.parseFloat(parentCange.GetValue())-Used);
                    }catch (NumberFormatException e){
                        Toast.makeText(context,"Please use US numbering system",Toast.LENGTH_SHORT);
                        String[] GetValue = parentCange.GetValue().split(",");
                        String get =String.valueOf (Float.parseFloat(GetValue[1])/10 + Float.parseFloat(GetValue[0]));
                        value = dc.format(Float.parseFloat(get)-Used);
                    }
                    parentCange.DoChanges(value);
                    value = Used+"";
                    tvUsed.setText(value);
                    float x = Float.parseFloat(tvUsed.getText().toString());
                    x-=resUsing.AmountUsed;
                    value = ""+x;
                    tvUsed.setText(value);
                }


            });
            mySnackbar.show();
           h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(generalMethods.readFromFile("Recording.txt").equals("do")){
                        business.MobAddResidentUsage(resUsing);
                    }

                }
                //1 second wait before saving to the database
            },duration);
        }
    }
}
