package com.music.ting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.adapter.CommentAdapter;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.Comments;
import com.music.ting.model.Songs;
import com.music.ting.utils.TingAPI;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jun on 2015/5/20.
 */
public class CommentActivity extends BaseActivity {

    private final static String TAG = "Ting";

    public Songs songs = new Songs();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout commentSwipe;
    private Toolbar toolbar;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView songImage;//歌曲封面
    private ImageView songLargeImage;//歌曲封面大图
    private TextView songTitle,songArtist;//歌名，歌手
    private ImageView playImage;//播放按钮
    private ImageView repeatImage;//循环按钮
    private ImageView rewIamge;//后退按钮
    private ImageView playBtnIamge;//播放按钮
    private ImageView fwdIamge;//快进按钮
    private ImageView shuffleIamge;//随机播放按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info);
        initToolbar();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //点击返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");
                playImage.setImageResource(R.drawable.ic_ting_music);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
                playImage.setImageResource(R.drawable.ic_play_black_round);
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy < 0){ //当用户向上滑时，隐藏SlidingUpPanelLayout
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }else{//当用户向上滑时，显示SlidingUpPanelLayout
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });

        /**
         * 刷新控件监听
         */
        commentSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSongCommentById(songs.getId());
            }
        });

    }

    public void getData(){

        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        commentSwipe = (SwipeRefreshLayout) this.findViewById(R.id.comment_swipe);
        songImage = (ImageView) findViewById(R.id.song_image);
        songLargeImage = (ImageView) findViewById(R.id.song_image_large);
        songTitle = (TextView) findViewById(R.id.song_title);
        songArtist = (TextView) findViewById(R.id.song_artist);
        playImage = (ImageView) findViewById(R.id.play);
        repeatImage = (ImageView) findViewById(R.id.repeat);
        playBtnIamge = (ImageView) findViewById(R.id.play_btn);
        fwdIamge = (ImageView) findViewById(R.id.fwd);
        shuffleIamge = (ImageView) findViewById(R.id.shuffle);

        commentSwipe.post(new Runnable() {
            @Override
            public void run() {
                commentSwipe.setRefreshing(true);
            }
        });
        /**
         * 给RecyclerView线性布局
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

        Intent intent = getIntent();
        songs = intent.getParcelableExtra("Songs");

        Picasso.with(CommentActivity.this).load(songs.getUrlPic()).into(songImage);
        Picasso.with(CommentActivity.this).load(songs.getUrlPic()).into(songLargeImage);
        songTitle.setText(songs.getTitle());
        songArtist.setText(songs.getArtist());

        getSongCommentById(songs.getId());

    }

    /**
     * 根据歌曲Id获取该歌曲评论
     * @param id
     */
    public void getSongCommentById(int id){
        final GsonRequest<List<Comments>> request = TingAPI.getCommentsRequest(id);

        final Response.Listener<List<Comments>> response = new Response.Listener<List<Comments>>() {
            @Override
            public void onResponse(List<Comments> comments) {
                recyclerView.setAdapter(new CommentAdapter(CommentActivity.this,comments,songs));
                commentSwipe.setRefreshing(false);
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu,menu);

        return true;
    }

}