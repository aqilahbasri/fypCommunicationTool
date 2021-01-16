package com.example.fypcommunicationtool.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({"Authorization: key=AAAAC4XyEUs:APA91bF6WlQq3pFXXUNFSH3Oak03sPtOHSchHZmV-1U9ECY_w3jfgC0WyiJymcNIDjNxXtODYENf_Ps6bHjyzxONfxA4Uvx_8Wx48PYJxOIoTomfpK6_gt1xPpRJojRvGfRFEyN5FtJj",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<String> sendRemoteMessage(
            @Body String remoteBody
    );
//    Call<String> sendRemoteMessage(
//            @HeaderMap HashMap<String, String> headers,
//            @Body String remoteBody
//    );
}
