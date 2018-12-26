package swap.irfanullah.com.swap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Media;
import swap.irfanullah.com.swap.Models.RMsg;

public class ToBeDeletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_deleted);
        ArrayList<Media> media = getIntent().getParcelableArrayListExtra("uris");
        RMsg.logHere("NEW ACTIVITY: "+media.get(0).getUri().toString());
    }
}
