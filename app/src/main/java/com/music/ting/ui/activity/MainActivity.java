package com.music.ting.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.music.ting.R;
import com.music.ting.adapter.DrawerListAdapter;
import com.music.ting.model.DrawerListBean;
import com.music.ting.ui.fragment.LocalSongsFragment;
import com.music.ting.ui.fragment.SongsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun on 2015/5/10.
 */
public class MainActivity extends BaseActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private LinearLayout drawerLayout;//抽屉布局

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        SongsListFragment fragment = new SongsListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment)
                .show(fragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.ting_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,
                R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        drawerLayout = (LinearLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.ting_drawer_list);
        final DrawerListAdapter adapter = new DrawerListAdapter(this,R.layout.item_navigation_list,initDrawerList());
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0: //立即欣赏
                        SongsListFragment fragment = new SongsListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                                .show(fragment).commit();
                        mDrawerLayout.closeDrawer(drawerLayout);
                        break;
                    case 1: //我的乐库
                        LocalSongsFragment localSongsFragment = new LocalSongsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, localSongsFragment)
                                .show(localSongsFragment).commit();
                        mDrawerLayout.closeDrawer(drawerLayout);
                        break;

                }
                //记录当前选中的位置
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 抽屉导航 ListView 数据
     * @return
     */
    public List<DrawerListBean> initDrawerList(){
        List<DrawerListBean> drawerList = new ArrayList<DrawerListBean>();
        DrawerListBean drawerListBean = new DrawerListBean("立即欣赏",R.drawable.ting_bg_selector);
        drawerList.add(drawerListBean);
        DrawerListBean drawerListBean1 = new DrawerListBean("我的乐库",R.drawable.music_bg_selector);
        drawerList.add(drawerListBean1);
        DrawerListBean drawerListBean2 = new DrawerListBean("我的分享",R.drawable.share_bg_selector);
        drawerList.add(drawerListBean2);
        DrawerListBean drawerListBean3 = new DrawerListBean("我的喜爱",R.drawable.like_bg_selector);
        drawerList.add(drawerListBean3);
        return drawerList;
    }



}
