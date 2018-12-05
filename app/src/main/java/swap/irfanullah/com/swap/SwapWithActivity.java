package swap.irfanullah.com.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Adapters.SwapWithAdapter;
import swap.irfanullah.com.swap.Models.Status;


public class SwapWithActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView sWRV;
    private SwapWithAdapter swapWithAdapter;
    private ArrayList<Status> statuses, filteredArrayList;
    private EditText searchTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_with);
        toolbar = findViewById(R.id.swapWithToolbar);
       // toolbar.setTitle("");
        setSupportActionBar(toolbar);
        searchTextField = findViewById(R.id.searchSwapedWithField);



        sWRV = findViewById(R.id.swapWithRV);

        statuses = new ArrayList<>();

        Status st1 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st1);

        Status st2 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st2);
        Status st3 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st3);

        Status st4 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st4);

        Status st5 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st5);
        swapWithAdapter = new SwapWithAdapter(this,statuses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sWRV.setLayoutManager(layoutManager);
        sWRV.setAdapter(swapWithAdapter);


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
        for(Status item: statuses)
        {
            if(item.getUSERNAME().toLowerCase().contains(s.toLowerCase()))
            {
                filteredArrayList.add(item);
            }
        }

        swapWithAdapter.FilterRV(filteredArrayList);

    }
}
