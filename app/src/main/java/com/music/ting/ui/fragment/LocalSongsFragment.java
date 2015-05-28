package com.music.ting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.music.ting.R;
import com.music.ting.adapter.LocalSongsAdapter;
import com.music.ting.model.LocalSongs;
import com.music.ting.utils.MediaUtils;

import java.util.List;

/**
 * Created by Jun on 2015/5/27.
 */
public class LocalSongsFragment extends Fragment {

    private ListView localListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_songs,container,false);

        localListView = (ListView) view.findViewById(R.id.local_songs_list);

        List<LocalSongs> localSongsList = MediaUtils.getLocalSongs(view.getContext());

        localListView.setAdapter(new LocalSongsAdapter(view.getContext(),
                R.layout.item_songs_list, localSongsList));

        return view;
    }
}
