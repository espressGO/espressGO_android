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

public class FollowingFragment extends Fragment {
    ListView followingListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FollowListItem> followList = getListData();
        followingListView = requireView().findViewById(R.id.followingListView);
        followingListView.setAdapter(new FollowListAdapter(this.getActivity(),followList));
    }

    private ArrayList<FollowListItem> getListData() {
        ArrayList<FollowListItem> following = new ArrayList<>();
        return following;
    }
}
