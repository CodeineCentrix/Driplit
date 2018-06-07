package com.example.s215131746.driplit;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import viewmodels.GetTip;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class TipTrickClass extends Fragment {

    String person;
    Date date;
    String description;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tip_trick, container, false);

       TipModel tipDetails = new TipModel();
        ListView postedItems = rootView.findViewById(R.id.lvPostedTips);

        person =  "Shervin";
        //date =  tipDetails;
        description = tipDetails.TipDescription;

        TipListAdapter la = new TipListAdapter(this.getContext());
        postedItems.setAdapter(la);
        return rootView;
    }

}