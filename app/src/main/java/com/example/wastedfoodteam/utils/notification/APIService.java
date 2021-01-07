package com.example.wastedfoodteam.utils.notification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAFOD5GtY:APA91bEJm7Qx-8fonSsZygoM-rHmrvLPd4FK3exlwOfTvJYVCr8W9n5dbzAKEEfRv2rXTNmUV7UkZJBVpzOirTOYYgsqPLb46AzarcBMMcx7YZx30j-daBpaf33bozQhojsGgkqS3--C" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
