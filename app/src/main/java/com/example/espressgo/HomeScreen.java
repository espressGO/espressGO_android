package com.example.espressgo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.widget.Toast;


public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //this is the user currently logged in, in firebase. we will prob have a user of our own model later
        currentUser = mAuth.getCurrentUser();


        //nav bar setup
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_feed);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                break;
            case R.id.nav_post:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostFragment()).commit();
                break;
            case R.id.nav_drinks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DrinksFragment()).commit();
                break;
            case R.id.nav_shops:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopsFragment()).commit();
                break;
            case R.id.nav_logout:
                logOut();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
