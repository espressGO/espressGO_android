package com.example.espressgo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class PostFragment extends Fragment implements View.OnClickListener{
    TextView tvTitle, tvShop, tvDrink, tvRating, tvComment;
    EditText etShop, etDrink, etComment;
    RatingBar ratingBar;
    Button createPostButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //5 TVs, 3 ETs, 1 RatingBar, 1 Button
        tvTitle = requireView().findViewById(R.id.tvTitle);
        tvShop = requireView().findViewById(R.id.tvShop);
        tvDrink = requireView().findViewById(R.id.tvDrink);
        tvRating = requireView().findViewById(R.id.tvRating);
        tvComment = requireView().findViewById(R.id.tvComment);
        etShop = requireView().findViewById(R.id.etShop);
        etDrink = requireView().findViewById(R.id.etDrink);
        etComment = requireView().findViewById(R.id.etComment);
        ratingBar = requireView().findViewById(R.id.ratingBar);
        createPostButton = requireView().findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        //Do everything here!
        //Make sure etShop isn't empty, check if shop exists
        //etDrink can be empty if it is a shop rating, but if it isn't empty check if it exists
        //Make sure etComment isn't empty
        //Once this is done, create a new post variable and add it to the database!
        if (TextUtils.isEmpty((CharSequence) etShop)) {
            Toast.makeText(getActivity(), "Shop is empty. Please re-enter the shop you wish to review.", Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty((CharSequence) etComment)) {
            Toast.makeText(getActivity(), "Comment is empty. Please re-enter the comment you wish to post.", Toast.LENGTH_LONG).show();
            return;
        }

        //Shops shop = searchForShop((CharSequence) etShop);
//        if (TextUtils.isEmpty((CharSequence) etShop)) {
//            //drink is empty, post the comment here without a drink
//        }
//        else {
//            //Drinks drink = searchForDrink((CharSequence) etDrink)
//            //make post here with a drink
//        }

    }
}
