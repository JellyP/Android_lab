package com.example.administrator.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Receiver extends BroadcastReceiver {
    public final static String STATICACTION=new String("MystaticFliters");
    public final static String DYNAMICACTION=new String("MyDynamicFliters");
    NotificationManager manager;
    PendingIntent mPenddingIntent;
    RemoteViews remoteViews;
    Goods temp;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentFromReceiver=new Intent();
        if(intent.getAction().equals(STATICACTION))
        {
            temp=(Goods) intent.getExtras().get("random");
            intentFromReceiver.setClass(context,GoodDetail.class);
            intentFromReceiver.putExtra("goods",temp);
            mPenddingIntent= PendingIntent.getActivity(context,0,intentFromReceiver,PendingIntent.FLAG_UPDATE_CURRENT);

        }
        else if(intent.getAction().equals(DYNAMICACTION))
        {
            temp=(Goods)  intent.getExtras().get("dynamic");
            intentFromReceiver.setClass(context,MainActivity.class);
            intentFromReceiver.putExtra("toShoppingCar",true);
            mPenddingIntent= PendingIntent.getActivity(context,0,intentFromReceiver,0);
        }
        remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_notification);
        remoteViews.setImageViewResource(R.id.notification_icon,temp.getImageid());
        if(intent.getAction().equals(STATICACTION))
        {
            remoteViews.setTextViewText(R.id.notification_title,"ExperimentFour");
            remoteViews.setTextViewText(R.id.content1,"新商品热卖");
            remoteViews.setTextViewText(R.id.content2,temp.getName()+"仅售"+temp.getPrice()+"!");

            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentIntent(mPenddingIntent)
                    .setSmallIcon(temp.getImageid())
                    .setContentText("·")
                    .setAutoCancel(true);
            Notification notification=builder.build();
            notification.bigContentView=remoteViews;
            manager.notify(0,notification);
        }
        else if(intent.getAction().equals(DYNAMICACTION))
        {
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
            manager.notify(1,notification);
        }
//        if(intent.getAction().equals(STATICACTION))
//        {
//
//
//
//
//
//            Notification.Builder builder=new Notification.Builder(context);
////            Notification notification=new Notification();
////            notification.icon=temp.getImageid();
////            notification.flags=Notification.FLAG_AUTO_CANCEL;
////            notification.when=System.currentTimeMillis();
//            Intent mIntent=new Intent(context,GoodDetail.class);
//
//
////            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_notification);
////            remoteViews.setTextViewText(R.id.notification_title,"Experiment 4");
////            remoteViews.setTextViewText(R.id.content1,"马上下单");
////            remoteViews.setTextViewText(R.id.content2,temp.getName()+"已添加到购物车");
//
//            //remoteViews.setImageViewIcon(R.id.notification_icon,);
//
////            builder.setContent(remoteViews);
////            notification.contentIntent=mPenddingIntent;
//
//            Bitmap bm= BitmapFactory.decodeResource(context.getResources(), temp.getImageid());
//            builder.setContentTitle("Experiment 4")
////                   .setContentText("马上下单\n ")
//                   .setContentText(temp.getName()+"已添加到购物车")
//                   .setLargeIcon(bm)
////                   .setSmallIcon(temp.getImageid())
//                   .setAutoCancel(true);
//
//            builder.setContentIntent(mPenddingIntent);
//            Notification notify=builder.build();
//            manager.notify(0,notify);
//
//        }
    }
}
