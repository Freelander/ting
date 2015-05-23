package com.music.ting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.adapter.SongsListAdapter;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.Songs;
import com.music.ting.utils.TingAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun on 2015/5/13.
 */
public class SongsListFragment extends Fragment {

    private ListView songListView;
    private SwipeRefreshLayout songSwipe;
    private View view;
    private List<Songs> songsList = new ArrayList<Songs>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs_list,container,false);

        songListView = (ListView) view.findViewById(R.id.songs_list);
        songSwipe = (SwipeRefreshLayout) view.findViewById(R.id.song_swipe);

        //给刷新控件一个颜色
        songSwipe.setColorSchemeColors(R.color.colorPrimary);
        //刚进入到界面设置刷新控件正在刷新
        songSwipe.post(new Runnable() {
            @Override
            public void run() {
                songSwipe.setRefreshing(true);
            }
        });
        //加载数据
        initData(20);
        /**
         * 刷新控件监听事件
         */
        songSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(20);
            }
        });


        return view;
    }

    /**
     * 初始化数据
     */
    public void initData(final int songCount){
        final GsonRequest<List<Songs>> request = TingAPI.getSongsRequest(songCount);

        final Response.Listener<List<Songs>> response = new Response.Listener<List<Songs>>() {
            @Override
            public void onResponse(List<Songs> songs) {
                songsList = songs;
                songListView.setAdapter(new SongsListAdapter(view.getContext(),
                        R.layout.item_songs_list,songsList));
                songSwipe.setRefreshing(false);
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, null);
    }

}
