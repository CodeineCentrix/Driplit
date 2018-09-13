package com.example.s215131746.driplit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.TipListAdapter;
import viewmodels.PersonModel;
import viewmodels.TipModel;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class TipTrickClass extends Fragment {
    TipListAdapter la;
    DBAccess business;
    EditText txtTip;
    GeneralMethods m;
    ArrayList<TipModel> tips;
    ListView postedItems;
    Handler h = new Handler();
     String [] fuck ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tip_trick, container, false);
        postedItems = rootView.findViewById(R.id.lvPostedTips);
        m = new GeneralMethods(getContext());
        business = new DBAccess();
        fuck = m.Read(this.getString(R.string.person_file_name),",");

        helpThread helpThread = new helpThread(true, rootView.getContext());
        new Thread(helpThread).start();

        txtTip = rootView.findViewById(R.id.txtTipTrick);
        final ImageButton btnPost = rootView.findViewById(R.id.imgbtnPost);
        txtTip.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.length() != 0)
                     btnPost.setClickable(true);
                 else
                     btnPost.setClickable(false);
             }

             @Override
             public void afterTextChanged(Editable s) {
             }
         });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipd = txtTip.getText().toString();
                if(tipd.length()<10){
                    Toast.makeText(rootView.getContext(),"Tip not descriptive enough",Toast.LENGTH_LONG).show();
                }else if(!tipd.equals("")){
                    Toast.makeText(rootView.getContext(),"your tip will wait for approval",Toast.LENGTH_LONG).show();
                    TipModel tip = new TipModel();
                    tip.PersonID =Integer.parseInt(
                            m.Read(rootView.getContext().getString(R.string.person_file_name)
                                    ,",")[PersonModel.ID]);
                    tip.TipDescription = tipd;
                    business.MobAddTip(tip);
                    ArrayList<TipModel> tips =  business.GetTips();
                    TipListAdapter la = new TipListAdapter(getContext(),tips);
                    postedItems.setAdapter(la);
                    txtTip.setText("");
                }
            }
        });
        ListView lvItemList = rootView.findViewById(R.id.lvPostedTips);

        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //after an item has been clicked the the following line will either make the bottom controllers visible or invisible
                if(fuck[PersonModel.ISAMDIN].equals("true")) {
                    la.approveTip(view,i);
                }
            }
        });
        return rootView;
    }
    class helpThread implements Runnable {
        Context context;
        boolean onCreate;
        boolean isConnecting;
        Snackbar mySnackbar;
        public helpThread() {

        }

        public helpThread(boolean onCreate,Context context) {
            this.onCreate = onCreate;
            this.context = context;
        }


        @Override
        public void run() {
            if(onCreate){

                isConnecting = business.isConnecting();
                if(isConnecting) {

                    if(fuck[PersonModel.ISAMDIN].equals("true"))
                        tips =  business.GetAdminTips();
                    else
                        tips = business.GetTips();
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            la = new TipListAdapter(getContext(),tips);
                            postedItems.setAdapter(la);
                        }
                    });
                }else {
                    mySnackbar = Snackbar.make(txtTip,"No Connection", 8000);
                    mySnackbar.getView().setBackgroundColor(Color.RED);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            mySnackbar.show();

                        }
                    });

                }
            }else {

            }
        }
    }
}