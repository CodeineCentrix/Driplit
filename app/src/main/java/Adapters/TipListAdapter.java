package Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215131746.driplit.DBAccess;
import com.example.s215131746.driplit.GeneralMethods;
import com.example.s215131746.driplit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import viewmodels.TipModel;

/**
 * Created by s216127904 on 2018/06/06.
 */

public class TipListAdapter extends BaseAdapter {
    ArrayList<TipModel> tipsList;
    private LayoutInflater mInflater;
    private Context context;
    View approve[];
    public TipListAdapter(Context context,ArrayList<TipModel> tipsList){
        this.context = context;
        this.tipsList = tipsList;
        approve = new View[tipsList.size()];
        mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return tipsList.size();
    }
    @Override
    public Object getItem(int position) {
        return tipsList.get(position).TipDescription;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.tiptrick_layout,null);
        TextView tipDescription = v.findViewById(R.id.tvDescription),
        tvPerson = v.findViewById(R.id.tvPostName),
        tvDate = v.findViewById(R.id.tvDate);
        approve[position] = v.findViewById(R.id.view);
        if(!tipsList.get(position).PersonName.equals( "Adim")){
            approve[position].setVisibility(View.GONE);
        }else if(tipsList.get(position).Approved==false){
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.red));
        } else if(tipsList.get(position).Approved==true) {
            approve[position].setBackgroundColor(v.getResources().getColor(R.color.green));
        }
        SimpleDateFormat df = new SimpleDateFormat("MMM dd");
        tipDescription.setText(tipsList.get(position).TipDescription);
        tvPerson.setText("By: "+tipsList.get(position).FullName);
        tvDate.setText(df.format( tipsList.get(position).DatePosted));
//        LinearLayout llTipContainer = v.findViewById(R.id.llTipContainer);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0,16,8,4);
//        llTipContainer.setLayoutParams(lp);

         return v;
}
    public boolean approveTip(int position){
        if(tipsList.get(position).Approved==true){
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.red));
            return tipsList.get(position).Approved=false;
        }else{
            approve[position].setBackgroundColor(context.getResources().getColor(R.color.green));
            return tipsList.get(position).Approved=true;
        }
    }
    public void approveTip(View v, final int position ){
        final GeneralMethods m = new GeneralMethods(v.getContext());
        m.writeToFile("do","approve.txt");
        final int duration = 3000;
        approveTip(position);
        final DBAccess business = new DBAccess();
        Handler h = new Handler();
        Snackbar mySnackbar = Snackbar.make(v,"Tip Status Changed", duration);
        mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.writeToFile("undo","approve.txt");
                approveTip(position);
            }


        });
        mySnackbar.show();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(m.readFromFile("approve.txt").equals("do")){
                    business.MobApproveTip(tipsList.get(position));
                }

            }
            //1 second wait before saving to the database
        },duration);

    }

}
