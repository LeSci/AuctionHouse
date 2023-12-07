package com.sscire.auctionhouse;

import static android.provider.Settings.System.getString;

//import static androidx.core.app.AppOpsManagerCompat.Api23Impl.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Misc {
//    String id = "my_channel_01";
//
//    // Notification
//    // https://developer.android.com/develop/ui/views/notifications/build-notification#java
//    // https://developer.android.com/develop/ui/views/notifications/channels#java
////    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
////            .setSmallIcon(R.drawable.notification_icon)
////            .setContentTitle("My notification")
////            .setContentText("Much longer text that cannot fit one line...")
////            .setStyle(new NotificationCompat.BigTextStyle()
////                    .bigText("Much longer text that cannot fit one line..."))
////            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is not in the Support Library.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this.
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    // Create an explicit intent for an Activity in your app.
//    Intent intent = new Intent(this, AlertDetails.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle("My notification")
//            .setContentText("Hello World!")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            // Set the intent that fires when the user taps the notification.
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true);
//
//    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//    // notificationId is a unique int for each notification that you must define.
//    notificationManager.notify(notificationId, builder.build());
//
//    // The id of the channel.
//    //String id = "my_channel_01";
//    notificationManager.deleteNotificationChannel(id);

}
