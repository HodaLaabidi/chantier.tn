package tn.chantier.chantiertn.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tn.chantier.chantiertn.notifications.MyResponse;
import tn.chantier.chantiertn.notifications.Sender;

public interface FirebaseAPIService {

    public static final String FIREBASE_DATA_ENDPOINT = "https://fcm.googleapis.com/";

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA30IwCnk:APA91bFbwUqTh1IKF_dv2OkC82LnGn2yRvfQShwOoVfnxo2vwBJMjynw4xyk4KFhyoV4qFHuuz44s1XcV7Mow21z4Jn5jm5gnl2JvmqLQOGfGXfjZIle-DDzriABo1_temJFMavv7v5x"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
