package com.music.ting.model;

/**
 * Created by Jun on 2015/5/31.
 */
public class OnLineSongs {

    /**
     * singer : Cascada
     * lyric : [00:00.00]Everytime We Touch (Yanou&amp;#039;s Candlelight Mix)
     * id : 1769890006
     * pic : http://img.xiami.net/images/album/img22/995422/1290995422_2.jpg
     * pic2x : http://img.xiami.net/images/album/img22/995422/1290995422.jpg
     * title : Everytime We Touch (Yanou&#39;s Candlelight Mix)
     * songurl : http://m5.file.xiami.com/276/37276/413077/1769890006_1793142_l.mp3?auth_key=a6d0acea381c5b2d4fbdf12edf482e16-1433116800-0-null
     * lyricurl : http://img.xiami.net/lyric/6/1769890006_13830711051717.lrc
     */
    private String singer;//歌手
    private String lyric;//歌词
    private int id;//歌曲播放Id
    private String pic;//专辑图片
    private String pic2x;//专辑图片大图
    private String title;//歌名
    private String songurl;//歌曲播放地址
    private String lyricurl;//歌词地址

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setPic2x(String pic2x) {
        this.pic2x = pic2x;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }

    public void setLyricurl(String lyricurl) {
        this.lyricurl = lyricurl;
    }

    public String getSinger() {
        return singer;
    }

    public String getLyric() {
        return lyric;
    }

    public int getId() {
        return id;
    }

    public String getPic() {
        return pic;
    }

    public String getPic2x() {
        return pic2x;
    }

    public String getTitle() {
        return title;
    }

    public String getSongurl() {
        return songurl;
    }

    public String getLyricurl() {
        return lyricurl;
    }
}
