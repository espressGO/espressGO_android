package com.example.espressgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    ListView feedListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FeedListItem> feedList = getListData();
        feedListView = requireView().findViewById(R.id.feedListView);
        feedListView.setAdapter(new FeedListAdapter(this.getActivity(), feedList));
    }

    private ArrayList<FeedListItem> getListData() {
        //Access current users following list and populate each of their posts to a new array list
        ArrayList<FeedListItem> feedList = new ArrayList<>();
        FeedListItem test = new FeedListItem();
        test.setEmail("testemail@email.com");
        test.setShop("Test Shop");
        test.setDrink("Test Drink");
        test.setComment("Here is the example comment");
        feedList.add(test);
        FeedListItem test2 = new FeedListItem();
        test2.setEmail("testemail@email.com");
        test2.setShop("Test Shop");
        test2.setComment("Here is the example comment no drink");
        feedList.add(test2);
        return feedList;
    }

}
