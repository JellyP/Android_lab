package com.example.administrator.lab3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
/*动态实现跳转到购物车列表*/
public class mDynamicWidget extends AppWidgetProvider {
    public final static String DYNAMICACTION=new String("MyDynamicFliters");
    AppWidgetManager appWidgetManager;
    PendingIntent mPenddingIntent;
    RemoteViews remoteViews;
    Goods temp;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Intent intentForDynamicWidget=new Intent();
        temp=(Goods) intent.getExtras().get("dynamic");
        appWidgetManager=AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(DYNAMICACTION))
        {
            intentForDynamicWidget.setClass(context,MainActivity.class);//设置跳转界面
            mPenddingIntent= PendingIntent.getActivity(context,0,intentForDynamicWidget,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews=new RemoteViews(context.getPackageName(),R.layout.m_widget);
            remoteViews.setImageViewResource(R.id.appwidget_image,temp.getImageid());
            remoteViews.setTextViewText(R.id.appwidget_text,temp.getName()+"已添加到购物车!");
            remoteViews.setOnClickPendingIntent(R.id.appwidget_relativeLayout,mPenddingIntent);
            ComponentName componentName=new ComponentName(context,mWidget.class);
            appWidgetManager.updateAppWidget(componentName,remoteViews);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_dynamic_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


         //There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

