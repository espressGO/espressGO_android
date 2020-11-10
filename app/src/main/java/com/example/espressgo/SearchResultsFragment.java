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

public class SearchResultsFragment extends Fragment {
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
        ArrayList<SearchListItem> searchList = getListData();
        tvSearchResults = requireView().findViewById(R.id.tvSearchResults);
        searchListView = requireView().findViewById(R.id.searchListView);
        searchListView.setAdapter(new SearchListAdapter(this.getActivity(), searchList));
        //implement listener here for clicking on search results -> go to shop page
    }

    private ArrayList<SearchListItem> getListData() {
        //this needs to be done dynamically
        ArrayList<SearchListItem> items = new ArrayList<>();
        SearchListItem test = new SearchListItem();
        test.setTitle("Test Search Email or Shop");
        items.add(test);
        return items;
    }


}
