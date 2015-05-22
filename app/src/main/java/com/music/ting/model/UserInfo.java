package com.music.ting.model;

/**
 * Created by Jun on 2015/5/11.
 * 用户信息实体类
 */
public class UserInfo {
    /**
     * 用户Id
     */
    private int id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户Email
     */
    private String email;
    /**
     * 个性标签
     */
    private String Bio;
    /**
     * 用户头像
     */
    private Avatar avatar;

    public class Avatar{
        /**
         * 用户头像URL
         */
        public String url;

        public String geturl() {
            return url;
        }

        public void seturl(String url) {
            this.url = url;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", Bio='" + Bio + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
