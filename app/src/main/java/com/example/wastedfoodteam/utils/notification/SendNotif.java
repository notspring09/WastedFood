package com.example.wastedfoodteam.utils.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotif {

    public void notificationHandle(String firebaseUID , final String title , final String message){

        FirebaseDatabase.getInstance().getReference().child("Tokens").child(firebaseUID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usertoken=dataSnapshot.getValue(String.class);
                Log.i("notification",usertoken + " usertoken");
                //TODO fix cứng để test
                sendNotifications(usertoken, title, message);
                //sendNotifications("dmGJ1N4DQo73InJghjWnQw:APA91bEQkC05VFM6FCkFssrAeCZ5oGeJ0vQCe_AchmZgf4uxAQhTLfqbzZ3tN2yEcYANFyVsdyAKNsBXy_cdyN5h4g2FlHSr76IMNKpowdlHXTBfAjkTMHYOeCXk2483VLzPTmWyd12N", title, message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken();
    }

    public void updateToken(){
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child()
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        //failed
                        Log.i("notification","failed");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                Log.i("notification","failed");
            }
        });
    }
}
