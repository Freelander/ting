package com.music.ting.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.Songs;
import com.music.ting.model.user.UserComments;
import com.music.ting.ui.activity.CommentActivity;
import com.music.ting.ui.widget.RoundImageView;
import com.music.ting.utils.TingAPI;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jun on 2015/6/28.
 */
public class UserCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private UserComments mUserComments;
    private HashMap hashMap = new HashMap();

    public UserCommentsAdapter(Context mContext, UserComments mUserComments) {
        this.mContext = mContext;
        this.mUserComments = mUserComments;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View commentsView = mLayoutInflater.inflate(R.layout.item_song_comments,
                parent, false);
        View noCommentsView = mLayoutInflater.inflate(R.layout.item_not_comment, parent, false);
        if (mUserComments.getComments().size() == 0) {
            return new NoCommentsViewHolder(noCommentsView);
        } else {
            return new UserCommentsViewHolder(commentsView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserCommentsViewHolder) {
            Picasso.with(mContext)
                    .load(mUserComments.getUser().getAvatar().getUrl())
                    .placeholder(R.drawable.ic_default_pic)
                    .error(R.drawable.ic_default_pic)
                    .resize(50, 50)
                    .centerCrop()
                    .into(((UserCommentsViewHolder) holder).commentUserPic);

            /**
             * 根据歌曲Id获取歌曲信息
             */
            final GsonRequest<Songs> request = TingAPI.getSongsInfoByIdRequest(mUserComments.
                    getComments().get(position).getSongId());
            final Response.Listener<Songs> response = new Response.Listener<Songs>() {
                @Override
                public void onResponse(Songs songs) {
                    hashMap.put(position, songs);
                    ((UserCommentsViewHolder) holder).commentSongName.setText(((Songs) hashMap.
                            get(position)).getTitle());
                }
            };
            request.setSuccessListener(response);
            RequestManager.addRequest(request, null);


            ((UserCommentsViewHolder) holder).commentContent.setText(mUserComments.
                    getComments().get(position).getContent());

            ((UserCommentsViewHolder) holder).commentSongName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra("Songs",(Songs)hashMap.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof NoCommentsViewHolder) {
            ((NoCommentsViewHolder)holder).notComment.setGravity(Gravity.CENTER);
            ((NoCommentsViewHolder)holder).notComment.setText("No comments yet");
        }

    }

    @Override
    public int getItemCount() {
        return mUserComments.getComments().size() == 0 ? 1 : mUserComments.getComments().size();
    }

    public class UserCommentsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.comment_user_pic)
        RoundImageView commentUserPic;
        @InjectView(R.id.comment_user_name)
        TextView commentSongName;
        @InjectView(R.id.comment_content)
        TextView commentContent;

        public UserCommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class NoCommentsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.not_comment)
        TextView notComment;
        public NoCommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
