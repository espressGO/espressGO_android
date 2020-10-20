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

import models.Users;

public class CreateUserView extends AppCompatActivity {
    EditText etUsername, etPassword1, etPassword2;
    TextView tvUsername, tvPassword1, tvPassword2;
    Button createUserButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPassword1 = (TextView) findViewById(R.id.tvPassword1);
        tvPassword2 = (TextView) findViewById(R.id.tvPassword2);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);

        createUserButton = (Button) findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO DO: Add a user, check if the passwords both match!
                createActivity();
            }
        });
    }

    public void createActivity() {
        Intent home = new Intent(this, HomeScreen.class);
        startActivity(home);
    }
}
