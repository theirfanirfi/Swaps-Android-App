package swap.irfanullah.com.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import swap.irfanullah.com.swap.Adapters.StatusFragGridAdapter;

public class ToBeDeletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_deleted);
//        ArrayList<Media> media = getIntent().getParcelableArrayListExtra("uris");
//        RMsg.logHere("NEW ACTIVITY: "+media.get(0).getUri().toString());
        GridView gv = findViewById(R.id.gridview);
        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(this);
        gv.setAdapter(statusFragGridAdapter);
    }
}
