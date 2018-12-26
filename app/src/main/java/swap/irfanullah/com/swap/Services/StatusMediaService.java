package swap.irfanullah.com.swap.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;

public class StatusMediaService extends Service {
    private ArrayList<Media> media;
    private int NUMBER_OF_REQUESTS = 5;
    private Context context;
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
        ArrayList<Media> media = intent.getParcelableArrayListExtra("uris");
        this.media= media;
        new RequestQueue(1, media).execute();
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


        public RequestQueue(int statusid,ArrayList<Media> media) {
            this.statusid = statusid;
            this.media = media;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            NUMBER_OF_REQUESTS--;
            if(NUMBER_OF_REQUESTS > 0){
                new RequestQueue(this.statusid,media).execute();
            }else {
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
            Log.i(RMsg.LOG_MESSAGE,"IN running: "+Integer.toString(NUMBER_OF_REQUESTS));
            return null;
        }
    }

}
