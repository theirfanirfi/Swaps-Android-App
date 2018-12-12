package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import javax.microedition.khronos.opengles.GL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ProfilePagerAdapter;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class UserProfile extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfilePagerAdapter profilePagerAdapter;
    private ImageView profile_image;
    private TabLayout tabLayout;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initializeObjects();
        changeProfilePic();


    }

    private void changeProfilePic() {
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Change Profile Picture")
                        .setMessage("Do you want to change profile picture?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Crop.pickImage(UserProfile.this);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    private void initializeObjects() {
        context = this;
        viewPager = findViewById(R.id.profileViewPage);
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout = findViewById(R.id.profileTabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        profile_image = findViewById(R.id.profile_image);

        GLib.downloadImage(context,PrefStorage.getUser(context).getPROFILE_IMAGE()).into(profile_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK){
            Uri source_uri = data.getData();
            Uri destination_uri = Uri.fromFile(new File(getCacheDir(),"swaps.png"));
            Crop.of(source_uri,destination_uri).withAspect(50,50).start(this);
            profile_image.setImageURI(Crop.getOutput(data));

        }else if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){
            profile_image.setImageURI(Crop.getOutput(data));
            Toast.makeText(context,"Profile picture is being updated.",Toast.LENGTH_LONG).show();
            Uri loc = Crop.getOutput(data);
            File file = new File(loc.getPath());
            RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"),PrefStorage.getUser(context).getTOKEN());
            RequestBody image = RequestBody.create(MediaType.parse("multipart/form-date"),file);
            MultipartBody.Part img = MultipartBody.Part.createFormData("image",file.getName(),image);

            RetroLib.geApiService().updateProfilePicture(tokenBody,img).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if(response.isSuccessful()){
                        ProfileModel profile = response.body();
                        if(profile.getIS_AUTHENTICATED()){
                            if(profile.getIS_EMPTY()){
                                Toast.makeText(context,profile.getMESSAGE(),Toast.LENGTH_LONG).show();
                            }else if(profile.getIS_ERROR()){
                                Toast.makeText(context,profile.getMESSAGE(),Toast.LENGTH_LONG).show();
                            }else if(profile.getIS_SAVED()){
                                User user = profile.getUSER();
                                Gson gson = new Gson();
                                String object = gson.toJson(user);
                                PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS,object).commit();
                                Toast.makeText(context,profile.getMESSAGE(),Toast.LENGTH_LONG).show();
                                Log.i("PROFILEUPDATED:",object);
                            }
                        }else {
                            Toast.makeText(context,"You are not loggedin. Login and try again.",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context,"Request was not successful",Toast.LENGTH_LONG).show();
                    }
                    Log.i("NOTPROFILE: ",response.raw().toString() + " : "+response.body().getMESSAGE());
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Log.i("NOTPROFILE: Exception ",t.toString());

                }
            });

        } else {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }
}
