package com.example.administrator.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class DynamicReceiver extends BroadcastReceiver {

    public final static String DYNAMICACTION=new String("MyDynamicFliters");
    NotificationManager manager;
    PendingIntent mPenddingIntent;
    RemoteViews remoteViews;
    Goods temp;
    private static int num=0;//修改这个自增实现按多次购物车弹出多个通知
    @Override
    public void onReceive(Context context, Intent intent) {

        manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentFromReceiver=new Intent();
        if(intent.getAction().equals(DYNAMICACTION))
        {
            temp=(Goods)  intent.getExtras().get("dynamic");//获取动态广播的物品

            intentFromReceiver.setClass(context,MainActivity.class);//设置跳转界面
            //intentFromReceiver.putExtra("toShoppingCar",true);
            mPenddingIntent= PendingIntent.getActivity(context,0,intentFromReceiver,0);
            remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_notification);
            //绑定remoteViews
            remoteViews.setImageViewResource(R.id.notification_icon,temp.getImageid());
            remoteViews.setTextViewText(R.id.notification_title,"前往购物车吧~");
            remoteViews.setTextViewText(R.id.content1,"马上下单");
            remoteViews.setTextViewText(R.id.content2,temp.getName()+"已添加到购物车");

            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentIntent(mPenddingIntent)
                    .setSmallIcon(temp.getImageid())
                    .setContentText("·")
                    .setAutoCancel(true);
            Notification notification=builder.build();
            notification.bigContentView=remoteViews;
            manager.notify(num++,notification);
        }

//        if(intent.getAction().equals(DYNAMICACTION))
//        {
//
//        }
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
