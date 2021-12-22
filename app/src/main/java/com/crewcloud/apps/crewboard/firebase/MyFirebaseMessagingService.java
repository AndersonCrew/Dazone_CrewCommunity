package com.crewcloud.apps.crewboard.firebase;

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
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.MainActivity;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> mapData = remoteMessage.getData();
        try {
            if (mapData.containsKey("title")) {
                ShowNotification(mapData.get("title"),
                        mapData.get("title"),
                        mapData.get("writer"));
            }

            int badgeCount = Integer.parseInt(mapData.get("badgeCount"));
            PreferenceUtilities prefs = new PreferenceUtilities();
            if (badgeCount > 0) {
                prefs.setInt(Constants.COUNTER_NOTIFICATION, badgeCount);
                CrewBoardApplication.getInstance().setShortcut(badgeCount);
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void ShowNotification(String message, String title, String writer) {
        long[] vibrate = new long[]{1000, 1000, 0, 0, 0};
        // Sets an ID for the notification, so it can be updated.
        int notifyID = 1985;
        String CHANNEL_ID = "CrewCommunity_channel_01";// The id of the channel.
        CharSequence name = "CrewCommunity";//getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        int counter = new PreferenceUtilities().getInt(Constants.COUNTER_NOTIFICATION);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentText(title)
                .setAutoCancel(false)
                .setContentIntent(contentIntent)
                .setTicker(message)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentTitle(message);

        if (counter > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                builder.setNumber(counter);
            else CrewBoardApplication.getInstance().setShortcut(counter);
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(Html.fromHtml("<font color='#878787'>" + title + "</font>" + "<br/>" + writer));
        builder.setStyle(bigTextStyle);

        PreferenceUtilities prefs = CrewBoardApplication.getInstance().getPreferenceUtilities();

        boolean isVibrate = prefs.getNOTIFI_VIBRATE();
        boolean isSound = prefs.getNOTIFI_SOUND();
        boolean isNewMail = prefs.getNOTIFI_MAIL();
        boolean isTime = prefs.getNOTIFI_TIME();
        String strFromTime = prefs.getSTART_TIME();
        String strToTime = prefs.getEND_TIME();

        if (isVibrate) {
            builder.setVibrate(vibrate);
        }

        if (isSound) {
            builder.setSound(soundUri);
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }
        Notification notification = builder.build();

        if (isNewMail) {
            if (isTime) {
                if (TimeUtils.isBetweenTime(strFromTime, strToTime)) {
                    mNotificationManager.notify(notifyID, notification);
                }
            } else {
                mNotificationManager.notify(notifyID, notification);
            }
        }
    }
}


