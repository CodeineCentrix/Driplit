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

public class ItemListAdapter extends BaseAdapter {

    private int[] ItemIcon;
    private String[] ItemName;
    private String[] ItemUsageAvg;

    private LinearLayout loDropHide;
    private ImageView imgIcons;

    String[] stringsQTY;
    LinearLayout[] LoDropHides;




    private TextView tvTimesUsedORActual;
    LayoutInflater mInflater;

    public ItemListAdapter(Context c,int[] itemIcon,String[] itemName,String[] itemUsageAvg)
    {
        ItemIcon = itemIcon;
        ItemName =itemName ;
        ItemUsageAvg = itemUsageAvg;
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
    public int getUsage(int position,int TotalQty)
    {
        int qty = Integer.parseInt(stringsQTY[position]);
        qty = qty*Integer.parseInt(ItemUsageAvg[position]);
        qty = qty + TotalQty;

        return  qty;
    }
    public int getQty(int position)
    {
        View v = getView(position,null,null);
        TextView t = v.findViewById(R.id.tvQty);
        int i = Integer.parseInt(t.getText().toString());
        return i;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.water_intake_item_layout,null);

        tvTimesUsedORActual = v.findViewById(R.id.tvLabel);

        final TextView tvQty = v.findViewById(R.id.tvQty);
        stringsQTY[position] = tvQty.getText().toString();
        final EditText txtItemUsage = v.findViewById(R.id.txtItemUsage);
        txtItemUsage.setText(ItemUsageAvg[position]);

        //imgIcons = v.findViewById(R.id.imgItemIcon);
        //imgIcons.setImageResource(ItemIcon[position]);
        loDropHide = v.findViewById(R.id.loDropHide);
        LoDropHides[position] = loDropHide;
        if(loDropHide.getVisibility()==View.INVISIBLE)
                loDropHide.setVisibility(View.VISIBLE);
            else
                loDropHide.setVisibility(View.INVISIBLE);

        final TextView tvItemName = v.findViewById(R.id.tvItemName);
        tvItemName.setText(ItemName[position]);






     /*buttons____________________________________________________________________________*/
        final Button btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i =Integer.parseInt( tvQty.getText().toString()) + 1;
                tvQty.setText(""+i);
                stringsQTY[position] = ""+i;
                i =Integer.parseInt( ItemUsageAvg[position]) *i;
                txtItemUsage.setText(""+i);
            }
        });
        final Button btnMinus = v.findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i =Integer.parseInt( tvQty.getText().toString()) - 1;
                if(i>=0) {
                    tvQty.setText("" + i);
                    stringsQTY[position] = ""+i;
                    i = Integer.parseInt(ItemUsageAvg[position]) * i;
                    txtItemUsage.setText("" + i);
                }
            }
        });
        final Button btnEdit = v.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final TextView tvTotal = parent.findViewById(R.id.tvTotalQty);
        final Button btnDone; btnDone = v.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*End buttons____________________________________________________________________________*/


        return v;
    }
    public void setVisibility(int position )
    {
        if(LoDropHides[position].getVisibility()==View.INVISIBLE)
            LoDropHides[position].setVisibility(View.VISIBLE);
        else
            LoDropHides[position].setVisibility(View.INVISIBLE);
    }
    public boolean isVisibie(int position )
    {
        boolean isVisibible = false;
        if(LoDropHides[position].getVisibility()==View.INVISIBLE)
        {
            isVisibible = true;
        }


        return isVisibible;
    }

}
