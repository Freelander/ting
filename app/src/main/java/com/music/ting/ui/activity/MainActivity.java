package com.music.ting.ui.activity;

import android.os.Bundle;

import com.music.ting.R;

/**
 * Created by Jun on 2015/5/10.
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
    }

}
