package viewmodels;

/**
 * Created by s216127904 on 2018/07/26.
 */

public class Business {
    public String Logo;
    public String Name;
    public String About;
    public String Hours;
    public String Address;
    public String Email;

    @Override
    public String toString() {
        return Logo+","+Name+","+About+","+Hours+","+Address+","+Email;
    }
}
