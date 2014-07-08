package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carlos on 07/07/14.
 */
public class Twit implements Parcelable {

    private String message;
    private String time;
    private String link;
    private String account;
    private String avatarUrl;
    private String alt;



    public Twit() {
    }

    public Twit(String message, String time, String link, String account, String avatarUrl, String alt) {
        this.message = message;
        this.time = time;
        this.link = link;
        this.account = account;
        this.avatarUrl = avatarUrl;
        this.alt = alt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.time);
        dest.writeString(this.link);
        dest.writeString(this.account);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.alt);
    }

    private Twit(Parcel in) {
        this.message = in.readString();
        this.time = in.readString();
        this.link = in.readString();
        this.account = in.readString();
        this.avatarUrl = in.readString();
        this.alt = in.readString();
    }

    public static final Creator<Twit> CREATOR = new Creator<Twit>() {
        public Twit createFromParcel(Parcel source) {
            return new Twit(source);
        }

        public Twit[] newArray(int size) {
            return new Twit[size];
        }
    };
}
