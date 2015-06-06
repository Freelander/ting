package com.music.ting.adapter;

import android.content.Context;

import com.music.ting.R;
import com.music.ting.model.LocalSongs;

import java.util.List;

/**
 * Created by Jun on 2015/5/28.
 */
public class LocalSongsAdapter extends SimpleBaseAdapter<LocalSongs> {

    private Context context;

    public LocalSongsAdapter(Context context, int layoutId, List<LocalSongs> data) {
        super(context, layoutId, data);
        this.context = context;
    }

    @Override
    public void getItemView(ViewHolder holder, LocalSongs localSongs) {
        holder.setText(R.id.song_title,localSongs.getTitle())
                .setText(R.id.song_artist,localSongs.getArtist())
                .setText(R.id.song_content,localSongs.getAlbum());

//        Bitmap bitmap = MediaUtils.getArtwork(context, localSongs.getId(),
//                localSongs.getAlbumId(), true, true);
//        holder.setImageBitmap(R.id.song_image,bitmap);
    }
}
