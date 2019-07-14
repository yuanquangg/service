package com.example.serviceapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

public class MusicPlayerService extends Service {

    //<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    //  <service android:name=".MusicPlayerService" android:exported="true">></service>

//    String text = getIntent().getStringExtra("text");
//        if(!TextUtils.isEmpty(text)){
//        mTv.setText(text);
//    }
    private static boolean flag = true;
    NotificationManager notificationManager;
    String notificationId = "channelId";

    String notificationName = "channelName";

    private void startForegroundService() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }
        startForeground(1,getNotification());

    }

    private Notification getNotification() {
     Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("华为翻译已开启")
                .setContentText("更多选项");

        //设置Notification的ChannelID,否则不能正常显示

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Notification notification = builder.build();
        return notification;

    }

    @Override

    public void onCreate() {
        super.onCreate();
        startForegroundService();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   setTimer();
        registerClipEvents();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
    //   addPrimaryClipChangedListener();
    }

    private void setTimer(){
        mHandler.postDelayed(runnable, 1000);
    }

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //在这里执行定时需要的操作
            Toast.makeText(getApplicationContext(),"华为翻译在运行..",Toast.LENGTH_SHORT).show();
            if (flag) {
                mHandler.postDelayed(this, 30000);
            }
        }
    };
    private void stopTimer(){
        flag = false;
    }

    private void registerClipEvents() {

        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {

                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();

                    if (addedText != null) {
                        Toast.makeText(getApplicationContext(),"copied text: " + addedText,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MusicPlayerService.this, MainActivity.class);
                        intent.putExtra("text",addedText);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
