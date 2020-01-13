package com.android.anggiat.moviecatalogueapi.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.view.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MovieAppWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.android.anggiat.moviecatalogueapi.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.android.anggiat.moviecatalogueapi.EXTRA_ITEM";
    public static final String TOAST_UPDATE = "com.android.anggiat.moviecatalogueapi.TOAST_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_app_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, MovieAppWidget.class);
        toastIntent.setAction(MovieAppWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        updateWidgetFavorite(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
                if (intent.getAction().equals(TOAST_ACTION)) {
                    intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                    int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                    Intent intentWidget = new Intent(context, MainActivity.class);
                    context.startActivity(intentWidget);
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, MovieAppWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        }
        super.onReceive(context, intent);
    }

    static void updateWidgetFavorite(Context context) {
        Intent updateIntent = new Intent(context, MovieAppWidget.class);
        updateIntent.setAction(TOAST_UPDATE);
        context.sendBroadcast(updateIntent);
    }

}

