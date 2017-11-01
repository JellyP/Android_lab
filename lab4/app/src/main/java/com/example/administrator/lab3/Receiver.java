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
   // public final static String DYNAMICACTION=new String("MyDynamicFliters");
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
            temp=(Goods) intent.getExtras().get("random");//获取ramdom物品
            intentFromReceiver.setClass(context,GoodDetail.class);//设置跳转
            intentFromReceiver.putExtra("goods",temp);//设置传输的key和对应的商品
            mPenddingIntent= PendingIntent.getActivity(context,0,intentFromReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
            /*FLAG_UPDATE_CURRENT会更新之前PendingIntent的消息，
            比如，你推送了消息1，并在其中的Intent中putExtra了一个值“ABC”，
            在未点击该消息前，继续推送第二条消息，并在其中的Intent中putExtra了一个值“CBA”，
            好了，这时候，如果你单击消息1或者消息2，你会发现，他俩个的Intent中读取过来的信息都是“CBA”，
            就是说，第二个替换了第一个的内容*/
            remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_notification);
            /*绑定RemoteViews的布局*/
            remoteViews.setImageViewResource(R.id.notification_icon,temp.getImageid());
            /*设置布局中的图片*/
            remoteViews.setTextViewText(R.id.notification_title,"ExperimentFour");//设置标题
            remoteViews.setTextViewText(R.id.content1,"新商品热卖");//设置内容1
            remoteViews.setTextViewText(R.id.content2,temp.getName()+"仅售"+temp.getPrice()+"!");//设置内容2

            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentIntent(mPenddingIntent)
                    .setSmallIcon(temp.getImageid())
                    .setContentText("·")
                    .setAutoCancel(true);
            Notification notification=builder.build();
            notification.bigContentView=remoteViews; //可以显示更多内容的通知
            //通过这种方式来绑定布局
            manager.notify(0,notification);
        }
//        else if(intent.getAction().equals(DYNAMICACTION))
//        {
//            temp=(Goods)  intent.getExtras().get("dynamic");
//            intentFromReceiver.setClass(context,MainActivity.class);
//            intentFromReceiver.putExtra("toShoppingCar",true);
//            mPenddingIntent= PendingIntent.getActivity(context,0,intentFromReceiver,0);
//        }

//        if(intent.getAction().equals(STATICACTION))
//        {
//
//        }
//        else if(intent.getAction().equals(DYNAMICACTION))
//        {
//            remoteViews.setTextViewText(R.id.notification_title,"前往购物车吧~");
//            remoteViews.setTextViewText(R.id.content1,"马上下单");
//            remoteViews.setTextViewText(R.id.content2,temp.getName()+"已添加到购物车");
//
//            Notification.Builder builder=new Notification.Builder(context);
//            builder.setContentIntent(mPenddingIntent)
//                    .setSmallIcon(temp.getImageid())
//                    .setContentText("·")
//                    .setAutoCancel(true);
//            Notification notification=builder.build();
//            notification.bigContentView=remoteViews;
//            manager.notify(1,notification);
////        }
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
