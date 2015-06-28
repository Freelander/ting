package com.music.ting.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.adapter.UserCommentsAdapter;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.user.UserComments;
import com.music.ting.utils.TingAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jun on 2015/6/28.
 */
@SuppressLint("ValidFragment")
public class UserCommentsFragment extends Fragment {

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.songs_swipe)
    SwipeRefreshLayout songsSwipe;
    private View view;
    private int userId;

    @SuppressLint("ValidFragment")
    public UserCommentsFragment(int userId){
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_songs, container, false);

        ButterKnife.inject(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        songsSwipe.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary);
        songsSwipe.post(new Runnable() {
            @Override
            public void run() {
                songsSwipe.setRefreshing(true);
            }
        });
        initData();
        songsSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        return view;
    }

    private void initData() {
        final GsonRequest<UserComments> request = TingAPI.getUserComments(userId);

        final Response.Listener<UserComments> response = new Response.Listener<UserComments>() {
            @Override
            public void onResponse(UserComments comments) {
                Log.i("UserComment",comments.toString() + ",UserId:"+userId);
                recyclerView.setAdapter(new UserCommentsAdapter(view.getContext(), comments));
                songsSwipe.setRefreshing(false);
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
