package com.example.espressgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Message;
import models.Shop;

public class FeedFragment extends Fragment {
    ListView feedListView;

    public final String localIp = "192.168.1.7:8080";
    private static final String TAG = "FeedFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Message> messages = new ArrayList<>();
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            SharedPreferences preferences = getActivity().getSharedPreferences("espressGO", Context.MODE_PRIVATE);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            messages = callFeed(preferences.getString("email", ""));

        }
        ArrayList<FeedListItem> feedList = getListData(messages);
        feedListView = requireView().findViewById(R.id.feedListView);
        feedListView.setAdapter(new FeedListAdapter(this.getActivity(), feedList));
    }

    private ArrayList<FeedListItem> getListData(ArrayList<Message> messages) {
        //Access current users following list and populate each of their posts to a new array list
        ArrayList<FeedListItem> feedList = new ArrayList<>();
        for (Message message: messages) {
            FeedListItem test = new FeedListItem();
            test.setEmail(message.getUserEmail());
            test.setShop(message.getShopname());
            test.setDrink(message.getDrinkname());
            test.setComment(message.comment);
            feedList.add(test);
        }
        return feedList;
    }
    private ArrayList<Message> callFeed(String email) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/getFeed";
        try {
            String http = "http://";
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(email);
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
                Log.d(TAG, result.toString());
                ArrayList<Message> messages =  gson.fromJson(result.toString(), new TypeToken<List<Message>>(){}.getType());
                return messages;
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
