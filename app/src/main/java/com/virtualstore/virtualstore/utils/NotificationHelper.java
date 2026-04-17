package com.virtualstore.virtualstore.utils;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.virtualstore.virtualstore.activities.admin.OrderDetailActivity;
import com.virtualstore.virtualstore.R;

public class NotificationHelper {

    private Context context;

    private static final String CHANNEL_ID = "admin_notifications";
    private static final int NOTIFICATION_ID = 101;

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = "Admin Alerts";
            String descriptionText = "Notifications for new orders and stock alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    name,
                    importance
            );

            channel.setDescription(descriptionText);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void showOrderNotification(String orderId, String customerName) {

        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("ORDER_ID", orderId);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Order Received!")
                .setContentText("Order #" + orderId + " from " + customerName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // ⚠ In real apps you should check notification permission (Android 13+)
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
