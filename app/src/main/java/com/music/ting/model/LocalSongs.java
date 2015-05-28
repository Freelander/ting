package com.music.ting.model;

/**
 * Created by Jun on 2015/5/28.
 * 本地歌曲信息实体类
 */
public class LocalSongs {

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
}
