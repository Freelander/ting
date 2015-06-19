package com.music.ting.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.Comments;
import com.music.ting.model.Songs;
import com.music.ting.model.UserInfo;
import com.music.ting.ui.widget.RoundImageView;
import com.music.ting.utils.TingAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jun on 2015/5/20.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int SONG = 0;
    private final int SLOGAN = 1;
    private final int COMMENT = 2;

    private List<Comments> commentsList;
    private Songs songs;
    private Context mContext;
    private boolean isLike = false;

    public CommentAdapter(Context mContext, List<Comments> commentsList, Songs songs){
        this.mContext = mContext;
        this.commentsList = commentsList;
        this.songs = songs;
    }

    /**
     * 获取当前Item（View）是哪种布局类型
     * @param position
     * @return 布局类型
     */
    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return SONG;
            case 1:
                return SLOGAN;
            default:
                return COMMENT;
        }
    }

    /**
     * 创建不同 Item 的ViewHolder
     * @param parent
     * @param viewType View类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View song_view = LayoutInflater.from(mContext).
                 inflate(R.layout.item_header_song,parent,false);
        View slogan_view = LayoutInflater.from(mContext).
                inflate(R.layout.item_slogan,parent,false);
        View comment_view = LayoutInflater.from(mContext).
                inflate(R.layout.item_song_comments,parent,false);
        View not_comment_view = LayoutInflater.from(mContext).
                inflate(R.layout.item_not_comment,parent,false);

        switch (viewType){
            case SONG:
                return new SongViewHolder(song_view);
            case SLOGAN:
                return  new SloganViewHolder(slogan_view);
            case COMMENT:
                if(commentsList.size() == 0){
                    return  new NotCommentViewHolder(not_comment_view);
                }else{
                    return new CommentViewHolder(comment_view);
                }
            default:
                return new SongViewHolder(song_view);
        }
    }

    /**
     * 为不同 Item 的 ViewHolder 绑定数据
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof SongViewHolder){ //为SongViewHolder里面属性绑定值

            ((SongViewHolder) viewHolder).songTitle.setText(songs.getTitle());
            ((SongViewHolder) viewHolder).songArtist.setText(songs.getArtist());
            ((SongViewHolder) viewHolder).songContent.setText(songs.getContent());

            Picasso.with(mContext)
                    .load(songs.getUrlPic())
                    .placeholder(R.drawable.ic_default_pic)
                    .error(R.drawable.ic_default_pic)
                    .resize(50,50)
                    .centerCrop()
                    .into(((SongViewHolder) viewHolder).songPic);

            /**
             * 根据用户Id获取用户头像
             */
            final GsonRequest<UserInfo> request = TingAPI.getUserInfoRequestById(songs.getUserId());
            final Response.Listener<UserInfo> response = new Response.Listener<UserInfo>() {
                @Override
                public void onResponse(UserInfo userInfo) {
                    Picasso.with(mContext)
                            .load(userInfo.getAvatar().geturl())
                            .placeholder(R.drawable.ic_default_pic)
                            .error(R.drawable.ic_default_pic)
                            .resize(50,50)
                            .centerCrop()
                            .into(((SongViewHolder) viewHolder).songShareUserPic);

                }
            };
            request.setSuccessListener(response);
            RequestManager.addRequest(request, songs.getUserId());

            ((SongViewHolder) viewHolder).songLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLike){
                        likeAnimator(((SongViewHolder) viewHolder).songLike,R.drawable.ic_like);
                        isLike = false;
                    }else{
                        likeAnimator(((SongViewHolder) viewHolder).songLike,R.drawable.ic_like_selector);
                        isLike = true;
                    }
                }
            });

        }else if(viewHolder instanceof SloganViewHolder){ //为标识View绑定值

            ((SloganViewHolder) viewHolder).slogan.setText("评论");

        }else if(viewHolder instanceof CommentViewHolder){ //为评论View绑定值
            //获取用户Id
            final int userId = commentsList.get(position - 2).getUserId();

            /**
             * 根据用户Id获取用户名和用户头像
             */
            final GsonRequest<UserInfo> request = TingAPI.getUserInfoRequestById(userId);
            final Response.Listener<UserInfo> response = new Response.Listener<UserInfo>() {
                @Override
                public void onResponse(UserInfo userInfo) {
                    ((CommentViewHolder) viewHolder).commentUser.
                            setText(userInfo.getName());
                    Picasso.with(mContext)
                            .load(userInfo.getAvatar().geturl())
                            .placeholder(R.drawable.ic_default_pic)
                            .error(R.drawable.ic_default_pic)
                            .resize(50,50)
                            .centerCrop()
                            .into((((CommentViewHolder) viewHolder).commentUserPic));

                }
            };
            request.setSuccessListener(response);
            RequestManager.addRequest(request, userId);


            ((CommentViewHolder) viewHolder).commentContent.
                    setText(commentsList.get(position - 2).getContent());
        }else if(viewHolder instanceof NotCommentViewHolder){
            ((NotCommentViewHolder) viewHolder).notComment.setText("没有人评论");
        }
    }


    /**
     * 判断有几个View
     * @return
     */
    @Override
    public int getItemCount() {
        return commentsList.size() == 0 ? 3 :commentsList.size() + 2;
    }

    /**
     * 歌曲信息 Song Viewolder类
     */
    public static class SongViewHolder extends RecyclerView.ViewHolder{

        TextView songTitle;
        TextView songArtist;
        TextView songContent;
        ImageView songPic;
        RoundImageView songShareUserPic;
        ImageView songLike;

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = (TextView) itemView.findViewById(R.id.song_title);
            songArtist = (TextView) itemView.findViewById(R.id.song_artist);
            songContent = (TextView) itemView.findViewById(R.id.song_content);
            songPic = (ImageView) itemView.findViewById(R.id.song_image);
            songShareUserPic = (RoundImageView) itemView.findViewById(R.id.share_user);
            songLike = (ImageView) itemView.findViewById(R.id.like);

        }
    }

    /**
     * Comment 评论 ViewHolder类
     */
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentUser;
        TextView commentContent;
        RoundImageView commentUserPic;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentUser = (TextView) itemView.findViewById(R.id.comment_user_name);
            commentContent = (TextView) itemView.findViewById(R.id.comment_content);
            commentUserPic = (RoundImageView) itemView.findViewById(R.id.comment_user_pic);
        }
    }

    /**
     * Slogan 标识ViewHolder类
     */
    public static class SloganViewHolder extends RecyclerView.ViewHolder {

        TextView slogan;

        public SloganViewHolder(View itemView) {
            super(itemView);

            slogan = (TextView) itemView.findViewById(R.id.slogan);
        }
    }

    /**
     * 没有评论 Viewholder类
     */
    public static class NotCommentViewHolder extends RecyclerView.ViewHolder {

        TextView notComment;

        public NotCommentViewHolder(View itemView) {
            super(itemView);

            notComment = (TextView) itemView.findViewById(R.id.not_comment);
        }
    }

    /**
     * 动画
     * @param imageView
     * @param resourceId
     */
    private void likeAnimator(final ImageView imageView,  final int resourceId) {

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(imageView, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(new OvershootInterpolator());
        bounceAnimX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setImageResource(resourceId);
            }
        });


        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(imageView, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(new OvershootInterpolator());
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setImageResource(resourceId);
            }
        });

        animatorSet.play(rotationAnim);
        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

        animatorSet.start();
    }
}
