package com.example.espressgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DrinksFragment extends Fragment {
    TextView tvTitle;
    ListView drinkTypesListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drinks,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<DrinkListItem> drinksList = getListData();
        tvTitle = requireView().findViewById(R.id.tvTitle);
        drinkTypesListView = requireView().findViewById(R.id.drinkTypesListView);
        drinkTypesListView.setAdapter(new DrinksListAdapter(this.getActivity(), drinksList));
    }

    private ArrayList<DrinkListItem> getListData() {
        ArrayList<DrinkListItem> drinks = new ArrayList<>();
        DrinkListItem coffee = new DrinkListItem();
        coffee.setDrink("Black Coffee");
        coffee.setDescription("Just Coffee (Any type)");
        drinks.add(coffee);
        DrinkListItem espresso = new DrinkListItem();
        espresso.setDrink("Espresso");
        espresso.setDescription("1 oz espresso ");
        drinks.add(espresso);
        DrinkListItem doppio = new DrinkListItem();
        doppio.setDrink("Doppio");
        doppio.setDescription("2 oz espresso");
        drinks.add(doppio);
        DrinkListItem latte = new DrinkListItem();
        latte.setDrink("latte");
        latte.setDescription("Espresso, steamed milk");
        drinks.add(latte);
        DrinkListItem cappuccino = new DrinkListItem();
        cappuccino.setDrink("Cappuccino");
        cappuccino.setDescription("Espresso, steamed milk, foam");
        drinks.add(cappuccino);
        DrinkListItem americano = new DrinkListItem();
        americano.setDrink("Americano");
        americano.setDescription("Espresso, hot water");
        drinks.add(americano);
        DrinkListItem cortado = new DrinkListItem();
        cortado.setDrink("Cortado");
        cortado.setDescription("1 oz espresso, 1 oz steamed milk");
        drinks.add(cortado);
        DrinkListItem redEye = new DrinkListItem();
        redEye.setDrink("Red Eye");
        redEye.setDescription("Coffee and espresso");
        drinks.add(redEye);
        DrinkListItem galao = new DrinkListItem();
        galao.setDrink("Galao");
        galao.setDescription("Espresso, foamed milk");
        drinks.add(galao);
        DrinkListItem lungo = new DrinkListItem();
        lungo.setDrink("Lungo");
        lungo.setDescription("Long pulled espresso");
        drinks.add(lungo);
        DrinkListItem macchiato = new DrinkListItem();
        macchiato.setDrink("Macchiato");
        macchiato.setDescription("Espresso shot, foam");
        drinks.add(macchiato);
        DrinkListItem mocha = new DrinkListItem();
        mocha.setDrink("Mocha");
        mocha.setDescription("Espresso, chocolate or chocolate syrup, steamed milk");
        drinks.add(mocha);
        DrinkListItem ristretto = new DrinkListItem();
        ristretto.setDrink("Ristretto");
        ristretto.setDescription("Short pulled espresso");
        drinks.add(ristretto);
        DrinkListItem flat = new DrinkListItem();
        flat.setDrink("Flat White");
        flat.setDescription("Espresso, steamed milk");
        drinks.add(flat);
        DrinkListItem affogato = new DrinkListItem();
        affogato.setDrink("Affogato");
        affogato.setDescription("Espresso, ice cream");
        drinks.add(affogato);
        DrinkListItem cafe = new DrinkListItem();
        cafe.setDrink("Cafe Au Lait");
        cafe.setDescription("Coffee, Steamed milk");
        drinks.add(cafe);
        DrinkListItem irish = new DrinkListItem();
        irish.setDrink("Irish Coffee");
        irish.setDescription("Coffee, Whiskey, Sugar, Cream");
        drinks.add(irish);
        DrinkListItem drip = new DrinkListItem();
        drip.setDrink("Drip Coffee");
        drip.setDescription("Made by letting boiling water slowly drip through grounds");
        drinks.add(drip);
        DrinkListItem pour = new DrinkListItem();
        pour.setDrink("Pour Over");
        pour.setDescription("Made by slowly pouring hot water over coffee grounds");
        drinks.add(pour);
        DrinkListItem french = new DrinkListItem();
        french.setDrink("French Press");
        french.setDescription("Made by steeping hot water in coffee grounds and pressing the grounds out");
        drinks.add(french);
        return drinks;
    }

}
