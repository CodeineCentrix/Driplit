package com.example.s215131746.driplit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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







    private TextView tvTimesUsedORActual;
    LayoutInflater mInflater;

    public ItemListAdapter(Context c,int[] itemIcon,String[] itemName,String[] itemUsageAvg)
    {
        ItemIcon = itemIcon;
        ItemName =itemName ;
        ItemUsageAvg = itemUsageAvg;
        mInflater =(LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ItemName.length;
    }

    @Override
    public Object getItem(int position) {
        return ItemName[position];
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

        final EditText txtItemUsage = v.findViewById(R.id.txtItemUsage);
        txtItemUsage.setText(ItemUsageAvg[position]);

        //imgIcons = v.findViewById(R.id.imgItemIcon);
        //imgIcons.setImageResource(ItemIcon[position]);

        final TextView tvItemNames = v.findViewById(R.id.tvItemName);
        tvItemNames.setText(ItemName[position]);

        loDropHide = v.findViewById(R.id.loDropHide);
        if(loDropHide.getVisibility()== v.INVISIBLE)
            loDropHide.setVisibility(v.VISIBLE);
        else
            loDropHide.setVisibility(v.INVISIBLE);


     /*buttons____________________________________________________________________________*/
        final Button btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i =Integer.parseInt( tvQty.getText().toString()) + 1;
                tvQty.setText(""+i);
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
        final Button btnDone; btnDone = v.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*End buttons____________________________________________________________________________*/


        return v;
    }

    public void setVisible(View v)
    {

        if(loDropHide.getVisibility()== v.INVISIBLE)
            loDropHide.setVisibility(v.VISIBLE);
        else
            loDropHide.setVisibility(v.INVISIBLE);
    }
}
