package com.accessibilitymanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        SharedPreferences sp = context.getSharedPreferences("data", 0);

        if (!sp.getBoolean("boot", true)) return;
        if (sp.getString("daemon", "").isEmpty()) return;

        // 延迟5秒启动，以确保系统服务准备就绪
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent serviceIntent = new Intent(context, daemonService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }, 5000);
    }
}