package com.music.ting.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.ting.R;
import com.music.ting.model.Songs;
import com.music.ting.model.user.UserShareSongs;
import com.music.ting.ui.activity.CommentActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jun on 2015/6/23.
 */
public class ShareSongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private UserShareSongs userShareSongs;

    public ShareSongsAdapter(Context mContext, UserShareSongs userShareSongs) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.userShareSongs = userShareSongs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View shareSongsView = mLayoutInflater.inflate(R.layout.item_user_songs_card_list,
                parent, false);
        View noShareSongsView = mLayoutInflater.inflate(R.layout.item_not_comment,
                parent,false);
        if(userShareSongs.getSongs().size() == 0){
            return new NoShareSongsViewHolder(noShareSongsView);
        }else{
            return new ShareSongsViewHolder(shareSongsView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShareSongsViewHolder) {
            ((ShareSongsViewHolder) holder).songTitle.setText(userShareSongs.getSongs().get(position).getTitle());

            ((ShareSongsViewHolder) holder).songTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    Songs songs = new Songs();
                    songs.setId(userShareSongs.getSongs().get(position).getId());
                    songs.setsId((long)userShareSongs.getSongs().get(position).getsId());
                    songs.setUserId(userShareSongs.getSongs().get(position).getUserId());
                    songs.setTitle(userShareSongs.getSongs().get(position).getTitle());
                    songs.setArtist(userShareSongs.getSongs().get(position).getArtist());
                    songs.setContent(userShareSongs.getSongs().get(position).getContent());
                    songs.setUrlPic(userShareSongs.getSongs().get(position).getUrlPic());
                    songs.setCommentsCount(userShareSongs.getSongs().get(position).getCommentsCount());
                    intent.putExtra("Songs", songs);
                    mContext.startActivity(intent);
                }
            });
        }else if(holder instanceof NoShareSongsViewHolder){
            ((NoShareSongsViewHolder)holder).notComment.setGravity(Gravity.CENTER);
            ((NoShareSongsViewHolder)holder).notComment.setText("No share songs yet");
        }
    }
    @Override
    public int getItemCount() {
        return userShareSongs.getSongs().size() == 0 ? 1 : userShareSongs.getSongs().size();
    }

    public static class ShareSongsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.play)
        ImageView play;
        @InjectView(R.id.song_title)
        TextView songTitle;

        public ShareSongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class NoShareSongsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.not_comment)
        TextView notComment;
        public NoShareSongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
