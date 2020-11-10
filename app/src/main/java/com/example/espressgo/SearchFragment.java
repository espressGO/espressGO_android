package com.example.espressgo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchFragment extends Fragment implements View.OnClickListener{

    TextView tvSearch, tvEnter;
    EditText etSearch;
    Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //5 TVs, 3 ETs, 1 RatingBar, 1 Button
        tvSearch = requireView().findViewById(R.id.tvSearch);
        tvEnter = requireView().findViewById(R.id.tvEnter);
        etSearch = requireView().findViewById(R.id.etSearch);
        searchButton = requireView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //make sure search bar isn't empty, then do the search
        if (TextUtils.isEmpty(etSearch.toString())) {
            Toast.makeText(getActivity(), "Search is empty. Please enter what you wish to search.", Toast.LENGTH_LONG).show();
            return;
        }
        //do search here, then move to the search_results fragment
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchResultsFragment()).commit();

    }
}
