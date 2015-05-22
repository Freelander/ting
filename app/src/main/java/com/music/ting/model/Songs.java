package com.music.ting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jun on 2015/5/11.
 * 歌曲信息实体类
 */
public class Songs implements Parcelable {

    /**
     * 歌曲Id
     */
    private int id;
    /**
     * 歌曲来源Id
     */
    @SerializedName("s_id")
    private Long sId;
    /**
     * 歌曲名称
     */
    private String title;
    /**
     * 歌手名称
     */
    private String artist;
    /**
     * 歌曲封面图片URL
     */
    @SerializedName("pic")
    private String urlPic;
    /**
     * 歌曲内容
     */
    private String content;
    /**
     * 分享该歌曲的用户Id
     */
    @SerializedName("user_id")
    private int userId;
    /**
     * 评论该歌曲数目
     */
    @SerializedName("comments_count")
    private int commentsCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeValue(this.sId);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.urlPic);
        dest.writeString(this.content);
        dest.writeInt(this.userId);
        dest.writeInt(this.commentsCount);
    }

    public Songs() {
    }

    private Songs(Parcel in) {
        this.id = in.readInt();
        this.sId = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.artist = in.readString();
        this.urlPic = in.readString();
        this.content = in.readString();
        this.userId = in.readInt();
        this.commentsCount = in.readInt();
    }

    public static final Parcelable.Creator<Songs> CREATOR = new Parcelable.Creator<Songs>() {
        public Songs createFromParcel(Parcel source) {
            return new Songs(source);
        }

        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };
}
