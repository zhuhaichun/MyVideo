package com.android.haichun.myvideoplayer.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import com.android.haichun.myvideoplayer.MyApplication;
import com.android.haichun.myvideoplayer.R;

import androidx.annotation.RequiresApi;

import static android.app.Notification.VISIBILITY_SECRET;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {
    private static NotificationManager manager;
    private static NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) MyApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Notification.Builder getNotificationBuilder(String title, String content, String channelId) {
        //大于8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //id随便指定
            NotificationChannel channel = new NotificationChannel(channelId, MyApplication.getInstance().getPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//可否绕过，请勿打扰模式
            channel.enableLights(true);//闪光
            channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
            channel.setLightColor(Color.GREEN);//指定闪光是的灯光颜色
            channel.canShowBadge();//桌面laucher消息角标
            channel.enableVibration(true);//是否允许震动
            channel.setSound(null, null);
            //channel.getAudioAttributes();//获取系统通知响铃声音配置
            channel.getGroup();//获取通知渠道组
            channel.setBypassDnd(true);//设置可以绕过，请勿打扰模式
            channel.setVibrationPattern(new long[]{100, 100, 200});//震动的模式，震3次，第一次100，第二次100，第三次200毫秒
            channel.shouldShowLights();//是否会闪光
            //通知管理者创建的渠道
            getManager().createNotificationChannel(channel);

        }
        return new Notification.Builder(MyApplication.getInstance()).setAutoCancel(true).setChannelId(channelId)
                .setContentTitle(title)
                .setContentText(content).setSmallIcon(R.drawable.no_head);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotification(String title, String content, int manageId, String channelId, int progress, int maxProgress) {
        final Notification.Builder builder = getNotificationBuilder(title,content,channelId);
       /* Intent intent = new Intent(this, SecondeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);*/
        builder.setOnlyAlertOnce(true);
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        builder.setProgress(maxProgress, progress, false);
        builder.setWhen(System.currentTimeMillis());
        getManager().notify(manageId, builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void cancleNotification(int manageId) {
        getManager().cancel(manageId);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateProgress(int manageId, int progress){
        Notification.Builder builder = getNotificationBuilder("正在下载",progress+"%","11");
        builder.setProgress(100,progress,false);
        getManager().notify(manageId,builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void completeProgress(int manageId){
        Notification.Builder builder = getNotificationBuilder("下载完成","100%","11");
        builder.setProgress(100,100,false);
        getManager().notify(manageId,builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void pauseProgress(int manageId,int progress){
        Notification.Builder builder = getNotificationBuilder("下载暂停",progress+"%","11");
        getManager().notify(manageId,builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void errorProgress(int manageId){
        Notification.Builder builder = getNotificationBuilder("下载失败","出现错误","11");
        builder.setProgress(100,0,false);
        getManager().notify(manageId,builder.build());
    }
}
