package swap.irfanullah.com.swap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import swap.irfanullah.com.swap.Adapters.ChatAdapter;
import swap.irfanullah.com.swap.Models.RMsg;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ChatAdapter chatAdapter;
    private Context context;
    private final String LOGGEDIN_USER_INTENT_KEY = "loggedin_user_id";
    private final String TO_CHAT_WITH_USER_INTENT_KEY = "to_chat_with_user_id";
    private int LOGGED_IN_USER = 0;
    private int CHAT_WITH_USER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeObjects();
        getExtras();
    }

    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            LOGGED_IN_USER = bundle.getInt(LOGGEDIN_USER_INTENT_KEY);
            CHAT_WITH_USER = bundle.getInt(TO_CHAT_WITH_USER_INTENT_KEY);
        }else {
            finish();
        }

    }

    private void initializeObjects() {
        context = this;
        rv = findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(chatAdapter);
    }


}
