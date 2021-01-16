package com.example.fypcommunicationtool.assessment;

import java.io.Serializable;

public class Users implements Serializable {

    public String email, fullName, password, profileImage, userID, fcmToken;

    public Users() {
    }

    public Users(String email, String fullName, String password, String profileImage, String userID, String fcmToken) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.profileImage = profileImage;
        this.userID = userID;
        this.fcmToken = fcmToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
