package com.example.finalproject.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.finalproject.R;
import com.example.finalproject.ui.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Ambil data Judul dan ID yang dikirim dari Alarm
        String taskTitle = intent.getStringExtra("title");
        int taskId = intent.getIntExtra("id", 0);

        // 2. Munculkan Notifikasi
        showNotification(context, taskTitle, taskId);
    }

    private void showNotification(Context context, String title, int taskId) {
        String channelId = "task_deadline_channel";
        String channelName = "Task Deadlines";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Wajib buat Android Oreo ke atas: Bikin Channel Notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifikasi untuk deadline tugas");
            notificationManager.createNotificationChannel(channel);
        }

        // Intent supaya kalau notif diklik, aplikasi kebuka ke halaman utama
        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Desain Tampilan Notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // Ikon aplikasi
                .setContentTitle("Deadline Reminder! ⏰")
                .setContentText("Waktunya mengerjakan: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Hilang otomatis kalau diklik

        // Tampilkan!
        if (notificationManager != null) {
            notificationManager.notify(taskId, builder.build());
        }
    }
}