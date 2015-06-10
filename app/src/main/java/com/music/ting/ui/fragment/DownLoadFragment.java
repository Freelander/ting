package com.music.ting.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.music.ting.R;
import com.music.ting.model.FileInfo;
import com.music.ting.service.DownloadService;

/**
 * Created by Jun on 2015/6/6.
 */
public class DownLoadFragment extends Fragment {

    private View view;

    private ImageView downLoadControl, cancelDownLoad;
    private NumberProgressBar progressBar;
    private TextView songTitle;
    public static boolean isDownLoad = false;
    private boolean isPause = true;
    private FileInfo mFileInfo;
    private ProgressReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (isDownLoad) {
            view = inflater.inflate(R.layout.fragment_download, container, false);
            registerReceiver();

            downLoadControl = (ImageView) view.findViewById(R.id.download_control);
            cancelDownLoad = (ImageView) view.findViewById(R.id.delete_download);
            progressBar = (NumberProgressBar) view.findViewById(R.id.progressBar);
            songTitle = (TextView) view.findViewById(R.id.song_title);

            ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
            downLoadControl.setOnClickListener(viewOnClickListener);
            cancelDownLoad.setOnClickListener(viewOnClickListener);

        } else {
            view = inflater.inflate(R.layout.fragment_not_download, container, false);
        }





        return view;
    }

    /**
     * 注册广播
     */
    public void registerReceiver(){

        mReceiver = new ProgressReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_DELETE);
        filter.addAction(DownloadService.ACTION_FILEINFO);
        filter.addAction(DownloadService.ACTION_OK);
        getActivity().registerReceiver(mReceiver, filter);
    }


    /**
     * 广播接收器
     */
    public class ProgressReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadService.ACTION_UPDATE.equals(action)) {
                int finished = intent.getIntExtra("finished", 0);
                progressBar.setProgress(finished);
                mFileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                songTitle.setText(mFileInfo.getfileName());
            } else if (DownloadService.ACTION_DELETE.equals(action)) {
                isDownLoad = false;
                stopService();
                DownLoadFragment loadFragment = new DownLoadFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,loadFragment).
                        show(loadFragment).commit();
            }else if(DownloadService.ACTION_OK.equals(action)){
                isDownLoad = false;
                stopService();
                DownLoadFragment loadFragment = new DownLoadFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,loadFragment).
                        show(loadFragment).commit();

            }
        }
    }

    /**
     * 暂停服务
     */
    public void stopService(){
        Intent intent = new Intent(view.getContext(),DownloadService.class);
        view.getContext().stopService(intent);

    }

    class ViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(view.getContext(), DownloadService.class);
            switch (v.getId()) {
                case R.id.download_control:
                    if (isPause) {
                        downLoadControl.setImageResource(R.drawable.ic_fa_arrow_down);
                        intent.setAction(DownloadService.ACTION_STOP);
                        intent.putExtra("fileInfo", mFileInfo);
                        getActivity().startService(intent);
                        isPause = false;
                    } else {
                        downLoadControl.setImageResource(R.drawable.ic_fa_pause);
                        intent.setAction(DownloadService.ACTION_START);
                        intent.putExtra("fileInfo", mFileInfo);
                        getActivity().startService(intent);
                        isPause = true;
                    }
                    break;
                case R.id.delete_download:
                    stopService();
                    intent.setAction(DownloadService.ACTION_DELETE);
                    intent.putExtra("fileInfo", mFileInfo);
                    getActivity().startService(intent);
                    break;

            }
        }
    }


}
