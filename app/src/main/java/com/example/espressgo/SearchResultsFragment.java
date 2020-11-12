package com.example.espressgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import models.Shop;
import models.User;

public class SearchResultsFragment extends Fragment {
    public final String localIp = "192.168.1.7:8080";
    public final String http = "http://";

    private static final String TAG = "SearchResultsFragment" ;

    ListView searchListView;
    TextView tvSearchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("espressGO", Context.MODE_PRIVATE);
        String searchString = preferences.getString("search", "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("search");
        editor.apply();
        ArrayList<SearchListItem> searchList = getListData(searchString);
        tvSearchResults = requireView().findViewById(R.id.tvSearchResults);
        searchListView = requireView().findViewById(R.id.searchListView);
        searchListView.setAdapter(new SearchListAdapter(this.getActivity(), searchList));
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchListItem item = (SearchListItem) searchListView.getItemAtPosition(position);
                if (item.getShop() != null) {
                    Toast.makeText(getActivity(), "Shop Page", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("espressGO",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    Shop shop = item.getShop();
                    editor.putString("shop", gson.toJson(shop, Shop.class));
                    editor.apply();
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment()).commit();
                }
                else if (item.getUser() != null) {
                    Toast.makeText(getActivity(), "User was followed", Toast.LENGTH_LONG).show();
                    //FIGURE OUT HOW TO FOLLOW HERE, we can get current user from shared preferences and have the user we want to follow at item.getUser()
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("espressGO",Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String email1 = sharedPreferences.getString("email", "");
                    String email2 = item.getUser().getEmail();
                    ArrayList<String> emails = new ArrayList<>();
                    emails.add(email1);
                    emails.add(email2);
                    boolean followed = follow(emails);
                    if (followed) {
                        Toast.makeText(getActivity(), "User was followed", Toast.LENGTH_LONG).show();
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                    }
                    else {
                        Toast.makeText(getActivity(), "User was not followed", Toast.LENGTH_LONG).show();
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                    }
                }

            }
        });
        //implement listener here for clicking on search results -> go to shop page
    }

    private ArrayList<SearchListItem> getListData(String search) {
        //this needs to be done dynamically
        ArrayList<SearchListItem> items = new ArrayList<>();
        Shop shop = getShopId(search);
        User user = getUser(search);
        if (shop != null) {
            SearchListItem s = new SearchListItem();
            s.setTitle(shop.getShopname());
            s.setShop(shop);
            items.add(s);
        }
        else if (user != null) {
            SearchListItem s = new SearchListItem();
            s.setTitle(user.getEmail());
            s.setUser(user);
            items.add(s);
        }
        else {
            Toast.makeText(this.getActivity(), "Search Results Empty", Toast.LENGTH_LONG).show();
            //switch back to search fragment
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
        }
        return items;
    }

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

    private User getUser(String email) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/findUser";

        try {
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
            Log.d(TAG,"CONNECTION MADE IT THIS FAR");
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }

            }
        } catch (Exception e) {
            Log.d(TAG,"Catching an error here");
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        Log.d(TAG, "Right before printing the User");
        Log.d(TAG, result.toString());
        Gson gson = new Gson();
        return gson.fromJson(result.toString(), User.class);
    }
    private boolean follow(ArrayList<String> emails) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/follow";

        try {
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(emails);
            //Sends to api
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            Log.d(TAG,"CONNECTION MADE IT THIS FAR");
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }

            }
        } catch (Exception e) {
            Log.d(TAG,"Catching an error here");
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        Log.d(TAG, "Right before printing the User");
        Log.d(TAG, result.toString());
        if (result.toString().equals("")) {
            return false;
        }
        else
            return true;
    }
}
