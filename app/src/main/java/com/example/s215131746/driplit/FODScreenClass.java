package com.example.s215131746.driplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s216127904 on 2018/04/30.
 */

public class FODScreenClass extends Fragment {
    Intent mainMenu;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fodscreen, container, false);

        return rootView;
    }

}
