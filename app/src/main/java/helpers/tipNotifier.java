package helpers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by s216127904 on 2018/08/31.
 */

public class tipNotifier extends Service {
    private static final String TAG = "Helpers.tipNotifier";

    public tipNotifier(){

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Fingers ");


        Runnable r = new Runnable() {
            @Override
            public void run() {
                for(int i =0; i<10;i++){
                    Log.i(TAG, "run: "+i);

                    try {
                        Thread.sleep(4000);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
