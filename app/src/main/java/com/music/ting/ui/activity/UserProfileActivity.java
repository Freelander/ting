package com.music.ting.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.music.ting.R;
import com.music.ting.adapter.FragmentAdapter;
import com.music.ting.data.GsonRequest;
import com.music.ting.data.RequestManager;
import com.music.ting.model.user.UserComments;
import com.music.ting.model.user.UserInfo;
import com.music.ting.model.user.UserLikeSongs;
import com.music.ting.model.user.UserShareSongs;
import com.music.ting.ui.fragment.LikeSongsListFragment;
import com.music.ting.ui.fragment.ShareSongsListFragment;
import com.music.ting.ui.fragment.UserCommentsFragment;
import com.music.ting.ui.widget.RoundImageView;
import com.music.ting.utils.TingAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Jun on 2015/6/20.
 */
public class UserProfileActivity extends SwipeBackActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.user_image)
    RoundImageView userImage;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.user_bio)
    TextView userBio;
    @InjectView(R.id.songs_count)
    TextView songsCount;
    @InjectView(R.id.like_count)
    TextView likeCount;
    @InjectView(R.id.comments_count)
    TextView commentsCount;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.user_options_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.user_email)
    TextView userEmail;

    public UserInfo mUserInfo;
    private UserLikeSongs userLikeSongs;
    private UserShareSongs userShareSongs;
    private UserComments userComments;
    private int userId=0;
    private SwipeBackLayout mSwipeBackLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.inject(this);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));


        initView();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setupViewPage();


        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);


//        getUserShareSons();
//
//        getUserLikeSongs();



    }

    public void initView(){
        Intent intent = getIntent();
        mUserInfo = intent.getParcelableExtra("UserInfo");
        toolbar.setTitle(mUserInfo.getUser().getName());
        toolbar.setTitleTextColor(Color.WHITE);
        userName.setText(mUserInfo.getUser().getName());
        userBio.setText(mUserInfo.getUser().getBio());
        userEmail.setText(mUserInfo.getUser().getEmail());
        Picasso.with(UserProfileActivity.this)
                .load(mUserInfo.getUser().getAvatar().geturl())
                .placeholder(R.drawable.ic_default_pic)
                .error(R.drawable.ic_default_pic)
                .into(userImage);
    }

    public void setupViewPage() {
        List<String> titles = new ArrayList<>();
        titles.add("Songs");
        titles.add("Likes");
        titles.add("Comments");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShareSongsListFragment(mUserInfo.getUser().getId()));
        fragments.add(new LikeSongsListFragment(mUserInfo.getUser().getId()));
        fragments.add(new UserCommentsFragment(mUserInfo.getUser().getId()));
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);

    }

    public void getUserLikeSongs(){
        final GsonRequest<UserLikeSongs> request = TingAPI.getUserLikeSongs(mUserInfo.getUser().getId());

        final Response.Listener<UserLikeSongs> response = new Response.Listener<UserLikeSongs>() {
            @Override
            public void onResponse(UserLikeSongs songs) {
                userLikeSongs = songs;
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, null);
    }

    public void getUserShareSons(){
        final GsonRequest<UserShareSongs> request = TingAPI.getUserShareSongs(mUserInfo.getUser().getId());

        final Response.Listener<UserShareSongs> response = new Response.Listener<UserShareSongs>() {
            @Override
            public void onResponse(UserShareSongs songs) {
                userShareSongs = songs;
            }
        };
        request.setSuccessListener(response);
        RequestManager.addRequest(request, null);
    }

    public void getUserComments(){

    }

}
