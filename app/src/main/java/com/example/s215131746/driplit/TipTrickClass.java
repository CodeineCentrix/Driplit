package com.example.s215131746.driplit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class TipTrickClass extends Fragment {
    String [] tips = {"","","","",""};
    ArrayList<String> tip = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tip_trick, container, false);

        tip.add("the first trick");

        final EditText txtTipTrick = rootView.findViewById(R.id.txtTipTrick);
        final ListView lvPostedTips = rootView.findViewById(R.id.lvPostedTips);
        lvPostedTips.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,tip));

        Button btnPost = rootView.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip.add( txtTipTrick.getText().toString());


                txtTipTrick.setText("Enter New tip");
                Toast.makeText(getContext(),"Post will show in 24hr",Toast.LENGTH_LONG).show();

                lvPostedTips.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,tip));

            }
        });
        return rootView;
    }

}