package com.example.espressgo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Message;
import models.Shop;

public class ShopsFragment extends Fragment {
    TextView tvTitle;
    ListView drinkTypesListView;
    public final String localIp = "192.168.1.191:8080";
    private static final String TAG = "ShopsFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drinks,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FollowListItem> drinksList = getListData();
        tvTitle = requireView().findViewById(R.id.tvTitle);
        tvTitle.setText("Shops:  ");
        drinkTypesListView = requireView().findViewById(R.id.drinkTypesListView);
        drinkTypesListView.setAdapter(new FollowListAdapter(this.getActivity(), drinksList));
    }

    private ArrayList<FollowListItem> getListData() {
        ArrayList<FollowListItem> items = new ArrayList<>();
        ArrayList<Shop> shops = callGetShops();
        if (shops == null)
            return null;
        for (Shop shop:shops) {
            Log.d(TAG, shop.getShopname());
            FollowListItem test = new FollowListItem();
            test.setDisplayName(shop.getShopname());
            items.add(test);
        }

        return items;
    }
    private ArrayList<Shop> callGetShops() {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String endpoint = "/getShops";
        try {
            String http = "http://";
            String apiUrl = (http+localIp+endpoint);
            URL requestUrl = new URL(apiUrl);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("POST"); //get
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();
            //Response
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    result.append(responseLine.trim());
                }
                Log.d(TAG, result.toString());

                ArrayList<Shop> shops = gson.fromJson(result.toString(), new TypeToken<List<Shop>>(){}.getType());
                return shops;
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
