package com.example.s215131746.driplit;

import android.app.Application;

/**
 * Created by s216127904 on 2018/08/23.
 */

public class Person extends Application {
    public int id;
    public String fullName;
    public String email;
    public String userPassword;
    public boolean isAdmin;

    @Override
    public String toString() {
        return fullName+","+email+","+userPassword+","+isAdmin;
    }
    public void Meme()
    {

    }
}
