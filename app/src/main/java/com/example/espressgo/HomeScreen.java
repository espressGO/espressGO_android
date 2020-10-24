package com.example.espressgo;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.Toast;


public class HomeScreen extends AppCompatActivity {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        //this is the user currently logged in, in firebase. we will prob have a user of our own model later
        currentUser = mAuth.getCurrentUser();



        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logOut();
        }
    }

    public void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent home = new Intent(this, OpeningScreen.class);
        startActivity(home);
        Toast loggedOut = Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG);
        loggedOut.show();
    }
}
