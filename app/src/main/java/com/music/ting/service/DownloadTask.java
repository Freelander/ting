package com.music.ting.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.music.ting.db.ThreadDAO;
import com.music.ting.db.ThreadDAOImpl;
import com.music.ting.model.FileInfo;
import com.music.ting.model.ThreadInfo;
import com.music.ting.ui.fragment.DownLoadFragment;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by Jun on 2015/6/4.
 * 下载任务类
 */
public class DownloadTask {
    private Context context = null;
    private FileInfo mFileInfo = null;
    private ThreadDAO mDao = null;
    private ThreadInfo mThreadInfo = null;
    private int mFinished = 0;
    public boolean isPause = false;
    public boolean isDelete = false;
    private Intent intent = new Intent();

    public DownloadTask(Context context, FileInfo mFileInfo) {
        this.context = context;
        this.mFileInfo = mFileInfo;
        mDao = new ThreadDAOImpl(context);

    }

    public void download(){
        //读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDao.getTreads(mFileInfo.getUrl());
        if(threadInfos.size() == 0){
            //初始化线程信息
            mThreadInfo = new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),0);
        }else{
            mThreadInfo = threadInfos.get(0);
        }

        //创建子线程进行下载
        new DownloadThread(mFileInfo).start();

    }

    public void cancelDownload(){
        //删除线程信息
        mDao.deleteThread(mThreadInfo.getUrl(),mThreadInfo.getId());
        File file = new File(DownloadService.DOWLOAD_PATH,mFileInfo.getfileName());
        //删除文件
        if(file.exists()){
            file.delete();
        }
        intent.setAction(DownloadService.ACTION_DELETE);
        context.sendBroadcast(intent);

    }



    /**
     * 下载线程
     */
    class DownloadThread extends Thread{

        private FileInfo mFileInfo = null;
        public DownloadThread(FileInfo mFileInfo){
           this.mFileInfo = mFileInfo;
        }

        public void run(){
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            //向数据库输入线程信息
            Log.i("TAG","isExists:"+mDao.isExists(mThreadInfo.getUrl(),mThreadInfo.getId()));

            if(!mDao.isExists(mThreadInfo.getUrl(),mThreadInfo.getId())){//如果不存在
                mDao.insertThread(mThreadInfo);
            }
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                //Range: bytes= ? // 请求内容字节范围
                conn.setRequestProperty("Range","bytes="+start+"-"+mThreadInfo.getEnd());

                //设置文件写入位置
                File file = new File(DownloadService.DOWLOAD_PATH,mFileInfo.getfileName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);//设置文件写入的位置
                //intent = new Intent();
                mFinished += mThreadInfo.getFinished();
                //开始下载
                Log.i("TAG","conn:"+conn.getResponseCode()+""+HttpStatus.SC_PARTIAL_CONTENT);
                if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT){//网络正常
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024*4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1){//判定数据是否读取结束
                        //写入文件
                        raf.write(buffer,0,len);
                        //把下载进度发送广播到Activity
                        mFinished += len;
                        if(System.currentTimeMillis() - time > 500){//延迟发送广播，因为这个循环太快,减少UI负载
                            time = System.currentTimeMillis();
                            intent.setAction(DownloadService.ACTION_UPDATE);
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            intent.putExtra("fileInfo",mFileInfo);
                            context.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if(isPause){
                            mDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(),mFinished);
                            return;//结束
                        }

                    }
                    //删除线程信息
                    mDao.deleteThread(mThreadInfo.getUrl(),mThreadInfo.getId());
                    intent.setAction(DownloadService.ACTION_OK);
                    context.sendBroadcast(intent);
                    DownLoadFragment.isDownLoad = false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                conn.disconnect();
                try {
                    input.close();
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        }
    }
}
