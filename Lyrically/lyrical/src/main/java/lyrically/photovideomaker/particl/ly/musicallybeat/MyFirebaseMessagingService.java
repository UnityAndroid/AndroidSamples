package lyrically.photovideomaker.particl.ly.musicallybeat;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.SplashActivity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();
    public static String CHANNEL_ID = "royalspin";
    public static String id = "hi";
    public static boolean inforeground = false;
    String title = "", text = "", url = "", icon = "";
    int nottype = 0;
    boolean checktitle, checktext, checkurl, checkicon;
    Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {

            context = this;

            Log.e("task", "notifed");

            checktitle = remoteMessage.getData().containsKey("title");
            checktext = remoteMessage.getData().containsKey("text");
            checkurl = remoteMessage.getData().containsKey("url");
            checkicon = remoteMessage.getData().containsKey("icon");

            if (checktitle)
                title = remoteMessage.getData().get("title");

            if (checktext)
                text = remoteMessage.getData().get("text");

            if (checkurl)
                url = remoteMessage.getData().get("url");

            if (checkicon)
                icon = remoteMessage.getData().get("icon");


            PendingIntent contentIntent;

            if (isAppIsInBackground(context)) {
                Intent intent;
                intent = new Intent(this, UnityPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            } else {
                contentIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        new Intent(), // add this
                        PendingIntent.FLAG_ONE_SHOT);
            }


            if (checktitle && checktext && !checkurl && !checkicon) {

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.logo).setSound(defaultSoundUri).setContentTitle(title)
                        .setContentText(text).setAutoCancel(true).setContentIntent(contentIntent);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            } else if (checktitle && checktext && checkicon) {
                startnotify();
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    public void startnotify() {
        new sendNotification().execute();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public class sendNotification extends AsyncTask<String, Void, Bitmap> {

        public sendNotification() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("task", "image download starts");
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                Bitmap bitmap;
                URL urll = new URL(icon);
                HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                connection.setDoInput(true);
                //  connection.setReadTimeout(60000);
                //   connection.setConnectTimeout(60000);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "bitmap error" + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "bitmap error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);

            if (result != null) {


                Log.e("task", "image download completes");

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder builder;
                PendingIntent contentIntent;

                Intent rIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                if (checktitle && checktext && checkicon) {

                    if (isAppIsInBackground(context)) {
                        Intent intent;
                        intent = new Intent(context, lyrically.photovideomaker.particl.ly.musicallybeat.UnityPlayerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    } else {
                         contentIntent = PendingIntent.getActivity(
                                getApplicationContext(),
                                0,
                                new Intent(), // add this
                                PendingIntent.FLAG_ONE_SHOT);
                    }

                    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.app_promotion_notify);

                    contentView.setImageViewBitmap(R.id.image, result);
               /*     contentView.setTextViewText(R.id.title, title);
                    contentView.setTextViewText(R.id.text, text);
*/
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        contentView.setTextViewText(R.id.title, Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        contentView.setTextViewText(R.id.title, Html.fromHtml(title));
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        contentView.setTextViewText(R.id.text, Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        contentView.setTextViewText(R.id.text, Html.fromHtml(text));
                    }

                    builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.logo).setContentTitle(title)
                            .setAutoCancel(true).setContent(contentView).setContentIntent(contentIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    manager.notify(1, builder.build());


                } else if (checktitle && !checktext && checkurl && checkicon) {

                    contentIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            rIntent, // add this
                            PendingIntent.FLAG_ONE_SHOT);

                    builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.logo).setContentTitle(title)
                            .setAutoCancel(true).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result)).setContentIntent(contentIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    manager.notify(1, builder.build());


                } else if (checktitle && !checktext && !checkurl && checkicon) {

                    if (isAppIsInBackground(context)) {
                        Intent intent;
                        intent = new Intent(context, UnityPlayerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    } else {
                        contentIntent = PendingIntent.getActivity(
                                getApplicationContext(),
                                0,
                                new Intent(), // add this
                                PendingIntent.FLAG_ONE_SHOT);
                    }

                    builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.logo).setContentTitle(title)
                            .setAutoCancel(true).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result)).setContentIntent(contentIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    manager.notify(1, builder.build());
                }
            } else {
                Log.e(TAG, "Bitmap Null");
            }
        }
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

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

        return isInBackground;
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}
