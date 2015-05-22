package com.music.ting;

import android.app.Application;

/**
 * Created by Jun on 2015/5/10.
 */
public class Ting extends Application {

    private static Ting mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Ting getInstance(){
        return mContext;
    }
}
