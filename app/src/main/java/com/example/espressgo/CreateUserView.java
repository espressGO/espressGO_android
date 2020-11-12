package com.example.espressgo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.graphics.Color;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import models.User;


public class CreateUserView extends AppCompatActivity {
    private static final String TAG = "CreateUserView" ;
    EditText etEmail, etPassword1, etPassword2, etDisplay;
    TextView tvUsername, tvPassword1, tvPassword2, tvDisplay;
    Button createUserButton;

    //create account in firebase
    private FirebaseAuth mAuth;

    //CHANGE THIS VALUE TO YOUR LOCAL IP. IF USING WINDOWS, USE IPCONFIG. LINUX, USE IP A
    public final String localIp = "192.168.1.191:8080";
    public final String http = "http://";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mAuth = FirebaseAuth.getInstance();

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPassword1 = (TextView) findViewById(R.id.tvPassword1);
        tvPassword2 = (TextView) findViewById(R.id.tvPassword2);
        tvDisplay = (TextView) findViewById(R.id.tvDisplay);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        etDisplay = (EditText) findViewById(R.id.etDisplay);
        createUserButton = (Button) findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean goodEmail = verifyEmail(etEmail.getEditableText().toString());
                if(!goodEmail) {
                    Toast toast = Toast.makeText(getApplicationContext(), "E-mail must be valid", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                boolean goodPassword = verifyPassword(etPassword1.getEditableText().toString(),etPassword2.getEditableText().toString());
                if(goodPassword) {
                    createUser(etEmail.getEditableText().toString(), etDisplay.getEditableText().toString(), etPassword1.getEditableText().toString());
                    createActivity();
                }

            }
        });
    }

    private boolean verifyEmail(String toString) {
        return Patterns.EMAIL_ADDRESS.matcher(toString).matches();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,OpeningScreen.class);
        startActivity(intent);
    }

    private boolean verifyPassword(String pass1, String pass2) {
        Log.d(TAG, "PASS1: " + pass1 + " PASS2: " + pass2);
        if(!pass1.equals(pass2))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else if (pass1.length() < 7 || pass1.length() >  14)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Password must be between 8-13 characters", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }
    public void createUser(final String email, final String displayName, String password) {
        //TODO create user in our database in this method
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User();
                            Log.d(TAG,"User's Display Name: " + displayName);
                            newUser.setDisplayName(displayName);
                            newUser.setEmail(email);
                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8)
                            {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                //your codes here
                                saveUser(newUser);

                            }


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUserView.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void saveUser(final User newUser) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/user";
        Log.d(TAG,"Trying to add a new user to the database");
        try {
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(newUser);
            //Sends to api
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d(TAG,"CONNECTION MADE IT THIS FAR");
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }
            }
        } catch (Exception e) {
            Log.d(TAG,"Catching an error here");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.d(TAG, "RESULT:");
        Log.d(TAG, result.toString());
        Gson gson = new Gson();
        User current = gson.fromJson(result.toString(), User.class);
        SharedPreferences sharedPreferences = getSharedPreferences("espressGO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jsonUser",result.toString());
        editor.putString("displayName",current.getDisplayName());
        editor.putString("email",current.getEmail());
        editor.putString("userID", current.getId().toString());
        Log.d(TAG,current.getEmail());
        Log.d(TAG, current.getId().toString());
        editor.apply();

    }

    public void createActivity() {
        Intent home = new Intent(this, OpeningScreen.class);
        startActivity(home);
    }

    @Override
    public void onStart() {
        super.onStart();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT);
        toast.show();
    }


}
