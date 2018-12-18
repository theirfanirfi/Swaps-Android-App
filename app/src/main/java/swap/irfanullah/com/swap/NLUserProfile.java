package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ProfilePagerAdapter;
import swap.irfanullah.com.swap.CustomComponents.PDialog;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Statistics;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class NLUserProfile extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfilePagerAdapter profilePagerAdapter;
    private ImageView profile_image;
    private TabLayout tabLayout;
    private Context context;
    private TextView profileDescription, statuses,swaps,followers;
    private String PROFILE_IMAGE = null, DESCRIPTION;
    private int USER_ID = 0;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeObjects();
        loadStats();
    }

    private void loadStats() {
        RetroLib.geApiService().getUserStats(USER_ID).enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if(response.isSuccessful()){
                    Statistics stat = response.body();
                        if(stat.getIS_EMPTY()){
                            finish();
                            //Toast.makeText(context,stat.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }else if(stat.getIS_FOUND()){
                            statuses.setText(Integer.toString(stat.getSTATUSES_COUNT()));
                            swaps.setText(Integer.toString(stat.getSWAPS_COUNT()));
                            followers.setText(Integer.toString(stat.getFOLLOWERS_COUNT()));
                            user = stat.getUSER();
                            PROFILE_IMAGE = user.getPROFILE_IMAGE();
                            loadProfilePicture();
                        }else {
finish();
                        }

                }else {
                    Toast.makeText(context,RMsg.REQ_ERROR_MESSAGE,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initializeObjects() {
        context = this;
        USER_ID=getIntent().getExtras().getInt("user_id");

        if(USER_ID == 0){
            finish();
        }

        viewPager = findViewById(R.id.profileViewPage);
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(),USER_ID);
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout = findViewById(R.id.profileTabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        statuses = findViewById(R.id.statusesProfileTextView);
        swaps = findViewById(R.id.swapsNoProfileTextView);
        followers = findViewById(R.id.followerNoProfileTextView);

        profile_image = findViewById(R.id.profile_image);

        profileDescription = findViewById(R.id.userProfileDescription);
        loadProfilePicture();

    }

   private void loadProfilePicture(){
       if(PROFILE_IMAGE == null) {
           profile_image.setImageResource(R.drawable.ic_person);
       } else {
           GLib.downloadImage(context,PROFILE_IMAGE).into(profile_image);
       }
    }

}
