package swap.irfanullah.com.swap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.Libraries.VolleyLib;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    Button loginBtn;
    EditText email,password;
    String emailF = "", passwordF = "";
    final String LOGIN_URL = "auth/login";
    final String TOKEN_URL = "auth/retoken";
    JSONObject userObject;
    ProgressBar progressBar;
    String str;
    Context context;
    private final String ENCODE_TAG = "ENCODING_ERROR";
    private final String JSON_TAG = "JSON_ERROR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkIsLoggedIn(this);

        initializeObjects();
        RegisterationLink();
        LoginButton();

    }

    private void checkIsLoggedIn(Context context) {
        if(PrefStorage.getUserData(context).equals(""))
        {
            //stay on the login activity
        }
        else
        {
            Intent homeAct = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeAct);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void RegisterationLink()
    {
        signup = findViewById(R.id.signupActLink);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void LoginButton()
    {
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailF = email.getText().toString().toLowerCase();
                passwordF = password.getText().toString().toLowerCase();
                if(passwordF.isEmpty() || emailF.isEmpty()) {
                    Snackbar.make(v, "None of the Fields can be empty.", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        loginRequest(emailF, passwordF);
                    } catch (JSONException e) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void loginRequest(String emailF, String passwordF) throws JSONException, UnsupportedEncodingException {

        JSONArray dataArray = new JSONArray();
        dataArray.put(0,emailF);
        dataArray.put(1,passwordF);
        str = VolleyLib.encode(dataArray.toString());

        VolleyLib.getRequest(this, LOGIN_URL+"/"+str, new VolleyLib.VolleyListener() {
            @Override
            public void onRecieve(JSONObject object) throws JSONException {

                Boolean isError = object.getBoolean("isError");
                Boolean isUser = object.getBoolean("isUser");
                Boolean tokenError = object.getBoolean("tokenError");
                String message = object.getString("message");

                if(isError)
                {
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                }
                else if(isUser)
                {
                    if(!tokenError) {
                        userObject = object.getJSONObject("user");

                        PrefStorage.getEditor(context).putString(PrefStorage.USER_PREF_DETAILS, userObject.toString()).commit();

                        Intent homeAct = new Intent(context, HomeActivity.class);
                        startActivity(homeAct);
                    }
                }

            }

            @Override
            public void onException(String exception) {
                Toast.makeText(context,exception,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(String volleyError) {
                Toast.makeText(context,volleyError,Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initializeObjects()
    {
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
        context = this;
    }
}
