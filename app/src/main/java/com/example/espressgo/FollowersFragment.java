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

public class FollowersFragment extends Fragment {
    ListView followersListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FollowListItem> followList = getListData();
        followersListView = requireView().findViewById(R.id.followingListView);
        followersListView.setAdapter(new FollowListAdapter(this.getActivity(),followList));
    }

    private ArrayList<FollowListItem> getListData() {
        ArrayList<FollowListItem> followers = new ArrayList<>();
        return followers;
    }
}