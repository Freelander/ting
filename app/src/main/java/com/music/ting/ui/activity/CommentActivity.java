package com.music.ting.ui.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.adapter.CommentAdapter;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.Comments;
import com.music.ting.model.FileInfo;
import com.music.ting.model.OnLineSongs;
import com.music.ting.model.Songs;
import com.music.ting.service.DownloadService;
import com.music.ting.service.MusicService;
import com.music.ting.service.MusicService.MusicBinder;
import com.music.ting.ui.fragment.DownLoadFragment;
import com.music.ting.utils.MediaUtils;
import com.music.ting.utils.TingAPI;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jun on 2015/5/20.
 *
 */
public class CommentActivity extends BaseActivity {

    private final static String TAG = "Ting";

    public Songs songs = new Songs();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout commentSwipe;
    private Toolbar toolbar;
    private ShareActionProvider shareActionProvider;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView songImage;//歌曲封面
    private ImageView songLargeImage;//歌曲封面大图
    private TextView songTitle, songArtist;//歌名，歌手
    private ImageView playImage;//播放按钮
    private ImageView repeatImage;//循环按钮
    private ImageView rewImage;//后退按钮
    private ImageView playBtnImage;//播放按钮
    private ImageView fwdImage;//快进按钮
    private ImageView shuffleImage;//随机播放按钮
    private TextView tvTimeElapsed;//当前播放位置
    private TextView tvDuration;//当前歌曲最大长度
    private SeekBar songSeekBar;//歌曲进度条

    public String playUrl = "";
    private int currentPosition;//当前播放歌曲位置
    private int currentMax;//歌曲最大长度

