package com.example.espressgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
//import android.graphics.Color;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import models.User;

public class LoginView extends AppCompatActivity{
    private static final String TAG = "LoginView" ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText edEmail, edPassword;
    TextView tvUsername, tvPassword;
    Button btnLogin;


    private FirebaseAuth mAuth;

    public final String localIp = "192.168.1.191:8080";
    public final String http = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);



        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO DO: if statement to check if username and password are correct
                checkSignIn(edEmail.getEditableText().toString(), edPassword.getEditableText().toString());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,OpeningScreen.class);
        startActivity(intent);
    }

    public void homeActivity(){
        Intent home = new Intent(this, HomeScreen.class);
        startActivity(home);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null) {
            FirebaseAuth.getInstance().signOut();
            Intent home = new Intent(this, OpeningScreen.class);
            startActivity(home);
            Toast loggedOut = Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG);
            loggedOut.show();
            Log.d(TAG, "User found at start, proceed");
            updateUI(currentUser);
            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                getUser(currentUser);
            }
            homeActivity();
        }

    }

    private void getUser(FirebaseUser currentUser) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/findUser";

        try {
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(currentUser.getEmail());
            //Sends to api
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            Log.d(TAG,"CONNECTION MADE IT THIS FAR");
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }

            }
        } catch (Exception e) {
            Log.d(TAG,"Catching an error here");
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        Log.d(TAG, "Right before printing the User");
        Log.d(TAG, result.toString());
        Gson gson = new Gson();
        User current = gson.fromJson(result.toString(), User.class);
        sharedPreferences = getSharedPreferences("espressGO", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("jsonUser",result.toString());
        if (current != null) {
            editor.putString("displayName",current.getDisplayName());
            editor.putString("email",current.getEmail());
            editor.putString("userID", current.getId().toString());
        }

//        Log.d(TAG,current.getEmail());
//        Log.d(TAG, current.getId().toString());
        editor.apply();

    }

    public void checkSignIn(String email, String password)
    {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            int SDK_INT = Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                getUser(user);
                            }

                            homeActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginView.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }

    public void updateUI(FirebaseUser user)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT);
        toast.show();
    }

}
