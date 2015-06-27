package com.music.ting.utils;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.music.ting.Ting;
import com.music.ting.data.GsonRequest;
import com.music.ting.model.Comments;
import com.music.ting.model.OnLineSongs;
import com.music.ting.model.Songs;
import com.music.ting.model.user.UserComments;
import com.music.ting.model.user.UserInfo;
import com.music.ting.model.user.UserLikeSongs;
import com.music.ting.model.user.UserShareSongs;

import java.util.List;

/**
 * Created by Jun on 2015/5/12.
 */
public class TingAPI {

    private final static String TingApi = "http://tinger.herokuapp.com/api";
    /**
     * 获取用户信息
     */
    private final static String TingUserApi = TingApi + "/users";

    private final static String TingUserName = TingUserApi + "?username=";

    private final static String TingUserId = TingUserApi + "?id=";

    private final static String TingUserShareSongs = "&include=songs";

    private final static String TingUserLikeSongs = "&include=liked_songs";

    private final static String TingUserComments = "&include=comments";

    /**
     * 获取歌曲信息
     */
    private final static String TingSongsApi = TingApi + "/songs";

    private final static String TingSongCount = TingSongsApi + "?songs_count=";
    /**
     * 根据歌曲Id获取评论
     */
    private final static String TingCommentApi = TingApi + "/comments?song_id=";
    /**
     * 根据歌曲s_id获取该歌曲的播放地址
     * 例：http://inmusic.sinaapp.com/xiami_api/ + s_id(歌曲虾米ID)
     */
    private final static String TingSongsAddressApi = " http://inmusic.sinaapp.com/xiami_api/";



    /**
     * 根据用户名获取用户信息
     * @param userName 用户名
     * @return
     */
    public static GsonRequest<UserInfo> getUserInfoRequest(String userName){
        final String userUrl = TingUserName + userName;
        return new GsonRequest<UserInfo>(userUrl, UserInfo.class,
                null, buildDefaultErrorListener());
    }

    /**
     * 根据用户Id获取用户信息
     * @param userId 用户Id
     * @return
     */
    public static GsonRequest<UserInfo> getUserInfoRequestById(int userId){
        final String userUrl = TingUserId + userId;
        return new GsonRequest<UserInfo>(userUrl, UserInfo.class,
                null, buildDefaultErrorListener());
    }

    /**
     * 获取用户喜欢歌曲列表
     * @param userId
     * @return
     */
    public static GsonRequest<UserLikeSongs> getUserLikeSongs(int userId){
        final String userLikeSongUrl = TingUserId + userId + TingUserLikeSongs;
        return new GsonRequest<UserLikeSongs>(userLikeSongUrl, UserLikeSongs.class,
                null,buildDefaultErrorListener());
    }

    /**
     * 获取用户分享歌曲
     * @param userId
     * @return
     */
    public static GsonRequest<UserShareSongs> getUserShareSongs(int userId){
        final String userShareSongUrl = TingUserId + userId + TingUserShareSongs;
        return new GsonRequest<UserShareSongs>(userShareSongUrl, UserShareSongs.class,
                null,buildDefaultErrorListener());
    }

    /**
     * 获取用户评论信息
     * @param userId
     * @return
     */
    public static GsonRequest<UserComments> getUserComments(int userId){
        final String userCommentsUrl = TingUserId + userId + TingUserComments;
        return new GsonRequest<UserComments>(userCommentsUrl, UserComments.class,
                null,buildDefaultErrorListener());
    }

     /**
     * 获取歌曲列表集合
     * @param songCount 集合长度
     * @return
     */
    public static GsonRequest<List<Songs>> getSongsRequest(int songCount){
        final String songsUrl;
        if(songCount == 0){//获取所有歌曲
            songsUrl = TingSongsApi;
        }else{
            songsUrl = TingSongCount + songCount;
        }
        return new GsonRequest<List<Songs>>(songsUrl, buildDefaultErrorListener());
    }

    /**
     * 获取歌曲被评论列表集合
     * @param songId
     * @return
     */
    public static GsonRequest<List<Comments>> getCommentsRequest(int songId){
        final String commentUrl = TingCommentApi + songId;
        return new GsonRequest<List<Comments>>(commentUrl,
                Comments.class,buildDefaultErrorListener());
    }

    /**
     * 根据歌曲播放Id获取歌曲信息
     * @param s_Id
     * @return
     */
    public static GsonRequest<OnLineSongs> getOnLineSongsRequest(long s_Id){
        final String onLineSongsUrl = TingSongsAddressApi + s_Id;
        return new GsonRequest<OnLineSongs>(onLineSongsUrl, OnLineSongs.class,
                null, buildDefaultErrorListener());
    }


    /**
     * 响应失败
     * @return
     */
    private static Response.ErrorListener buildDefaultErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ting.getInstance(),"网络出现问题",Toast.LENGTH_LONG).show();
               // Log.e("Ting",error.getMessage());
            }
        };
    }
}
