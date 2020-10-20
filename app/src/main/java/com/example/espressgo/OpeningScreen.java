package com.example.espressgo;

import android.content.Intent;
import android.os.Bundle;
//import android.graphics.Color;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OpeningScreen extends AppCompatActivity {
    Button loginButton, createButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity();
            }
        });
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createActivity();
            }
        });
    }
    public void loginActivity(){
        Intent login = new Intent(this, LoginView.class);
        startActivity(login);
    }
    public void createActivity(){
        Intent create = new Intent(this, CreateUserView.class);
        startActivity(create);
    }
}
