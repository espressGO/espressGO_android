package com.example.espressgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;

import models.Drink;
import models.Shop;

public class ShopFragment extends Fragment {
    ListView listViewMenu;
    TextView tvShopName, tvMenu;
    Rating shopRatingBar;
    ImageView shopImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvShopName = requireView().findViewById(R.id.tvShopName);
        ArrayList<DrinkListItem> menu = getListData();
        listViewMenu = requireView().findViewById(R.id.listViewMenu);
        listViewMenu.setAdapter(new DrinksListAdapter(this.getActivity(),menu));
    }

    private ArrayList<DrinkListItem> getListData() {
        ArrayList<DrinkListItem> menu = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("espressGO", Context.MODE_PRIVATE);
        String shopJson = preferences.getString("shop", "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("shop");
        editor.apply();
        Gson gson = new Gson();
        Shop shop = gson.fromJson(shopJson,Shop.class);
        tvShopName.setText(shop.getShopname());
        ArrayList<Drink> drinks = shop.getDrinks();
        for (Drink drink : drinks) {
            DrinkListItem item = new DrinkListItem();
            item.setDrink(drink.getDrink_name());
            item.setDescription("Type: " + drink.getDrink_type() + "  Price: " + drink.getPrice());
            menu.add(item);
        }
        return menu;
    }
}