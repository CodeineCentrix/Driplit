package com.example.s215131746.driplit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class ReportLeakClass extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_leak, container, false);
        ImageButton imgReport = rootView.findViewById(R.id.imageButton);
        bll b = new bll();
        imgReport.setImageBitmap(b.ScaleImg(R.drawable.tontsi,getResources()));
        imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Leak reported",Toast.LENGTH_LONG);
            }
        });
        return rootView;
    }

}