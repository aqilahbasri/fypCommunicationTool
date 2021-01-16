package com.example.fypcommunicationtool.listeners;

import com.example.fypcommunicationtool.Contacts;

public interface UsersListener {

    void initiateVideoMeeting(Contacts contacts);
    void initiateAudioMeeting(Contacts contacts);
}
