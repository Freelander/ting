package com.music.ting.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jun on 2015/5/11.
 * 歌曲评论实体类
 */
public class Comments {


        /**
         * 歌曲Id
         */
        @SerializedName("song_id")
        private int songId;
        /**
         * 评论Id
         */
        private int id;
        /**
         * 评论内容
         */
        private String content;
        /**
         * 评论用户Id
         */
        @SerializedName("user_id")
        private int userId;

        public int getSongId() {
            return songId;
        }

        public void setSongId(int songId) {
            this.songId = songId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

}