    private MusicBinder musicBinder;
    private ProgressReceiver progressReceiver;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void connectToMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }


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

        connectToMusicService();//绑定服务
        getData();

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelExpanded(View panel) {
                playImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                playImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelAnchored(View panel) {
            }

            @Override
            public void onPanelHidden(View panel) {
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) { //当用户向上滑时，隐藏SlidingUpPanelLayout
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {//当用户向上滑时，显示SlidingUpPanelLayout
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

        // 添加来电监听事件
        TelephonyManager telManager = (TelephonyManager) getSystemService(
                Context.TELEPHONY_SERVICE); // 获取系统服务
        telManager.listen(new MobliePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);

    }

    public void onResume() {
        Log.v(TAG, "OnResume register Progress Receiver");
        super.onResume();
        registerReceiver();
        if (musicBinder != null) {
            if (musicBinder.isPlaying()) {
                playImage.setImageResource(R.drawable.ic_pause_black_large);
            } else {
                playImage.setImageResource(R.drawable.ic_play_black_round);
            }
            musicBinder.notifyActivity();
        }
    }

    public void onPause() {
        Log.v(TAG, "OnPause unregister Progress Receiver");
        super.onPause();
        unregisterReceiver(progressReceiver);
    }

    public void onStop() {
        Log.v(TAG, "OnStop");
        super.onStop();
    }

    public void onDestroy() {
        Log.v(TAG, "OnDestroy");
        super.onDestroy();
        if (musicBinder != null) {
            unbindService(serviceConnection);
        }
    }

    public void getData() {

        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        commentSwipe = (SwipeRefreshLayout) this.findViewById(R.id.comment_swipe);
        songImage = (ImageView) findViewById(R.id.song_image);
        songLargeImage = (ImageView) findViewById(R.id.song_image_large);
        songTitle = (TextView) findViewById(R.id.song_title);
        songArtist = (TextView) findViewById(R.id.song_artist);
        playImage = (ImageView) findViewById(R.id.play);
        repeatImage = (ImageView) findViewById(R.id.repeat);
        playBtnImage = (ImageView) findViewById(R.id.play_btn);
        fwdImage = (ImageView) findViewById(R.id.fwd);
        shuffleImage = (ImageView) findViewById(R.id.shuffle);
        tvTimeElapsed = (TextView) findViewById(R.id.current_position);
        tvDuration = (TextView) findViewById(R.id.current_max);
        songSeekBar = (SeekBar) findViewById(R.id.seekBar);

        songSeekBar.setOnSeekBarChangeListener(new SongSeekBarListener());

        //绑定点击事件
        ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
        playImage.setOnClickListener(viewOnClickListener);
        playBtnImage.setOnClickListener(viewOnClickListener);

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

        //根据歌曲Id获取歌曲评论信息
        getSongCommentById(songs.getId());

        getSongPlayUrlBysId(songs.getsId());

    }

    private void play(final String url) {
        if (musicBinder.isPlaying()) {
            musicBinder.stopPlay();
            playImage.setImageResource(R.drawable.ic_play_black_round);
            playBtnImage.setImageResource(R.drawable.ic_fab_play_btn_normal);
        } else {
            musicBinder.startPlay(url, currentPosition);
            playImage.setImageResource(R.drawable.ic_pause_black_large);
            playBtnImage.setImageResource(R.drawable.ic_fab_pause_btn_normal);
        }
    }

    /**
     * 电话监听类
     */
    private boolean  phoneState = false; //电话状态
    private class MobliePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 挂机状态
                    if(phoneState){
                        play(playUrl);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:   //通话状态
                case TelephonyManager.CALL_STATE_RINGING:   //响铃状态
                    if(musicBinder.isPlaying()){ //判断歌曲是否在播放
                        musicBinder.stopPlay();
                        phoneState = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 点击监听器
     */
    class ViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play:
                    play(playUrl);
                    break;
                case R.id.play_btn:
                    play(playUrl);
                    break;
            }
        }
    }

    /**
     * 进度条监听器
     */
    private class SongSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                musicBinder.changProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * 根据歌曲Id获取该歌曲评论
     *
     * @param id
     */
    public void getSongCommentById(int id) {
        final GsonRequest<List<Comments>> request = TingAPI.getCommentsRequest(id);

        final Response.Listener<List<Comments>> response = new Response.Listener<List<Comments>>() {
            @Override
            public void onResponse(List<Comments> comments) {
                recyclerView.setAdapter(new CommentAdapter(CommentActivity.this, comments, songs));
                commentSwipe.setRefreshing(false);
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, id);
    }

    /**
     * 根据歌曲播放Id获取歌曲播放地址url
     *
     * @param sId
     * @return
     */
    public void getSongPlayUrlBysId(long sId) {

        final GsonRequest<OnLineSongs> request = TingAPI.getOnLineSongsRequest(sId);

        final Response.Listener<OnLineSongs> response = new Response.Listener<OnLineSongs>() {
            @Override
            public void onResponse(OnLineSongs onLineSongs) {
                playUrl = onLineSongs.getSongurl();
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, sId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                /**
                 * 分享
                 */
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, songs.getTitle() + " by "
                        + songs.getArtist() + "\n" + songs.getContent() +
                        " 分享来自 http://tinger.herokuapp.com/ ");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
            case R.id.action_download:
                final FileInfo file = new FileInfo(0, playUrl,songs.getArtist() + " - " +
                        songs.getTitle()+".MP3", 0, 0);
                Intent intent = new Intent(CommentActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", file);
                startService(intent);
                DownLoadFragment.isDownLoad = true;
                Toast.makeText(CommentActivity.this,"加入下载队列",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }




    /**
     * 注册广播
     */
    private void registerReceiver() {
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
        intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
        intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
        registerReceiver(progressReceiver, intentFilter);
    }


    /**
     * 接收广播类
     */
    public class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MusicService.ACTION_UPDATE_PROGRESS.equals(action)) {
                int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
                if (progress > 0) {
                    currentPosition = progress;
                    Log.i(TAG,progress+"");
                    songSeekBar.setProgress(progress / 1000);
                    tvTimeElapsed.setText(MediaUtils.formatDuration(progress));
                }
            } else if (MusicService.ACTION_UPDATE_DURATION.equals(action)) {
                currentMax = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
                int max = currentMax / 1000;
                songSeekBar.setMax(max);
                tvDuration.setText(MediaUtils.formatDuration(currentMax));
            }
        }
    }

}