package com.khach.feed.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.khach.feed.R;
import com.khach.feed.activities.HomeActivity;
import com.khach.feed.client.ApiClient;
import com.khach.feed.client.IFeedService;
import com.khach.feed.pojo.Feed;
import com.khach.feed.util.NetworkUtil;
import com.khach.feed.util.PreferencesUtil;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khach.feed.util.Constants.API_KEY;
import static com.khach.feed.util.Constants.CHANNEL_ID;
import static com.khach.feed.util.Constants.FEED_PREF_NAME;
import static com.khach.feed.util.Constants.FEED_PREF_TOTAL;
import static com.khach.feed.util.Constants.FORMAT;
import static com.khach.feed.util.Constants.Q;
import static com.khach.feed.util.Constants.SHOW_ELEMENTS;
import static com.khach.feed.util.Constants.SHOW_FIELDS;

public class FeedService extends Service {
    private static final String TAG = "FeedService";
    public static final int ONGOING_NOTIFICATION_ID = 7;
    public static final int SERVICE_ID = 5;
    private IFeedService iFeedService;

    private Timer timer = new Timer();

    public FeedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iFeedService = ApiClient.getInstance(getCacheDir()).createService(IFeedService.class);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (NetworkUtil.isConnected(getApplicationContext())) {
                    Call<Feed> feedCall = iFeedService.loadFeeds(Q, FORMAT, SHOW_FIELDS, SHOW_ELEMENTS, API_KEY);
                    feedCall.enqueue(new Callback<Feed>() {
                        @Override
                        public void onResponse(Call<Feed> call, Response<Feed> response) {
                            Feed body = response.body();
                            if (body != null) {
                                int total = body.getResponse().getTotal();
                                int prefTotal = PreferencesUtil.getIntData(getApplicationContext(), FEED_PREF_NAME, FEED_PREF_TOTAL);
                                if (total > prefTotal) {
                                    showNotification();
                                    PreferencesUtil.saveIntData(getApplicationContext(), FEED_PREF_NAME, FEED_PREF_TOTAL, total);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Feed> call, Throwable t) {

                        }
                    });
                }
            }
        }, 0, 30000);//30 Seconds
    }

    private void showNotification() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion <= 26) {
            Context context = FeedService.this;

            Intent notificationIntent = new Intent(context, HomeActivity.class);
            PendingIntent contentIntent = PendingIntent.getService(context, 0,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(res.getString(R.string.app_name))
                    .setContentText("new notification");
            Notification n = builder.build();

            nm.notify(ONGOING_NOTIFICATION_ID, n);
        } else {
            Intent notificationIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText("new notification")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(ONGOING_NOTIFICATION_ID, notification);
        }
    }

}
