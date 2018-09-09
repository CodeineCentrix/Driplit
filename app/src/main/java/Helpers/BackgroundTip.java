package Helpers;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.s215131746.driplit.DBAccess;
import com.example.s215131746.driplit.GeneralMethods;
import com.example.s215131746.driplit.Login;
import com.example.s215131746.driplit.R;

import java.util.ArrayList;

import viewmodels.PersonModel;
import viewmodels.TipModel;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackgroundTip extends IntentService {
    private static final String TAG = "BackgroundTip";
    public BackgroundTip() {
        super("BackgroundTip");


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Fingers ");


        Runnable r = new Runnable() {
            @Override
            public void run() {
                for(int i =0; i<100;i++){
                    Log.i(TAG, "run: "+i);
                    Notification();
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
    public void Notification() {
        GeneralMethods m = new GeneralMethods(getApplicationContext());
        DBAccess business = new DBAccess();
        ArrayList<TipModel> tips;
        final String[] personDetails = m.Read("person.txt", ",");

            String Title ="";
            StringBuilder text= new StringBuilder();
            if(personDetails[PersonModel.ISAMDIN].equals("true")){
                tips = business.GetAdminTips();
            }else {
                tips = business.GetTips();
            }


            int adminNewTips = 0,id=0;
            int old =0,personNewTips =0;
            try {
                 old = Integer.parseInt(personDetails[PersonModel.OLDAPPROVED]);
            }catch (Exception e){
                e.printStackTrace();
            }
            for (int z = 0; z < tips.size(); z++) {

                if (!tips.get(z).Readed && m.Read("person.txt",",")[PersonModel.ISAMDIN].equals("true")) {
                    adminNewTips++;
                }else if( m.Read("person.txt",",")[PersonModel.FULLNAME].equals(tips.get(z).FullName)){
                    personNewTips++;
                    Title = "Approved Tip(s) :"+(personNewTips-old);
                    text.append(tips.get(z).TipDescription).append("\n");
                }
            }

            if(personDetails[PersonModel.ISAMDIN].equals("true")){
                Title ="New Tip";
                text = new StringBuilder("You have " + adminNewTips + " unread tips");
            }
            if (adminNewTips > 0 || personNewTips > old ) {
                PersonModel person = new PersonModel();
                person.userPassword = personDetails[PersonModel.PASSWORD];
                person.email = personDetails[PersonModel.EMAIL];
                person.Usagetarget = Integer.parseInt(personDetails[PersonModel.USAGETARGET]);
                person.getOldapproved = Integer.parseInt(personDetails[PersonModel.OLDAPPROVED]);
                person = business.LoginPerson(person);
                person.getOldapproved = personNewTips;
                m.writeToFile(person.toString(),"person.txt");

                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.drawable.logohead)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(Title)
                        .setContentText(text.toString())
                        .setStyle(new NotificationCompat.BigTextStyle()
                         .bigText(text.toString()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(adminNewTips+id, mBuilder.build());
            }


    }


}
