package com.example.fypcommunicationtool;

public class UploadScore {
    public long xp;
    public String username;
    public String profileimage;


    public UploadScore(){}

    public UploadScore(long xp, String username, String profileimage) {
        this.xp = xp;
        this.username = username;
        this.profileimage = profileimage;


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

}
