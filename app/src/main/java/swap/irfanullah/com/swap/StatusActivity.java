package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SingleStatusAdapter;
import swap.irfanullah.com.swap.Adapters.StatusAdapter;
import swap.irfanullah.com.swap.Adapters.StatusFragGridAdapter;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.SingleStatusModel;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class StatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView fullname,statusTextView,status_date,avg_rating,swaps_count;
    private ImageView profile_image,trash;
    private RatingBar ratingBar;
    private SingleStatusAdapter singleStatusAdapter;
    private int STATUS_ID, ADAP_POSITION, SWAP_ID, USER_ID;
    private Context context;
    private Status status;
    private SingleStatusModel singleStatusModel;
    private ArrayList<User> raters;
    private static final String ACTION_BAR_TITLE = "Status";
    private int IS_ACCEPTED = 0;
    private Boolean isMe= false;
    private RecyclerView statusMedia;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Attachments> mediaAttachments;
    private StatusFragGridAdapter statusFragGridAdapter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        getSupportActionBar().setTitle(ACTION_BAR_TITLE);

        initializeObjects();
        getStatusID();
        makeRequest(this);
        deleteStatus();
        rateStatus();
        gotoProfile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.i(RMsg.LOG_MESSAGE,Boolean.toString(isMe));
        if(this.IS_ACCEPTED == 0 && isMe.equals(false)){
            getMenuInflater().inflate(R.menu.status_action_menu,menu);
            return true;
        }else {
            menu.removeItem(R.id.accept);
            menu.removeItem(R.id.decline);
            //getMenuInflater().inflate(R.menu.status_action_menu,menu);
            return false;
        }
        //return super.onCreateOptionsMenu(menu);
    }

    private void rateStatus() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    RetroLib.geApiService().rateStatus(PrefStorage.getUser(context).getTOKEN(), STATUS_ID, rating).enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {

                            if (response.isSuccessful()) {

                                Status status = response.body();
                                if (status.getAuthenticated()) {
                                    if (status.getIS_EMPTY()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    } else if (status.getIS_RATED()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        makeRequest(context);
                                    } else if (status.getIS_ALREADY_RATED()) {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                        makeRequest(context);
                                    } else {
                                        Toast.makeText(context, status.getMESSAGE(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "You are not logged in. Please login and try again.", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(context, "Request was unsuccessful.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });
    }

    private void deleteStatus() {
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Status");
                builder.setMessage("Are you sure to delete the status?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(context,Integer.toString(status.getSTATUS_ID()),Toast.LENGTH_LONG).show();
                    RetroLib.geApiService().deleteStatus(PrefStorage.getUser(context).getTOKEN(),status.getSTATUS_ID()).enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if(response.isSuccessful()){
                                Status st = response.body();
                                if(st.getAuthenticated()){
                                    if(st.getIS_EMPTY()){
                                        Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                    }else if(st.getFound()){
                                        if(st.getIS_DELETED()){
                                            Intent sf = new Intent(context,HomeActivity.class);
                                            sf.putExtra("delete_position",ADAP_POSITION);
                                            startActivity(sf);
                                            //finish();
                                            Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(context,st.getMESSAGE(),Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(context,"You are not logged in. Please login and try again.",Toast.LENGTH_LONG).show();

                                }
                            }else {
                                Toast.makeText(context,"The request to delete the status was not successful.",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });
    }


    private void getStatusID() {
        Bundle bundle = getIntent().getExtras();
        this.STATUS_ID = bundle.getInt("status_id");
        this.ADAP_POSITION = bundle.getInt("position");
        this.IS_ACCEPTED = bundle.getInt("is_accepted");
        this.SWAP_ID = bundle.getInt("swap_id");
        Log.i(RMsg.LOG_MESSAGE,"IS ACCEPTED: "+Integer.toString(IS_ACCEPTED));
        //menu.setGroupVisible(R.id.action_group,false);
        //onCreateOptionsMenu(menu);
    }


    public void initializeObjects(){

        recyclerView = findViewById(R.id.singleStatusRatersRV);
        fullname = findViewById(R.id.fullNameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        status_date = findViewById(R.id.status_posted_date);
        avg_rating = findViewById(R.id.avg_ratingTextView);
        swaps_count = findViewById(R.id.totalSwaps);
        profile_image = findViewById(R.id.profile_image);
        trash = findViewById(R.id.deleteStatusIcon);
        ratingBar = findViewById(R.id.ratingBar);
        statusMedia = findViewById(R.id.gridViewStatus);
        context = this;
        status = new Status();

        raters = new ArrayList<>();
        singleStatusAdapter = new SingleStatusAdapter(context,raters, status);
        RecyclerView.LayoutManager layoutManagerr = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManagerr);
        recyclerView.setAdapter(singleStatusAdapter);

        mediaAttachments = new ArrayList<>();
        statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments,this.STATUS_ID);
        layoutManager = new GridLayoutManager(context,4);
        //statusMedia.setHasFixedSize(true);
        statusMedia.setAdapter(statusFragGridAdapter);
        statusMedia.setLayoutManager(layoutManager);

    }

    private void makeRequest(final Context context) {
        RetroLib.geApiService().getRaters(PrefStorage.getUser(this).getTOKEN(),this.STATUS_ID).enqueue(new Callback<SingleStatusModel>() {
            @Override
            public void onResponse(Call<SingleStatusModel> call, Response<SingleStatusModel> response) {
                if(response.isSuccessful()){
                    singleStatusModel = response.body();
                    status = singleStatusModel.getSTATUS();

                    if(status.getHAS_ATTACHMENTS() == 1){
                        loadStatusMedia(status.getATTACHMENTS());
                    }


                    fullname.setText(status.getName());
                    statusTextView.setText(status.getSTATUS());
                    status_date.setText(status.getTIME());
                    USER_ID= status.getUSER_ID();
                    if(status.getPROFILE_IMAGE() == null ){
                        profile_image.setImageResource(R.drawable.ic_person);
                    }else {
                        GLib.downloadImage(context,status.getPROFILE_IMAGE()).into(profile_image);
                    }

                    if(status.getUSER_ID() == PrefStorage.getUser(context).getUSER_ID()){
                        trash.setVisibility(View.VISIBLE);
                        isMe = true;
                        onCreateOptionsMenu(menu);
                    }else {
                        onCreateOptionsMenu(menu);
                        isMe = false;
                    }

                    if(singleStatusModel.getIS_AUTHENTICATED()){
                        if(singleStatusModel.getIS_EMPTY()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                            finish();
                        } else if(singleStatusModel.getIS_ERROR()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        } else if(singleStatusModel.getIS_FOUND()){

                            ratingBar.setRating(singleStatusModel.getAVERAGE_RATING());
                            avg_rating.setText("AVG Rating: "+Float.toString(singleStatusModel.getAVERAGE_RATING()));
                            swaps_count.setText("Swaps: "+Integer.toString(singleStatusModel.getSWAPS_COUNT()));

                            raters = singleStatusModel.getRATERS();

                            singleStatusAdapter = new SingleStatusAdapter(context,raters, status);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(singleStatusAdapter);

                        } else {
                           // finish();
                            ratingBar.setRating(singleStatusModel.getAVERAGE_RATING());
                            avg_rating.setText("AVG Rating: "+Float.toString(singleStatusModel.getAVERAGE_RATING()));
                            swaps_count.setText("Swaps: "+Integer.toString(singleStatusModel.getSWAPS_COUNT()));
                           // Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, RMsg.AUTH_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(context, RMsg.REQ_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SingleStatusModel> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void notifyAdapter(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.accept:
                Log.i(RMsg.LOG_MESSAGE,"accepted");
                approve();
                break;
            case R.id.decline:
                Log.i(RMsg.LOG_MESSAGE,"declined");
                menu.setGroupVisible(R.id.action_group,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void approve(){
        RetroLib.geApiService().approveSwap(PrefStorage.getUser(context).getTOKEN(),this.SWAP_ID).enqueue(new Callback<Swap>() {
            @Override
            public void onResponse(Call<Swap> call, Response<Swap> response) {
                if(response.isSuccessful()){
                    Swap swap = response.body();
                    if(swap.getAuthenticated()){
                        if(swap.getError()){
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else if(swap.getApproved()){
                            menu.setGroupVisible(R.id.action_group,false);
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,swap.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(context,RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.toastHere(context,RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Swap> call, Throwable t) {
                RMsg.toastHere(context,t.toString());
            }
        });
    }

    public void gotoProfile(){
        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefStorage.isMe(context,USER_ID)){
                    Intent profileAct = new Intent(context,UserProfile.class);
                   context.startActivity(profileAct);
                }else {
                    Intent profileAct = new Intent(context,NLUserProfile.class);
                    profileAct.putExtra("user_id",USER_ID);
                    context.startActivity(profileAct);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefStorage.isMe(context,USER_ID)){
                    Intent profileAct = new Intent(context,UserProfile.class);

                    context.startActivity(profileAct);
                }else {
                    Intent profileAct = new Intent(context,NLUserProfile.class);
                    profileAct.putExtra("user_id",USER_ID);
                    context.startActivity(profileAct);
                }
            }
        });
    }

    private void loadStatusMedia(String attachments){

        // viewHolder.mediaProgressBar.setVisibility(View.VISIBLE);

//        viewHolder.mediaProgressBar.setVisibility(View.GONE);
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);
        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            statusFragGridAdapter.notifyAdapter(mediaAttachments);
           // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            statusFragGridAdapter.notifyAdapter(arrayList);

            RMsg.logHere("working");
        }
    }
}
