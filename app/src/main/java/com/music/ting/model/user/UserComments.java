package com.music.ting.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jun on 2015/6/27.
 */
public class UserComments {

    private List<Comments> comments;
    private User user;

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public User getUser() {
        return user;
    }

    public class Comments {

        @SerializedName("song_id")
        private int songId;

        @SerializedName("user_id")
        private int userId;

        private int id;

        private String content;

        public void setSongId(int songId) {
            this.songId = songId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSongId() {
            return songId;
        }

     
        public int getUserId() {
            return userId;
        }


        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }

    public class User {

        private String bio;

        private Avatar avatar;

        private String name;

        private int id;

        private String email;

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setAvatar(Avatar avatar) {
            this.avatar = avatar;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBio() {
            return bio;
        }

        public Avatar getAvatar() {
            return avatar;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }


        public class Avatar {

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

                private String url;

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getUrl() {
                    return url;
                }
            }

            public class LargeUrl {

                private String url;

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getUrl() {
                    return url;
                }
            }

            public class ThumbUrl {


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
}
