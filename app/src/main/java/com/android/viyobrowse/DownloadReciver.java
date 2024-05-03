package com.android.viyobrowse;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId != -1) {
            handleDownloadCompletion(context, downloadId);
        }
    }

    private void handleDownloadCompletion(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = null;

        try {
            cursor = downloadManager.query(query);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    @SuppressLint("Range") String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    showDownloadCompleteNotification(context, localUri);
//                    Toast.makeText(context, "Download Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showDownloadCompleteNotification(Context context, String localUri) {
        if (localUri != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId")
                    .setSmallIcon(R.drawable.baseline_download_for_offline_24)
                    .setContentTitle("Download Complete")
                    .setContentText("Your file has been downloaded.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Set the intent that will open when the user taps the notification
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);

            // Assuming localUri is a valid file URI, you might want to perform additional validation here
            Uri contentUri = Uri.parse(localUri);

            String mimeType = context.getContentResolver().getType(contentUri);
            openFileIntent.setDataAndType(contentUri, mimeType);
            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            PendingIntent openFilePendingIntent = PendingIntent.getActivity(context, 0, openFileIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Create the notification manager and show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(123, builder.build());
        } else {
            // Handle the case where localUri is null
            Toast.makeText(context, "Local URI is null", Toast.LENGTH_SHORT).show();
        }
    }


}
