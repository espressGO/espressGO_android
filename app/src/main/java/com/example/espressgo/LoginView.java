package com.example.espressgo;

import android.content.Intent;
import android.os.Bundle;
//import android.graphics.Color;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginView extends AppCompatActivity{
    private static final String TAG = "LoginView" ;

    EditText edEmail, edPassword;
    TextView tvUsername, tvPassword;
    Button btnLogin;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
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
            Log.d(TAG, "User found at start, proceed");
            updateUI(currentUser);
            homeActivity();
        }

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
