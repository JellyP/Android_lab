package com.example.administrator.meetingtaste;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.Items;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.squareup.picasso.Picasso.with;

/**
 * Created by renardbebe on 2018/1/6.
 */

public class Receiver extends BroadcastReceiver {
    private static final String STATICACTION = "com.example.administrator.meetingtaste.MyStaticFilter";

    private int I=1;
    private String Url;
    private String Name;
    private Bundle bundle;

    RemoteViews mRemoteViews = new RemoteViews("com.example.administrator.meetingtaste", R.layout.remoteview_layout);

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equals(STATICACTION)) {
            bundle = intent.getExtras();
            final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final Notification.Builder builder = new Notification.Builder(context);

            DBService dbService= DBFactory.createDBService();
            dbService.queryItemByItemId((long)bundle.getSerializable("ItemID"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Items>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Items items) {
                            Url = items.getItem_picture_url();
                            Name = items.getItem_name();

                            mRemoteViews.setTextViewText(R.id.title, "成功下单");
//                            // 设置图片
//                            URL userImageUrl = null;
//                            try {
//                                userImageUrl = new URL(Url);
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            }
//                            Bitmap bmUserImage = null;
//                            try {
//                                Log.e("e2", userImageUrl.toString());
//                                Picasso.with(context).load(items.getItem_picture_url()).into(R.id.img);
//                                bmUserImage = BitmapFactory.decodeStream(userImageUrl.openStream());
//                                Log.e("e3", userImageUrl.toString());
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                                Log.e("e4", e.getMessage());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Log.e("e5", e.getMessage());
//                            }

//                            mRemoteViews.setImageViewBitmap(R.id.img, bmUserImage);
//                            ImageLoader.getInstance().displayImage(items.getItem_picture_url(), R.id.img);
                            //Uri uri = Uri.parse((String) Url);
                            //mRemoteViews.setImageViewUri(R.id.img, uri);
                            mRemoteViews.setImageViewResource(R.id.img, R.mipmap.wemeet);
                            mRemoteViews.setTextViewText(R.id.context, Name+"已添加到购物车");
                            Intent mIntent = new Intent(context, ItemCommentList.class);
                            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(mPendingIntent);
                            builder.setSmallIcon(R.mipmap.logo)
                                    .setContent(mRemoteViews);

                            Notification notify = builder.build();
                            manager.notify(I++, notify);
                        }
                    });
        }
    }
}
