package com.music.ting.ui.fragment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.ting.R;
import com.music.ting.adapter.LocalSongsAdapter;
import com.music.ting.model.LocalSongs;
import com.music.ting.service.MusicService;
import com.music.ting.service.MusicService.MusicBinder;
import com.music.ting.utils.MediaUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * Created by Jun on 2015/5/27.
 *
 */
public class LocalSongsFragment extends Fragment {

    private ListView localListView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private View view;
    private List<LocalSongs> localSongsList;
    private int currentMusic;//当前播放歌曲
    private int currentPosition;//当前播放歌曲位置
    private int currentMax;
    private final static String TAG="com.music.ting.ui.fragment.LocalSongsFragment";

    private ImageView songImage;//歌曲封面
    private ImageView songLargeImage;//歌曲封面大图
    private TextView songTitle,songArtist;//歌名，歌手
    private ImageView playImage;//播放按钮
    private ImageView repeatImage;//循环按钮
    private ImageView rewImage;//后退按钮
    private ImageView playBtnImage;//播放按钮
    private ImageView fwdImage;//快进按钮
    private ImageView shuffleImage;//随机播放按钮
    private TextView tvTimeElapsed;//当前播放位置
    private TextView tvDuration;//当前歌曲最大长度
    private SeekBar songSeekBar;//歌曲进度条

