package swap.irfanullah.com.swap;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    Button regBtn;
    EditText email,password;
    String emailF = "", passwordF = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeCbjects();
        LoginLink();
        RegisterButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void RegisterButton()
    {
        regBtn = findViewById(R.id.loginBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
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

    private void LoginLink()
    {
        loginLink = findViewById(R.id.loginActLink);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void initializeCbjects()
    {
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
    }
}
