package swap.irfanullah.com.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.SwapWithAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Storage.PrefStorage;


public class SwapWithActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView sWRV;
    private SwapWithAdapter swapWithAdapter;
    private ArrayList<Followers> followersList, filteredArrayList;
    private EditText searchTextField;
    private int INTENT_STATUS_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_with);
        toolbar = findViewById(R.id.swapWithToolbar);
       // toolbar.setTitle("");
        setSupportActionBar(toolbar);
        searchTextField = findViewById(R.id.searchSwapedWithField);

        INTENT_STATUS_ID = getIntent().getExtras().getInt("status_id");


        sWRV = findViewById(R.id.swapWithRV);

        RetroLib.geApiService().getFollowers(PrefStorage.getUser(this).getTOKEN(),INTENT_STATUS_ID).enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                Followers followers = response.body();
                ArrayList<Followers> followersArrayList = followers.getFollowers();
                followersList = followers.getFollowers();
                int status_id = followers.getSTATUS_ID();
                ArrayList<Swap> swaps = followers.getSWAPS();

                swapWithAdapter = new SwapWithAdapter(getApplicationContext(),followersArrayList,swaps,status_id);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                sWRV.setLayoutManager(layoutManager);
                sWRV.setAdapter(swapWithAdapter);

                Log.i("FOLLOWERS: ",response.raw().toString());
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.i("FOLLOWERS: ",t.toString());

            }
        });


        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getApplicationContext(),s.toString(),Toast.LENGTH_LONG).show();
                filter(s.toString());
            }
        });



    }

    private void filter(String s) {
        filteredArrayList = new ArrayList<>();
        for(Followers item: followersList)
        {
            if(item.getUSERNAME().toLowerCase().contains(s.toLowerCase()))
            {
                filteredArrayList.add(item);
            }
        }

        swapWithAdapter.FilterRV(filteredArrayList);

    }
}
