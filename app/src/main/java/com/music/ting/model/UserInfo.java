package com.music.ting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun on 2015/5/11.
 * 用户信息实体类
 */
public class UserInfo implements Parcelable {
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
    private String bio;
    /**
     * 用户头像
     */
    private Avatar avatar;

    public static class Avatar implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
        }

        public Avatar() {
        }

        private Avatar(Parcel in) {
            this.url = in.readString();
        }

        public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
            public Avatar createFromParcel(Parcel source) {
                return new Avatar(source);
            }

            public Avatar[] newArray(int size) {
                return new Avatar[size];
            }
        };
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
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
                ", Bio='" + bio + '\'' +
                ", avatar=" + avatar +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeParcelable(this.avatar, flags);
    }

    public UserInfo() {
    }

    private UserInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        this.bio = in.readString();
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
