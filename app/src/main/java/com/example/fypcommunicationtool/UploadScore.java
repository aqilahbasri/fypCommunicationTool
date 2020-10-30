package com.example.fypcommunicationtool;

public class UploadScore {
    public String userID;
    public int xp = 0;


    public UploadScore (){}

    public UploadScore(String userID, int xp) {
        this.userID = userID;
        this.xp = xp;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

}