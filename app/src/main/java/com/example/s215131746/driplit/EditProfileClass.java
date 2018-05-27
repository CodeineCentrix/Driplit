package com.example.s215131746.driplit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by s216127904 on 2018/05/01.
 */

public class EditProfileClass extends Fragment {
    bll business;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edit_profile, container, false);


        EditText txtFullname = rootView.findViewById(R.id.txtUsername);
        EditText txtEmail = rootView.findViewById(R.id.txtUserEmail);
        EditText txtPhoneNumber =rootView.findViewById(R.id.txtPhoneNumber);
        EditText txtPassword = rootView.findViewById(R.id.txtPassword);
        if (business==null)
            business = new bll();
        Bundle tab = savedInstanceState.getBundle("emailAddress");
        String emailAddress = tab.getString("emailAddress");
        String[] person = business.Person(emailAddress);


        txtFullname.setText(person[0]);
        txtEmail.setText(person[1]);
        txtPhoneNumber.setText(person[2]);
        txtPassword.setText(person[3]);
        return rootView;
    }

}
