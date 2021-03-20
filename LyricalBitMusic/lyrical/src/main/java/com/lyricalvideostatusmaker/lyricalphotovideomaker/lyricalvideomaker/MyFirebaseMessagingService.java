package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public final String TAG = "FCM Service";
    public static final int NOTIFICATION_ID = 1;
    public String NOTIFICATION_CHANNEL_ID = "LyricalBitMusic_channel_01";
    public NotificationManager mNotificationManager;
    public static String id = "hi";
    public String title = "", text = "", url = "", icon = "";
    public boolean checktitle, checktext, checkurl, checkicon;
    public Context context;

    @Override
    public void onNewToken(String token) {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        try {
            context = this;

            title = remoteMessage.getData().get("title");
            text = remoteMessage.getData().get("text");
            url = remoteMessage.getData().get("url");
            icon = remoteMessage.getData().get("icon");

            checktitle = remoteMessage.getData().containsKey("title");
            checktext = remoteMessage.getData().containsKey("text");
            checkurl = remoteMessage.getData().containsKey("url");
            checkicon = remoteMessage.getData().containsKey("icon");

            if (title.equalsIgnoreCase("") || title.isEmpty()) {
                checktitle = false;
            } else {
                checktitle = true;
            }

            if (text.equalsIgnoreCase("") || text.isEmpty()) {
                checktext = false;
            } else {
                checktext = true;
            }

            if (url.equalsIgnoreCase("") || url.isEmpty()) {
                checkurl = false;
            } else {
                checkurl = true;
            }

            if (icon.equalsIgnoreCase("") || icon.isEmpty()) {
                checkicon = false;
            } else {
                checkicon = true;
            }

            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.app_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            Intent intent;
            if (checkurl) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            } else {
                intent = new Intent(this, UnityPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setLights(Color.RED, 3000, 3000);

            mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setContentTitle(title);
            mBuilder.setTicker(text);

            if (checkicon) {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(icon)).setSummaryText(Html.fromHtml(text)));
                mBuilder.setContentText(Html.fromHtml(text));
            } else {
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(text)));
                mBuilder.setContentText(Html.fromHtml(text));
            }

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
            return R.drawable.logo_white;
        } else {
            return R.drawable.logo_white;
        }
    }

    private int getColour() {
        return 0x603683;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static boolean isApplicationSentToBackground(final Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