    private MusicBinder musicBinder;
    private ProgressReceiver progressReceiver;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicBinder) service;
        }
    };
    private void connectToMusicService(){
        Intent intent = new Intent(view.getContext(), MusicService.class);
        getActivity().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_songs,container,false);
        initView();

        localSongsList = MediaUtils.getLocalSongs(view.getContext());
        connectToMusicService();

        localListView.setAdapter(new LocalSongsAdapter(view.getContext(),
                R.layout.item_songs_list, localSongsList));
        localListView.setOnScrollListener(new ListViewOnScrollListener());
        localListView.setOnItemClickListener(new ListViewOnItemClickListener());

        Bitmap bitmap = MediaUtils.getArtwork(view.getContext(), localSongsList.get(0).getId(),
                localSongsList.get(0).getAlbumId(), true, true);
        songImage.setImageBitmap(bitmap);
        songTitle.setText(localSongsList.get(0).getTitle());
        songArtist.setText(localSongsList.get(0).getArtist());
        songLargeImage.setImageBitmap(bitmap);


        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                playImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelExpanded(View view) {
                playImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        // 添加来电监听事件
        TelephonyManager telManager = (TelephonyManager) getActivity().getSystemService(
                Context.TELEPHONY_SERVICE); // 获取系统服务
        telManager.listen(new MobliePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);

        return view;
    }


    public void onResume(){
        Log.v(TAG, "OnResume register Progress Receiver");
        super.onResume();
        registerReceiver();
        if(musicBinder != null){
            if(musicBinder.isPlaying()){
                playImage.setImageResource(R.drawable.ic_pause_black_large);
            }else{
                playImage.setImageResource(R.drawable.ic_play_black_round);
            }
            musicBinder.notifyActivity();
        }
    }

    public void onPause(){
        Log.v(TAG, "OnPause unregister Progress Receiver");
        super.onPause();
        getActivity().unregisterReceiver(progressReceiver);
    }

    public void onStop(){
        Log.v(TAG, "OnStop");
        super.onStop();
    }

    public void onDestroy(){
        Log.v(TAG, "OnDestroy");
        super.onDestroy();
        if(musicBinder != null){
            getActivity().unbindService(serviceConnection);
        }
    }

    /**
     * ListView item 点击监听器
     */
    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            currentMusic = position;
            musicBinder.startPlay(currentMusic,0);
            if(musicBinder.isPlaying()){
                playImage.setImageResource(R.drawable.ic_pause_black_large);
                playBtnImage.setImageResource(R.drawable.ic_fab_pause_btn_normal);
            }
        }
    }

    /**
     * ListView 滚动监听器
     */
    private class ListViewOnScrollListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == 1){ //当用户向上滑时，隐藏SlidingUpPanelLayout
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }else if(scrollState == 2){//当用户向下滑时，显示SlidingUpPanelLayout
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    /**
     * 进度条监听器
     */
    private class SongSeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
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
     * 初始化控件
     */
    private void initView(){
        localListView = (ListView) view.findViewById(R.id.local_songs_list);
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.local_sliding_panel);

        songImage = (ImageView) view.findViewById(R.id.song_image);
        songLargeImage = (ImageView) view.findViewById(R.id.song_image_large);
        songTitle = (TextView) view.findViewById(R.id.song_title);
        songArtist = (TextView) view.findViewById(R.id.song_artist);
        playImage = (ImageView) view.findViewById(R.id.play);
        repeatImage = (ImageView) view.findViewById(R.id.repeat);
        playBtnImage = (ImageView) view.findViewById(R.id.play_btn);
        fwdImage = (ImageView) view.findViewById(R.id.fwd);
        rewImage = (ImageView) view.findViewById(R.id.rew);
        shuffleImage = (ImageView) view.findViewById(R.id.shuffle);
        tvTimeElapsed = (TextView) view.findViewById(R.id.current_position);
        tvDuration = (TextView) view.findViewById(R.id.current_max);
        songSeekBar = (SeekBar) view.findViewById(R.id.seekBar);

        songSeekBar.setOnSeekBarChangeListener(new SongSeekBarListener());

        ViewOnclickListener viewOnclickListener = new ViewOnclickListener();
        playImage.setOnClickListener(viewOnclickListener);
        playBtnImage.setOnClickListener(viewOnclickListener);
        fwdImage.setOnClickListener(viewOnclickListener);
        repeatImage.setOnClickListener(viewOnclickListener);
        shuffleImage.setOnClickListener(viewOnclickListener);
        rewImage.setOnClickListener(viewOnclickListener);

    }

    private void play(int position){
        if(musicBinder.isPlaying()){
            musicBinder.stopPlay();
            playImage.setImageResource(R.drawable.ic_play_black_round);
            playBtnImage.setImageResource(R.drawable.ic_fab_play_btn_normal);
        }else{
            musicBinder.startPlay(position,currentPosition);
            playImage.setImageResource(R.drawable.ic_pause_black_large);
            playBtnImage.setImageResource(R.drawable.ic_fab_pause_btn_normal);
        }
    }

    private class ViewOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play:
                    play(currentMusic);
                    break;
                case R.id.play_btn:
                    play(currentMusic);
                    break;
                case R.id.fwd:
                    musicBinder.toPlayNext();
                    break;
                case R.id.rew:
                    musicBinder.toPlayPrevious();
                    break;
                case R.id.repeat:
                    switch (musicBinder.getCurrentMode()){
                        case MusicService.MODE_ALL_LOOP:
                            repeatImage.setImageResource(R.drawable.ic_repeat_one_song_dark);
                            musicBinder.changeMode(MusicService.MODE_ONE_LOOP);
                            break;
                        case MusicService.MODE_ONE_LOOP:
                            repeatImage.setImageResource(R.drawable.ic_repeat_dark);
                            musicBinder.changeMode(MusicService.MODE_SEQUENCE);
                            break;
                        case MusicService.MODE_SEQUENCE:
                            repeatImage.setImageResource(R.drawable.ic_repeat_dark_selected);
                            musicBinder.changeMode(MusicService.MODE_ALL_LOOP);
                            break;
                        default:
                            repeatImage.setImageResource(R.drawable.ic_repeat_dark);
                            break;
                    }
                    break;
                case R.id.shuffle:
                    switch (musicBinder.getCurrentMode()){
                        case MusicService.MODE_SEQUENCE:
                            shuffleImage.setImageResource(R.drawable.ic_play_shuffle_orange);
                            musicBinder.changeMode(MusicService.MODE_RANDOM);
                            break;
                        case MusicService.MODE_RANDOM:
                            shuffleImage.setImageResource(R.drawable.ic_shuffle_dark);
                            musicBinder.changeMode(MusicService.MODE_SEQUENCE);
                            break;
                        default:
                            shuffleImage.setImageResource(R.drawable.ic_shuffle_dark);
                            break;
                    }
                    break;
            }
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
                        play(currentMusic);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:   //通话状态
                case TelephonyManager.CALL_STATE_RINGING:   //响铃状态
                    musicBinder.stopPlay();
                    phoneState = true;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver(){
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
        intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
        intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
        getActivity().registerReceiver(progressReceiver,intentFilter);
    }


    /**
     * 接收广播类
     */
    public class ProgressReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MusicService.ACTION_UPDATE_PROGRESS.equals(action)){
                int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
                if(progress > 0){
                    currentPosition = progress;
                    songSeekBar.setProgress(progress / 1000);
                    tvTimeElapsed.setText(MediaUtils.formatDuration(progress));
                }
            }else if(MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){
                currentMusic = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);
                LocalSongs localSongs = localSongsList.get(currentMusic);
                Bitmap bitmap = MediaUtils.getArtwork(view.getContext(),localSongs.getId(),
                        localSongs.getAlbumId(), true, true);
                songImage.setImageBitmap(bitmap);
                songTitle.setText(localSongs.getTitle());
                songArtist.setText(localSongs.getArtist());
                songLargeImage.setImageBitmap(bitmap);
            }else if(MusicService.ACTION_UPDATE_DURATION.equals(action)){
                currentMax = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
                int max = currentMax / 1000;
                songSeekBar.setMax(max);
                tvDuration.setText(MediaUtils.formatDuration(currentMax));
            }
        }
    }

}
