package edu.stu.tool;

import edu.stu.ihelp.client.CaptureActivity;
import edu.stu.ihelp.client.General;
import edu.stu.ihelp.client.iHELP;
import edu.stu.ihelp.client.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class iHelpWidgetProvider extends AppWidgetProvider {
    
    public iHelpWidgetProvider() {

    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
       
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            // Get the layout for the iHelpWidget
            RemoteViews views = new RemoteViews (context.getPackageName(), R.layout.ihelper_widget);
            
            // Open iHelp
            {
                // Create an Intent to launch IHelpActivity
                Intent intent = new Intent (context, iHELP.class);
                PendingIntent pendingIntent = PendingIntent.getActivity (context, 2, intent, 0);
                
                // Attach on-click listener to the btnWidgetIHelp
                views.setOnClickPendingIntent (R.id.btnWidgetIHelp, pendingIntent);
            }
            
            // Open video recorder then open iHelp general
            {
                // Create an Intent to launch CaptureActivity with request code REQ_VIDEO_CAPTURE
                // Remind: getActivity parameter MUST use PendingIntent.FLAG_UPDATE_CURRENT to carry IntExtra for reqCode
                Intent intent = new Intent (context, CaptureActivity.class);
                intent.putExtra("reqCode", CaptureActivity.REQ_VIDEO_CAPTURE);
                PendingIntent pendingIntent = PendingIntent.getActivity (context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                // Attach on-click listener to the btnWidgetVideo
                views.setOnClickPendingIntent (R.id.btnWidgetVideo, pendingIntent);
            }
            
            // Open camera then open iHelp general
            {
                // Create an Intent to launch CaptureActivity with request code REQ_PHOTO_CAPTURE
             // Remind: getActivity parameter MUST use PendingIntent.FLAG_UPDATE_CURRENT to carry IntExtra for reqCode
                Intent intent = new Intent (context, CaptureActivity.class);
                intent.putExtra("reqCode", CaptureActivity.REQ_PHOTO_CAPTURE);
                PendingIntent pendingIntent = PendingIntent.getActivity (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                // Attach on-click listener to the btnWidgetPhoto
                views.setOnClickPendingIntent(R.id.btnWidgetPhoto, pendingIntent);
            }
                        
            // Open iHelp general
            {
                // Create an Intent to launch General
                Intent intent = new Intent(context, General.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                
                // Attach on-click listener to the btnWidgetGeneral
                views.setOnClickPendingIntent(R.id.btnWidgetGeneral, pendingIntent);
            }
            
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    
    @Override
    public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        
    }
   
    @Override
    public void onEnabled(Context context) {
        // Not necessary because it's just a shortcut widget
    }
    
    @Override
    public void onDisabled(Context context) {
        // Not necessary because it's just a shortcut widget
    }

    @Override
    public void  onReceive(Context context, Intent intent) {
        // Not necessary because we extends from AppWidgetProvider
        super.onReceive(context, intent);
    }
}
