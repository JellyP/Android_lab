package com.example.administrator.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.StaticLayout;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
/*静态广播实现widget跳转到商品详情*/
public class mWidget extends AppWidgetProvider {
    Goods temp;
    AppWidgetManager appWidgetManager;
    PendingIntent mPendingIntent;
    RemoteViews remoteViews;
    public final static String STATICACTION=new String("MystaticFliters");
    @Override
    public void onReceive(Context context, Intent intent) {
        /*重写onReceive函数即可，实现方式类似于上次的通知实现*/
        super.onReceive(context, intent);
        Intent intentForStaticWidget=new Intent();
        temp=(Goods) intent.getExtras().get("random");
        /*由于功能上没有什么不同，所以可以借用上次通知的时候采用的静态广播来实现*/
        appWidgetManager=AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(STATICACTION))
        {
            intentForStaticWidget.setClass(context,GoodDetail.class);//设置跳转
            intentForStaticWidget.putExtra("goods",temp);//设置传输的key和对应的商品
            mPendingIntent= PendingIntent.getActivity(context,0,intentForStaticWidget,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews=new RemoteViews(context.getPackageName(),R.layout.m_widget);

            remoteViews.setImageViewResource(R.id.appwidget_image,temp.getImageid());
            remoteViews.setTextViewText(R.id.appwidget_text,temp.getName()+"仅售"+temp.getPrice());
            remoteViews.setOnClickPendingIntent(R.id.appwidget_relativeLayout,mPendingIntent);
            ComponentName componentName=new ComponentName(context,mWidget.class);
            appWidgetManager.updateAppWidget(componentName,remoteViews);/*更新widget*/
        }


    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

