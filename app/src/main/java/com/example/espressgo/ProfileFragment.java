package com.example.espressgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    FirebaseUser user;
    TextView tvProfile, tvEmail, tvUserEmail;
    Button followersButton, followingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }

        tvProfile = requireView().findViewById(R.id.tvProfile);
        tvEmail = requireView().findViewById(R.id.tvEmail);
        tvUserEmail = requireView().findViewById(R.id.tvUserEmail);
        followersButton = requireView().findViewById(R.id.followersButton);
        followersButton.setOnClickListener(this);
        followingButton = requireView().findViewById(R.id.followingButton);
        followingButton.setOnClickListener(this);
        tvUserEmail.setText(email);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.followersButton:
                //go to followers page
                break;
            case R.id.followingButton:
                //go to following page
                break;
            default:
                break;
        }
    }
}
