package viewmodels;

import android.app.Application;

import java.net.PortUnreachableException;

/**
 * Created by s216127904 on 2018/06/01.
 */

public class PersonModel extends Application {
    public static final int ID = 0;
    public static final int FULLNAME=1;
    public static final int EMAIL=2;
    public static final int PASSWORD=3;
    public static final int ISAMDIN=4;
    public static final int USAGETARGET=5;
    public static final int OLDAPPROVED =6;

    public int id;
    public String fullName;
    public String email;
    public String userPassword;
    public boolean isAdmin;
    public int Usagetarget ;
    public int getOldapproved;

    @Override
    public String toString() {
        return id+","+fullName+","+email+","+userPassword+","+isAdmin +","+Usagetarget+","+getOldapproved;
    }
}
