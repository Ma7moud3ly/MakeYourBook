package com.ma7moud3ly.makeyourbook.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFCM extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage msg) {
        if (msg.getData() != null) {
            String title = msg.getData().get("title");
            String text = msg.getData().get("text");
            String type = msg.getData().get("type");
            String id = msg.getData().get("id");
            String url = "https://makeubook.web.app/" + type + "/" + id;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            myNotify((int) (Math.random() * 1000), title, text, intent);
        }
    }

    private void myNotify(int notify_id, String title, String text, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setColorized(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notify_id, builder.build());
    }

}