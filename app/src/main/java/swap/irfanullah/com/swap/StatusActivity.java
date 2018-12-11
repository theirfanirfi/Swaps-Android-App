package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private int STATUS_ID;
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

    }



    private void getStatusID() {
        this.STATUS_ID = getIntent().getExtras().getInt("status_id");
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

        raters = new ArrayList<>();
        singleStatusAdapter = new SingleStatusAdapter(context,raters);
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

                    if(singleStatusModel.getIS_AUTHENTICATED()){
                        if(singleStatusModel.getIS_EMPTY()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        } else if(singleStatusModel.getIS_ERROR()){
                            Toast.makeText(context, singleStatusModel.getMESSAGE(), Toast.LENGTH_LONG).show();
                        } else if(singleStatusModel.getIS_FOUND()){
                            ratingBar.setRating(singleStatusModel.getAVERAGE_RATING());
                            avg_rating.setText("AVG Rating: "+Float.toString(singleStatusModel.getAVERAGE_RATING()));
                            swaps_count.setText("Swaps: "+Integer.toString(singleStatusModel.getSWAPS_COUNT()));

                            raters = singleStatusModel.getRATERS();
                            singleStatusAdapter = new SingleStatusAdapter(context,raters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(singleStatusAdapter);
                            //singleStatusAdapter.notifyDataSetChanged();
                        } else {
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
}
