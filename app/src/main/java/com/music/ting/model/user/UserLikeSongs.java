package com.music.ting.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jun on 2015/6/27.
 */
public class UserLikeSongs {

    private User user;
    @SerializedName("liked_songs")
    private List<LikeSongs> likeSongs;

public List<LikeSongs> getLikeSongs() {
        return likeSongs;
    }

    public void setLikeSongs(List<LikeSongs> likeSongs) {
        this.likeSongs = likeSongs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public class LikeSongs{
        private int id;
        @SerializedName("s_id")
        private int sId;
        private String title;
        private String artist;
        @SerializedName("pic")
        private String urlPic;
        private String content;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("comments_count")
        private int commentsCount;

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

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
    }
}
