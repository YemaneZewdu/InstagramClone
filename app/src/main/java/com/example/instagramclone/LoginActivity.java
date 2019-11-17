package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button   btnLogin;
    private Button   btnSignup;
    public static final String TAG = "LoginActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username,password);
                
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                try {
                    signup (username,password);
                } catch (ParseException e) {
                    Log.d(TAG,"Error in btnSignup onClickListener");
                    e.printStackTrace();
                }

            }
        });


    }

    private void signup(String username, String password) throws ParseException {
        final ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        final int numUser = query.whereMatches("username", username).count();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                // numUser ==0, there is no user with the same username
                if (numUser == 0) {
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e != null){
                                Log.e(TAG,"Issue with Signing up");
                                e.printStackTrace();
                                return;
                            }
                            gotoMainActivity();
                        }
                    });
                } else {
                    Log.d(TAG,"Username taken!");
                    Toast.makeText(LoginActivity.this, "Username taken! Enter a new username.", Toast.LENGTH_LONG).show();
                    etUsername.setText("");
                    etPassword.setText("");
                }
            }
        });
        // Invoke signUpInBackground

    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
             if (e != null){
                 Log.e(TAG,"Issue with Login");
                 e.printStackTrace();
                 return;
             }
             gotoMainActivity();
            }
        });
    }

    private void gotoMainActivity() {
        Log.d(TAG,"Navigating to Main Activity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
