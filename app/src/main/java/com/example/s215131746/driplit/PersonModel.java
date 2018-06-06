package com.example.s215131746.driplit;

/**
 * Created by s216127904 on 2018/06/01.
 */

public class PersonModel {
    public int id;
    public String fullName;
    public String email;
    public String userPassword;
    public String phoneNumber;

    @Override
    public String toString() {
        return fullName+","+email+","+userPassword+","+phoneNumber;
    }
}
