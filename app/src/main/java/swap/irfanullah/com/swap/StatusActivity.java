package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SingleStatusAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.SingleStatusModel;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class StatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView fullname,statusTextView,status_date,avg_rating,swaps_count;
    private ImageView profile_image,trash;
    private RatingBar ratingBar;
    private SingleStatusAdapter singleStatusAdapter;
    private int STATUS_ID, ADAP_POSITION;
    private Context context;
    private Status status;
    private SingleStatusModel singleStatusModel;
    private ArrayList<User> raters;
    private static final String ACTION_BAR_TITLE = "Status";

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
        context = this;
        status = new Status();

        raters = new ArrayList<>();
        singleStatusAdapter = new SingleStatusAdapter(context,raters, status);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(singleStatusAdapter);
    }

    private void makeRequest(final Context context) {
        RetroLib.geApiService().getRaters(PrefStorage.getUser(this).getTOKEN(),this.STATUS_ID).enqueue(new Callback<SingleStatusModel>() {
            @Override
            public void onResponse(Call<SingleStatusModel> call, Response<SingleStatusModel> response) {
                if(response.isSuccessful()){
                    singleStatusModel = response.body();
                    status = singleStatusModel.getSTATUS();

                    fullname.setText(status.getName());
                    statusTextView.setText(status.getSTATUS());
                    status_date.setText(status.getTIME());
                    if(status.getUSER_ID() == PrefStorage.getUser(context).getUSER_ID()){
                        trash.setVisibility(View.VISIBLE);
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
                            finish();
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "You are not logged in. Please login and try again.", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(context, "Request was not successful.", Toast.LENGTH_LONG).show();

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
}
