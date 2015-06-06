package com.music.ting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun on 2015/5/28.
 * 本地歌曲信息实体类
 */
public class LocalSongs implements Parcelable {

    /**
     * 歌曲Id
     */
    private long id;
    /**
     * 歌曲标题
     */
    private String title;
    /**
     * 艺术家
     */
    private String artist;
    /**
     * 专辑
     */
    private String album;
    /**
     * 专辑Id
     */
    private long albumId;
    /**
     * 时长
     */
    private long duration;
    /**
     * 歌曲大小
     */
    private long size;
    /**
     * 歌曲路径
     */
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeLong(this.albumId);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeString(this.url);
    }

    public LocalSongs() {
    }

    private LocalSongs(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.albumId = in.readLong();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<LocalSongs> CREATOR = new Parcelable.Creator<LocalSongs>() {
        public LocalSongs createFromParcel(Parcel source) {
            return new LocalSongs(source);
        }

        public LocalSongs[] newArray(int size) {
            return new LocalSongs[size];
        }
    };
}
