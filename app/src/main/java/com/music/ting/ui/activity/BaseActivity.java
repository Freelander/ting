package com.music.ting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.music.ting.R;
import com.music.ting.ui.fragment.DownLoadFragment;

/**
 * Created by Jun on 2015/5/15.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                Toast.makeText(this,"设置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_download_setting:
                DownLoadFragment downLoad = new DownLoadFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,downLoad).
                        show(downLoad).commit();
                break;
            case R.id.action_about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
