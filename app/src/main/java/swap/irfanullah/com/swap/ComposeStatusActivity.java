package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Consumer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.GridImageAdapter;
import swap.irfanullah.com.swap.Libraries.Compressor;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ComposeStatusActivity extends AppCompatActivity {

    private Context context;
    private final int PICK_IMAGE_REQUEST_CODE = 1;
    private User user;
    private GridView gv;
    private GridImageAdapter gridImageAdapter;
    private List<Uri> uris;
    private ProgressBar progressBar;
    private final int IMAGE_ACTION = 1;
    private final int VIDEO_ACTION = 2;
    private final int OPEN_CAMERA_FOR_IMAGE = 0;
    private final int OPEN_FILES_DIR = 1;
    private final int OPEN_CAMERA_FOR_VIDEO = 0;
    private final int OPEN_FILES_DIR_VIDEO = 1;
    private final int REQUEST_IMAGE_CAPTURE = 111;
    String mCurrentPhotoPath;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_status);
        getSupportActionBar().setTitle("");
        initializeObjects();
    }

    private void initializeObjects() {
        context = this;
        user = PrefStorage.getUser(context);
        gv = findViewById(R.id.composeStatusGridView);
        uris = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarStatus);
        gridImageAdapter = new GridImageAdapter(context, uris);
        gv.setAdapter(gridImageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_status_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.camera:
                ChooseAction(IMAGE_ACTION);
                break;
            case R.id.video:
                ChooseAction(VIDEO_ACTION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void statusComposeRequest(String status)  {
        RetroLib.geApiService().composeStatus(PrefStorage.getUser(context).getTOKEN(),status).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getAuthenticated()) {
                        if (response.body().getPosted()) {
                           // progressBar.setVisibility(View.GONE);

                            Toast.makeText(context, response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, response.body().getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, response.body().getMESSAGE(), Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(context,response.body().getMESSAGE(),Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK){
            Uri source_uri = data.getData();
            //image will be compressed, added to @URIs list and @GridView
            // @adapter will be updated.
                new ImageCompressor(source_uri,context).execute(source_uri);
            //Uri destination_uri = Uri.fromFile(Compressor.getImagesCacheDir(context));
            Long tsLong = System.currentTimeMillis()/1000;
            String file_name = Integer.toString(RMsg.getRandom())+ user.getFULL_NAME()+tsLong.toString()+"pick";
            //Uri destination_uri = Uri.fromFile(new File(getCacheDir(),file_name));

            RMsg.logHere(source_uri.toString());

        }else if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){

        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //@bitmap uri will be obtained here.
          Uri uri = getImageUri(context,bitmap);
            //image will be compressed, added to @URIs list and @GridView
            // @adapter will be updated.
          new ImageCompressor(uri,context).execute(uri);
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
           // mVideoView.setVideoURI(videoUri);
        }

        else {
            finish();
        }
    }

    private void ChooseAction(int ACTION){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        //if image action was initiated
        if(ACTION == IMAGE_ACTION) {
            final String[] action = {"Take Photo", "Choose from files"};
            builder.setItems(action, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == OPEN_CAMERA_FOR_IMAGE) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }else {
                        Crop.pickImage(ComposeStatusActivity.this);
                    }

                }
            });
        }
        //else, if the action was of video
        else {
            final String[] action = {"Record Video", "Choose from files"};
            builder.setItems(action, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == OPEN_CAMERA_FOR_VIDEO) {
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }else {
                        RMsg.logHere(action[which]);
                    }
                }
            });
        }
        builder.create().show();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public class ImageCompressor extends AsyncTask<Uri,Integer,byte[]> {
        private Uri uri;
        private Context context;

        public ImageCompressor(Uri uri,Context context) {
            this.uri = uri;
            this.context = context;
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            byte[] bytes = null;
            try {
                Bitmap bitmap = Compressor.uri2bitmap(context,params[0]);
                bytes = getBytesFromBitmap(bitmap,80);
            } catch (IOException e) {
                RMsg.logHere(e.getMessage());
            }

            return bytes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            if(bytes != null){
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Uri uri = getImageUri(context,compressedBitmap);
                uris.add(uri);
                gridImageAdapter.notifyAdapter(uris);
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }


}
