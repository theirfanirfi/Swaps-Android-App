package swap.irfanullah.com.swap.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;

import static swap.irfanullah.com.swap.AppClasses.App.CHANNEL_ID;

public class StatusMediaService extends Service {
    private ArrayList<Media> media;
    private int NUMBER_OF_REQUESTS = 0;
    private int CURRENT_REQUEST = 0;
    private int TOTAL_REQUESTS = 0;
    private Context context;
    private final String _SERVICE_INTENT_STATUS_ID = "post_id";
    private int POST_ID = 0;
    private final static String NOTIFICATION_TITLE = "Swaps";
    private final static String NOTIFICATION_CONTENT = "Status attachments are being uploaded ..";
    private final static int NOTIFICATION_ID = 1023;
    private static int NOTIFICATION__ATTACHMENT_PROGRESS = 0;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder mBuilder;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        ArrayList<Media> media = intent.getParcelableArrayListExtra("uris");
        this.media= media;
        this.POST_ID = intent.getExtras().getInt(_SERVICE_INTENT_STATUS_ID);
        NUMBER_OF_REQUESTS = media.size();
        TOTAL_REQUESTS = media.size();
        new RequestQueue(this.POST_ID, media).execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RMsg.logHere("Service Destroyed.");
    }



    public class RequestQueue extends AsyncTask<Void, Void, Void> {

        private int statusid;
        private ArrayList<Media> media;
        private Handler handler;
        private Runnable runnable;


        public RequestQueue(int statusid,ArrayList<Media> media) {
            this.statusid = statusid;
            this.media = media;
            handler = new Handler();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showNotification();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            NUMBER_OF_REQUESTS--;
            if(NUMBER_OF_REQUESTS > 0){
                CURRENT_REQUEST++;
                delayNextRequest();
                RMsg.logHere(Integer.toString(CURRENT_REQUEST) + " of " + Integer.toString(TOTAL_REQUESTS)+ " is being uploaded.");
                showNotification();
            }else {
               // notificationManager.cancel(NOTIFICATION_ID);
                RMsg.logHere(Integer.toString(CURRENT_REQUEST+1) + " of " + Integer.toString(TOTAL_REQUESTS)+ " is being uploaded.");
                showNotification();
                CURRENT_REQUEST = 0;
                stopSelf();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Media m = this.media.get(CURRENT_REQUEST);
            if(m.getType() == 1){
                RMsg.logHere("upload Image");
            }else {
                RMsg.logHere("upload Video");
            }
            //Log.i(RMsg.LOG_MESSAGE,"IN running: "+Integer.toString(NUMBER_OF_REQUESTS));
            return null;
        }

        public void delayNextRequest(){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new RequestQueue(statusid,media).execute();
                }
            },10000);
        }

    }

    public void showNotification(){

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    // app small icon will be set when it is provided.
                    .setSmallIcon(R.drawable.ic_person)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(NOTIFICATION_CONTENT)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Progress: "+Integer.toString(NOTIFICATION__ATTACHMENT_PROGRESS)))
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
