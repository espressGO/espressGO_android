package com.example.espressgo;
import android.content.Intent;
import android.os.Bundle;
//import android.graphics.Color;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import models.Users;

public class CreateUserView extends AppCompatActivity {
    private static final String TAG = "CreateUserView" ;
    EditText etEmail, etPassword1, etPassword2;
    TextView tvUsername, tvPassword1, tvPassword2;
    Button createUserButton;

    //create account in firebase
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mAuth = FirebaseAuth.getInstance();

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPassword1 = (TextView) findViewById(R.id.tvPassword1);
        tvPassword2 = (TextView) findViewById(R.id.tvPassword2);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);

        createUserButton = (Button) findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean goodPassword = verifyPassword(etPassword1.getEditableText().toString(),etPassword2.getEditableText().toString());
                if(goodPassword) {
                    createUser(etEmail.getEditableText().toString(), etPassword1.getEditableText().toString());
                    createActivity();
                }

            }
        });
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
    public void createUser(String email, String password) {
        //TODO create user in our database in this method
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
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
    public void createActivity() {
        Intent home = new Intent(this, HomeScreen.class);
        startActivity(home);
    }

    @Override
    public void onStart() {
        super.onStart();
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
