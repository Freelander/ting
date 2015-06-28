package com.music.ting.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jun on 2015/6/27.
 */
public class UserShareSongs {
    private User user;
    private List<Songs> songs;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Songs> getSongs() {
        return songs;
    }

    public void setSongs(List<Songs> songs) {
        this.songs = songs;
    }

    public class User{
        private int id;
        private Avatar avatar;
        private String name;
        private String email;
        private String bio;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Avatar getAvatar() {
            return avatar;
        }

        public void setAvatar(Avatar avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }
    }

    public class Songs{

        @SerializedName("user_id")
        private int userId;
        private int id;
        @SerializedName("s_id")
        private int sId;
        private String title;
        private String artist;
        private String pic;
        private String content;
        @SerializedName("comments_count")
        private int commentsCount;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getsId() {
            return sId;
        }

        public void setsId(int sId) {
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }
    }

    public class Avatar {
        /**
         * small : {"url":"http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Fsmall_1623782_586424324782930_323912970_n.png"}
         * large : {"url":"http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Flarge_1623782_586424324782930_323912970_n.png"}
         * thumb : {"url":"http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Fthumb_1623782_586424324782930_323912970_n.png"}
         * url : http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2F1623782_586424324782930_323912970_n.png
         */
        private SmallUrl small;
        private LargeUrl large;
        private ThumbUrl thumb;
        private String url;

        public void setSmall(SmallUrl small) {
            this.small = small;
        }

        public void setLarge(LargeUrl large) {
            this.large = large;
        }

        public void setThumb(ThumbUrl thumb) {
            this.thumb = thumb;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public SmallUrl getSmall() {
            return small;
        }

        public LargeUrl getLarge() {
            return large;
        }

        public ThumbUrl getThumb() {
            return thumb;
        }

        public String getUrl() {
            return url;
        }

        public class SmallUrl {
            /**
             * url : http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Fsmall_1623782_586424324782930_323912970_n.png
             */
            private String url;

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }

        public class LargeUrl {
            /**
             * url : http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Flarge_1623782_586424324782930_323912970_n.png
             */
            private String url;

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }

        public class ThumbUrl {
            /**
             * url : http://7tsy2n.com1.z0.glb.clouddn.com/uploads%2Fuser%2Favatar%2F4%2Fthumb_1623782_586424324782930_323912970_n.png
             */
            private String url;

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}