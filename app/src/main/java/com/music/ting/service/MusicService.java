package com.music.ting.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.music.ting.model.LocalSongs;
import com.music.ting.utils.MediaUtils;

import java.io.IOException;
import java.util.List;


/**
 * Created by Jun on 2015/5/30.
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    private List<LocalSongs> localSongsList;

    private Binder musicBinder = new MusicBinder();

    private int currentMusic;//当前播放歌曲
    private int currentPosition;//当前播放到什么位置

    private static final String TAG = "com.music.ting.service.MusicService";

    private static final int updateProgress = 1; //更新进度条
    private static final int updateCurrentMusic = 2; //更新当前播放歌曲
    private static final int updateDuration = 3; //更新当前播放时间


    public static final String ACTION_UPDATE_PROGRESS = "com.music.ting.UPDATE_PROGRESS";
    public static final String ACTION_UPDATE_DURATION = "com.music.ting.UPDATE_DURATION";
    public static final String ACTION_UPDATE_CURRENT_MUSIC = "com.music.ting.UPDATE_CURRENT_MUSIC";

    private int currentMode = 3;//默认播放模式

    public static final String[] MODE_DESC = { //模式描述
            "Single Loop", "List Loop", "Random", "Sequence"};

    public static final int MODE_ONE_LOOP = 0;//单曲循环
    public static final int MODE_ALL_LOOP = 1;//全部循环
    public static final int MODE_RANDOM = 2;//随机播放
    public static final int MODE_SEQUENCE = 3;//按顺序播放

    private Notification notification;//通知

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case updateProgress:
                    toUpdateProgress();
                    break;
                case updateDuration:
                    toUpdateDuration();
                    break;
                case updateCurrentMusic:
                    toUpdateCurrentMusic();
                    break;
            }
        }
    };

    private void toUpdateCurrentMusic() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
        intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC,currentMusic);
        sendBroadcast(intent);//通过广播发送
    }

    private void toUpdateDuration() {
        if(mediaPlayer != null){
            int duration = mediaPlayer.getDuration();
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_DURATION);
            intent.putExtra(ACTION_UPDATE_DURATION,duration);
            sendBroadcast(intent);
        }
    }

    private void toUpdateProgress() {
        if(mediaPlayer != null){
            int progress = mediaPlayer.getCurrentPosition();
            Intent intent = new Intent();
            intent.putExtra(ACTION_UPDATE_PROGRESS,progress);
            sendBroadcast(intent);
            handler.sendEmptyMessageDelayed(updateProgress,1000);
        }
    }

    @Override
    public void onCreate() {
        initMediaPlayer();
        localSongsList = MediaUtils.getLocalSongs(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    /**
     * 初始化 MediaPlayer
     */
    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.seekTo(currentPosition);
                Log.v(TAG, "[OnPreparedListener] Start at " + currentMusic + " in mode " +
                        currentMode + ", currentPosition : " + currentPosition);
                handler.sendEmptyMessage(updateDuration);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isPlaying){
                    Log.v(TAG, "[OnCompletionListener] On Completion at " + currentMusic);
                    switch (currentMode){
                        case MODE_ONE_LOOP:
                            Log.v(TAG, "[Mode] currentMode = MODE_ONE_LOOP.");
                            mediaPlayer.start();
                            break;
                        case MODE_ALL_LOOP:
                            Log.v(TAG, "[Mode] currentMode = MODE_ALL_LOOP.");
                            play((currentMusic + 1) % localSongsList.size(), 0);
                            break;
                        case MODE_RANDOM:
                            Log.v(TAG, "[Mode] currentMode = MODE_RANDOM.");
                            play(getRandomPosition(), 0);
                            break;
                        case MODE_SEQUENCE:
                            Log.v(TAG, "[Mode] currentMode = MODE_SEQUENCE.");
                            if(currentMusic < localSongsList.size() - 1){
                                playNext();
                            }
                            break;
                        default:
                            Log.v(TAG, "No Mode selected! How could that be ?");
                            break;
                    }
                    Log.v(TAG, "[OnCompletionListener] Going to play at " + currentMusic);
                }
            }
        });
    }

    //记录下当前播放歌曲位置
    private void setCurrentMusic(int pCurrentMusic){
        currentMusic = pCurrentMusic;
        handler.sendEmptyMessage(updateCurrentMusic);
    }

    //在歌曲列表长度中随机生成一个位置
    private int getRandomPosition(){
        int random = (int)(Math.random() * (localSongsList.size() - 1));
        return random;
    }

    private void play(int currentMusic, int pCurrentPosition){
        currentPosition = pCurrentPosition;
        setCurrentMusic(currentMusic);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(localSongsList.get(currentMusic).getUrl());
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.v(TAG, "[Play] Start Preparing at " + currentMusic);
        mediaPlayer.prepareAsync();
        handler.sendEmptyMessage(updateProgress);

        isPlaying = true;
    }

    private void stop(){
        mediaPlayer.stop();
        isPlaying = false;
    }

    private void playNext(){
        switch (currentMode){
            case MODE_ONE_LOOP://单曲循环模式
                play(currentMusic, 0);
                break;
            case MODE_ALL_LOOP://全部歌曲循环模式
                if(currentMusic + 1 == localSongsList.size()){//最后一首
                    play(0, 0);
                }else{
                    play(currentMusic + 1, 0);
                }
                break;
            case MODE_SEQUENCE://顺序播放
                if(currentMusic + 1 == localSongsList.size()){
                    Toast.makeText(this,"没有歌曲了",Toast.LENGTH_LONG).show();
                }else{
                    play(currentMusic + 1, 0);
                }
                break;
            case MODE_RANDOM://随机播放模式
                play(getRandomPosition(), 0);
                break;
        }
    }

    private void playPrevious(){
        switch (currentMode){
            case MODE_ONE_LOOP://单曲循环模式
                play(currentMusic, 0);
                break;
            case MODE_ALL_LOOP://全部歌曲循环模式
                if(currentMusic - 1 < 0){//第一首
                    play(localSongsList.size() - 1, 0);
                }else{
                    play(currentMusic - 1, 0);
                }
                break;
            case MODE_SEQUENCE://顺序播放
                if(currentMusic - 1 < 0){
                    Toast.makeText(this,"这是第一首歌",Toast.LENGTH_LONG).show();
                }else{
                    play(currentMusic - 1, 0);
                }
                break;
            case MODE_RANDOM://随机播放模式
                play(getRandomPosition(), 0);
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public class MusicBinder extends Binder{

        public void startPlay(int currentMusic, int currentPosition){
            play(currentMusic,currentPosition);
        }

        public void stopPlay(){
            stop();
        }

        public void toPlayNext(){
            playNext();
        }

        public void toPlayPrevious(){
            playPrevious();
        }

        /**
         * MODE_ONE_LOOP = 1;
         * MODE_ALL_LOOP = 2;
         * MODE_RANDOM = 3;
         * MODE_SEQUENCE = 4;
         */
        public void  changeMode(){
            currentMode = (currentMode + 1) % 4;
            Log.v(TAG, "[NatureBinder] changeMode : " + currentMode);
            Toast.makeText(MusicService.this, MODE_DESC[currentMode], Toast.LENGTH_SHORT).show();
        }

        public int getCurrentMode(){
            return currentMode;
        }

        /**
         * 判断是否在播放
         */
        public boolean isPlaying(){
            return isPlaying;
        }

        /**
         * 通知Activity更新当前歌曲和时间
         */
        public void notifyActivity(){
            toUpdateCurrentMusic();
            toUpdateDuration();
        }

        /**
         * 进度条改变
         * @param progress
         */
        public void changProgress(int progress){
            if(mediaPlayer != null){
                currentPosition = progress * 1000;
                if(isPlaying){
                    mediaPlayer.seekTo(currentPosition);
                }else{
                    play(currentMusic,currentPosition);
                }
            }
        }

    }
}
