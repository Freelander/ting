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
import com.music.ting.model.user.UserLikeSongs;
import com.music.ting.ui.activity.CommentActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jun on 2015/6/27.
 */
public class LikeSongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private UserLikeSongs userLikeSongs;

    public LikeSongsAdapter(Context mContext, UserLikeSongs userLikeSongs) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.userLikeSongs = userLikeSongs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View likeSongsViewHolder = mLayoutInflater.inflate(R.layout.item_user_songs_card_list,
                parent, false);
        View noLikeSongsViewHolder = mLayoutInflater.inflate(R.layout.item_not_comment,
                parent,false);
        if(userLikeSongs.getLikeSongs().size() == 0){
            return new NoLikeSongsViewHolder(noLikeSongsViewHolder);
        }else{
            return new LikeSongsViewHolder(likeSongsViewHolder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof  LikeSongsViewHolder){
            ((LikeSongsViewHolder)holder).songTitle.setText(userLikeSongs.getLikeSongs().get(position).getTitle());

            ((LikeSongsViewHolder) holder).songTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    Songs songs = new Songs();
                    songs.setId(userLikeSongs.getLikeSongs().get(position).getId());
                    songs.setsId((long)userLikeSongs.getLikeSongs().get(position).getsId());
                    songs.setUserId(userLikeSongs.getLikeSongs().get(position).getUserId());
                    songs.setTitle(userLikeSongs.getLikeSongs().get(position).getTitle());
                    songs.setArtist(userLikeSongs.getLikeSongs().get(position).getArtist());
                    songs.setContent(userLikeSongs.getLikeSongs().get(position).getContent());
                    songs.setUrlPic(userLikeSongs.getLikeSongs().get(position).getUrlPic());
                    songs.setCommentsCount(userLikeSongs.getLikeSongs().get(position).getCommentsCount());
                    intent.putExtra("Songs", songs);
                    mContext.startActivity(intent);
                }
            });
        }else{
            ((NoLikeSongsViewHolder)holder).notComment.setGravity(Gravity.CENTER);
            ((NoLikeSongsViewHolder)holder).notComment.setText("No Like songs yet");
        }
    }

    @Override
    public int getItemCount() {
        return userLikeSongs.getLikeSongs().size() == 0 ? 1 : userLikeSongs.getLikeSongs().size();
    }

    public static class LikeSongsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.play)
        ImageView play;
        @InjectView(R.id.song_title)
        TextView songTitle;

        public LikeSongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class NoLikeSongsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.not_comment)
        TextView notComment;
        public NoLikeSongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}