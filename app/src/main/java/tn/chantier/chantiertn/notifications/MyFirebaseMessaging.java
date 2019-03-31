package tn.chantier.chantiertn.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.internal.Util;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.ChatActivity;
import tn.chantier.chantiertn.activities.HomeActivity;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.utils.Utils;

import static tn.chantier.chantiertn.utils.Utils.isNotification;

public class MyFirebaseMessaging extends FirebaseMessagingService {

      private static ArrayList<Notification> listNotifications = new ArrayList<>();

      public static String refreshedToken = null;
     public static  String date = null;



    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {
        Log.e(" inAppMessaging id " , FirebaseInAppMessaging.getInstance().toString()+ " !");
        Log.e(" inAppMessaging id " , FirebaseInstanceId.getInstance().getId()+ " !");
        refreshedToken = s;




    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
       // String message = remoteMessage.getData().toString();
       // String message2 = remoteMessage.getNotification().getBody();
        //Log.e("notif" , message +" !");
        //Log.e("notif" , message2 +" !");
        //String message = remoteMessage.getNotification().getTitle() + remoteMessage.getNotification().getBody();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Log.e("test notification !" , message +"!");
       // Log.e("test notification" , sented +"!");
        // the app can crashed after executing the next instruction sented can be null object ! don't know way -_-
        if (sented != null) {
            if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
                String user = remoteMessage.getData().get("user");
                String title = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();


                RemoteMessage.Notification notification = remoteMessage.getNotification();
                int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
                Intent intent = new Intent(MyFirebaseMessaging.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid", user);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // 6 - Support Version >= Android 8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = getString(R.string.default_notification_channel_id);
                    CharSequence channelName = "Message provenant de Firebase";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(mChannel);
                }

                Log.e(" test notif" , title + body + "!");
                int i = 0;
                if (j > 0) {
                    i = j;
                }

                notificationManager.notify(i, builder.build());

            }
        } else {
            date = System.currentTimeMillis()+"";
            String channelId = getString(R.string.default_notification_channel_id);
            String title ="" , content ="" , type;
            if ( remoteMessage.getData().size() > 0) {
                Map<String, String> getData = remoteMessage.getData();
                title = getData.get("title");
                content = getData.get("content");
                type = getData.get("type");
                Log.e("data notification" , title + " "+ content + " "+ type +" ");

                if (title != null && content != null && type != null) {
                    isNotification = true ;
                    listNotifications = SharedPreferencesFactory.getListOfNotifications(getBaseContext());
                    listNotifications.add(0,new Notification(title, content, date, type));
                    SharedPreferencesFactory.saveNotifications(getBaseContext(), listNotifications);
                    Utils.isNotification = true ;

                }



                Intent intent = new Intent(MyFirebaseMessaging.this, HomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(R.drawable.logo_c)
                        .setAutoCancel(true)
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                //  Support Version >= Android 8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    CharSequence channelName = "Message provenant de Firebase";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    mChannel.setVibrationPattern(new long[]{ 0,1000,500,1000});
                    mChannel.enableVibration(true);
                    notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(007, builder.build());


            }

        }
    }

}
