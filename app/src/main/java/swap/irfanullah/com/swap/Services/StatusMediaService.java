package swap.irfanullah.com.swap.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.RealPathUtils;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

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
    private User user;
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
        //user = PrefStorage.getUser(this).getUSER();
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
                Uri uri = m.getUri();
                String path = getRealPathFromURIPath(uri,context);
                File file = new File(path);
                imageUploadRequest(file);
                RMsg.logHere("upload Image");
            }else {
                Uri uri = m.getUri();
                String path = getRealPathFromURIPath(uri,context);

                File file = new File(path);
                VideoUploadRequest(file);
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


    private String getRealPathFromURIPath(Uri contentURI, Context activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public void VideoUploadRequest(File file){

        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"),"$2y$10$G0RWLNAiFq/fwZe0sUA7.uqLYoHwhV0hmkcdmk87VmpIzB5QF7IWK");
        RequestBody attachment_type = RequestBody.create(MediaType.parse("multipart/form-data"),"2");
        RequestBody vid = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part video = MultipartBody.Part.createFormData("video",file.getName(),vid);
        RequestBody status_id = RequestBody.create(MediaType.parse("multipart/form-data"),Integer.toString(POST_ID));

        RetroLib.geApiService().sendAttachments(tokenBody,video,attachment_type,status_id).enqueue(new Callback<Attachments>() {
            @Override
            public void onResponse(Call<Attachments> call, Response<Attachments> response) {
                if(response.isSuccessful()){
                    Attachments med = response.body();
                    if(med.getIS_ERROR()){
                        //if there was anykind of error. Roll back the status
                        statusRollBack();
                        RMsg.toastHere(context,med.getMESSAGE());
                    }else {
                        if(med.getIS_AUTHENTICATED()){
                            if(med.getIS_SAVED()){
                                RMsg.toastHere(context,med.getMESSAGE());
                            }else {
                                statusRollBack();
                                RMsg.toastHere(context,med.getMESSAGE());
                            }
                        }else {
                            statusRollBack();
                            RMsg.toastHere(context,med.getMESSAGE());
                        }
                    }
                }else {
                    statusRollBack();
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }

                //debug
                RMsg.logHere(response.raw().toString());
                RMsg.logHere(response.body().getMESSAGE());
            }

            @Override
            public void onFailure(Call<Attachments> call, Throwable t) {
                RMsg.logHere(t.toString());
            }
        });
    }

    public void imageUploadRequest(File file){

        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"),"$2y$10$G0RWLNAiFq/fwZe0sUA7.uqLYoHwhV0hmkcdmk87VmpIzB5QF7IWK");
        RequestBody attachment_type = RequestBody.create(MediaType.parse("multipart/form-data"),"1");
        RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image",file.getName(),img);
        RequestBody status_id = RequestBody.create(MediaType.parse("multipart/form-data"),Integer.toString(POST_ID));

        RetroLib.geApiService().sendAttachments(tokenBody,image,attachment_type,status_id).enqueue(new Callback<Attachments>() {
            @Override
            public void onResponse(Call<Attachments> call, Response<Attachments> response) {

                if(response.isSuccessful()){
                    Attachments med = response.body();
                    if(med.getIS_ERROR()){
                        //if there was anykind of error. Roll back the status
                        statusRollBack();
                        RMsg.toastHere(context,med.getMESSAGE());
                    }else {
                        if(med.getIS_AUTHENTICATED()){
                            if(med.getIS_SAVED()){
                                RMsg.toastHere(context,med.getMESSAGE());
                            }else {
                                statusRollBack();
                                RMsg.toastHere(context,med.getMESSAGE());
                            }
                        }else {
                            statusRollBack();
                            RMsg.toastHere(context,med.getMESSAGE());
                        }
                    }
                }else {
                    statusRollBack();
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }

                //debug logs
                RMsg.logHere(response.raw().toString());
                RMsg.logHere(response.body().getMESSAGE());
            }

            @Override
            public void onFailure(Call<Attachments> call, Throwable t) {
                statusRollBack();
                RMsg.logHere(t.toString());
            }
        });
    }

    private void statusRollBack() {
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
