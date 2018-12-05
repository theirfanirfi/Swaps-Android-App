package swap.irfanullah.com.swap;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    Button loginBtn;
    EditText email,password;
    String emailF = "", passwordF = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeObjects();
        RegisterationLink();
        LoginButton();

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
            }
        });
    }

    private void initializeObjects()
    {
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
    }
}
