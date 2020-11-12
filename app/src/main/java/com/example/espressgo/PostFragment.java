package com.example.espressgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.gson.Gson;

import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import models.Drink;
import models.Message;
import models.Shop;
import models.User;

public class PostFragment extends Fragment implements View.OnClickListener{
    TextView tvTitle, tvShop, tvDrink, tvRating, tvComment;
    EditText etShop, etDrink, etComment;
    RatingBar ratingBar;
    Button createPostButton;
    private static final String TAG = "PostFragment" ;
    SharedPreferences sharedPreferences;

    public final String localIp = "192.168.1.191:8080";
    public final String http = "http://";

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
        createPostButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            SharedPreferences preferences = getActivity().getSharedPreferences("espressGO", Context.MODE_PRIVATE);
            String userId = preferences.getString("email", "");
            Log.d(TAG, userId);

            Shop shop = getShopId(etShop.getEditableText().toString());
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            if(shop != null && userId != null) {
                if (etDrink.getEditableText().toString().equals("")) {
                    Log.d(TAG, shop.getId().toString());
                    createPost(shop.getId(),etDrink.getEditableText().toString(),etComment.getEditableText().toString(), userId, shop);
                }
                else {
                    ArrayList<Drink> drinks = shop.getDrinks();
                    Iterator<Drink> drinkIterator = drinks.iterator();
                    boolean found = false;
                    while(drinkIterator.hasNext()) {
                        Drink drink = drinkIterator.next();
                        if (drink.getDrink_name() != null && drink.getDrink_name().equals(etDrink.getEditableText().toString())) {
                            found = true;
                        }
                    }
                    if (found) {
                        Log.d(TAG, shop.getId().toString());
                        createPost(shop.getId(),etDrink.getEditableText().toString(),etComment.getEditableText().toString(), userId, shop);
                    }
                    else {
                        Toast.makeText(this.getActivity(), "Drink Not Found", Toast.LENGTH_LONG).show();
                        return;
                    }
                }


            }
            else {
                Log.d(TAG, "Shop ID was null!");
                Toast.makeText(this.getActivity(), "Shop Not Found", Toast.LENGTH_LONG).show();
                return; //handle error
            }

        }
    }

    private void createPost(ObjectId shopId, String drink, String comment, String userId, Shop shop) {


        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/createmessage";
        Message newMessage = new Message();
        newMessage.setShopId(shopId);
        newMessage.setUserEmail(userId);
        if (drink != "" )
            newMessage.setDrinkname(drink);

        Log.d(TAG, "Trying to get object from shop");

        newMessage.setRating((int)ratingBar.getRating());
        newMessage.setComment(comment);
        try {
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(newMessage);
            //Sends to api
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        if (result != null) {
            Log.d(TAG, "Post Created!");
            Toast.makeText(this.getActivity(), "Post Created!", Toast.LENGTH_LONG).show();
            etComment.setText("");
            etDrink.setText("");
            etShop.setText("");
            ratingBar.setNumStars(0);

        }


    }

    //return shop name
    private Shop getShopId(String shopname)
    {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/getShop";
        Log.d(TAG, "Trying to find shop by shopname");
        Log.d(TAG, shopname);
        try {
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(shopname);
            //Sends to api
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }
                Log.d(TAG, "Shop: " + result.toString());
                Shop current = gson.fromJson(result.toString(), Shop.class);
                if (current != null)
                    return current;
                else
                    return null; //handle error
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return null;
    }
}
