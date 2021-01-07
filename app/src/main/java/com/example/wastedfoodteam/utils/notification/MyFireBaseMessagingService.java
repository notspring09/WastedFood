package com.example.wastedfoodteam.utils.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.home.SellerHomeActivity;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.d("notification", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
        pushNotification(remoteMessage);
        }

    public void pushNotification(RemoteMessage remoteMessage){
        NotificationUtil notificationUtil = new NotificationUtil();
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        String CHANNEL_ID= getString(R.string.project_id);
        String CHANNEL_NAME="MESSAGE";
        NotificationManagerCompat manager=NotificationManagerCompat.from(MyFireBaseMessagingService.this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent;
        Intent resultIntent = new Intent(this, LoginActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(Variable.CURRENT_USER.equals("SELLER")) {
                resultIntent = new Intent(this, SellerHomeActivity.class);
                resultIntent.putExtra("From", "notifyFrag");
                pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationUtil.getTotalNotification(getApplicationContext(), Variable.SELLER.getId(), Variable.bottomNavigationViewSeller);
            }else if(Variable.CURRENT_USER.equals("BUYER")){
                resultIntent = new Intent(this, BuyHomeActivity.class);
                resultIntent.putExtra("From", "notifyFrag");
                pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationUtil.getTotalNotification(getApplicationContext(), Variable.BUYER.getId(), Variable.bottomNavigationViewSeller);
            }
        }
        Notification notification = new NotificationCompat.Builder(MyFireBaseMessagingService.this,CHANNEL_ID)
                .setSmallIcon(R.drawable.logo2)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(getRandomNumber(),notification);
    }

    private static int getRandomNumber() {
        Date dd= new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }


    public void sendNotification(String messageBody) {
        Intent intent = new Intent(MyFireBaseMessagingService.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle(getString(R.string.project_id))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_missed,
                                "Cancel",
                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)))
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_outgoing,
                                "OK",
                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NotNull String s)
    {
        super.onNewToken(s);
        Log.e("TOKEN",s + "nothing");
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Token token1= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
}
