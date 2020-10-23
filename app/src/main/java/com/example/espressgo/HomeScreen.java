package com.example.espressgo;
import android.content.Intent;
import android.os.Bundle;
//import android.graphics.Color;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import models.Users;

public class HomeScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        //this is the user currently logged in, in firebase. we will prob have a user of our own model later
        currentUser = mAuth.getCurrentUser();

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

//        tvUsername = (TextView) findViewById(R.id.tvUsername);
//        tvPassword = (TextView) findViewById(R.id.tvPassword);
//        edUsername = (EditText) findViewById(R.id.edUsername);
//        edPassword = (EditText) findViewById(R.id.edPassword);

//        btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    @Override
    public void onBackPressed()
    {
        logOut();
    }
    //log the user out
    public void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent home = new Intent(this, OpeningScreen.class);
        startActivity(home);
        Toast loggedOut = Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG);
        loggedOut.show();
    }
}
