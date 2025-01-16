package com.example.graphicsmaker.create;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;

import android.widget.Toast;

import com.example.graphicsmaker.R;

import java.io.File;
import java.io.IOException;

public class ActivityDestroyService extends Service {
    private String newPath = "";
    private String oldPath = "";

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.newPath = intent.getStringExtra("newPath");
            this.oldPath = intent.getStringExtra("oldPath");
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (new File(this.newPath).exists()) {
            deleteFileFromPath(this.oldPath);
        }
        stopSelf();
    }

    private boolean deleteFileFromPath(String filePath) {
        boolean boolValue = false;
        try {
            File file = new File(filePath);
            boolValue = file.delete();
            if (file.exists()) {
                try {
                    boolValue = file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    boolValue = getApplicationContext().deleteFile(file.getName());
                }
            }
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e2) {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        return boolValue;
    }
}
