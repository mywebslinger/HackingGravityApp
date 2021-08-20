/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain ISRootedDevice2 copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matt.hacking_gravity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.matt.hacking_gravity.common.MyPref;
import java.util.Iterator;
import java.util.Map;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    static int ids = 1;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.e("New Token","-"+token);

        MyPref myPref = new MyPref(this);
        myPref.setMytoken(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {

            try {
                if (remoteMessage.getData() != null) {
                    String body = "";
                    int id = 1;
                    Map<String, String> datamp = remoteMessage.getData();
                    if (datamp.containsKey("body")) {

                        Iterator myVeryOwnIterator = datamp.keySet().iterator();
                        while (myVeryOwnIterator.hasNext()) {
                            try {
                                String key = (String) myVeryOwnIterator.next();
                                String value = (String) datamp.get(key);

                                if (key.equalsIgnoreCase("body")) {
                                    body = value;
                                } else if (key.equalsIgnoreCase("id")) {
                                    id = Integer.parseInt(value);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    sendNotification(body, id);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }

        Log.e("hello notiii","notification");

        // Check if message contains ISRootedDevice2 notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getData().get("body"), ids++);
        }

    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    public void sendNotification(String messageBody, int notId) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Data", "" + messageBody);
        intent.putExtra("id", notId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_IMMUTABLE);//FLAG_ONE_SHOT FLAG_IMMUTABLE  FLAG_UPDATE_CURRENT


        String channelId = "Hacking Gravity Yoga";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Hacking Gravity Yoga",
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(notId , notificationBuilder.build());

        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Data", "" + messageBody);
        intent.putExtra("id", notId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notId, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_firebasenoti);
//
//        contentView.setImageViewResource(R.id.notifimage, R.mipmap.ic_launcher);
//        contentView.setTextViewText(R.id.notiftitle, getResources().getString(R.string.app_name));
//        contentView.setTextViewText(R.id.notiftext, title);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)//,channelId
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(false).setOngoing(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notId *//* ID of notification *//*, notificationBuilder.build());*/
    }
}
