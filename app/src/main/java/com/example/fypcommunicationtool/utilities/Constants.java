package com.example.fypcommunicationtool.utilities;

import java.util.HashMap;

public class Constants {

    //Guna firebase database
    public static final String KEY_COLLECTION_USERS = "Users";
    public static final String KEY_FULL_NAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "userID";
    public static final String KEY_PROFILE_IMAGE ="profileImage";
    public static final String KEY_FCM_TOKEN = "fcmToken";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration-ids";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAC4XyEUs:APA91bF6WlQq3pFXXUNFSH3Oak03sPtOHSchHZmV-1U9ECY_w3jfgC0WyiJymcNIDjNxXtODYENf_Ps6bHjyzxONfxA4Uvx_8Wx48PYJxOIoTomfpK6_gt1xPpRJojRvGfRFEyN5FtJj"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }

}
