package com.android.viyobrowse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Stack;

public class ResultActivity extends AppCompatActivity {
    EditText search;
    private WebView webView;
    private String currentUrl;

    private ValueCallback<Uri[]> mFilePathCallback;
    Stack<String> historySites;
    ImageView backbtn;
    ImageView forwaddbtn;
    ImageView faviconIcon;

    ImageView customPopUpBtn;

    SwipeRefreshLayout swipeRefreshLayout;

    private static final int PICK_FILE_REQUEST_CODE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        historySites = new Stack<>();


        webView = findViewById(R.id.result);
        backbtn = findViewById(R.id.backbtn);
        forwaddbtn = findViewById(R.id.forwardbtn);
        search = findViewById(R.id.serchquery);
        faviconIcon = findViewById(R.id.faviconicon);
        customPopUpBtn = findViewById(R.id.cutompopup);
        swipeRefreshLayout = findViewById(R.id.refreshPage);


        // Add this line to register the WebView for the context menu
        registerForContextMenu(webView);



        String query = getIntent().getStringExtra("query");
        String url = getIntent().getStringExtra("url");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setDownloadListener(new MyDownloadListener());


        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
            loadFavicon(url);
        } else {
            String websiteUrl = "https://www.google.com/search?q=" + query;
            webView.loadUrl(websiteUrl);
//            saveSearchHistory(websiteUrl);
            loadFavicon(websiteUrl);
        }


//        String websiteUrl = "https://www.google.com/search?q=" + query;
//        webView.loadUrl(websiteUrl);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        loadInitialUrl();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    // If WebView can go back, navigate back
                    webView.goBack();
                } else if (!historySites.isEmpty()) {
                    // If WebView cannot go back but history is not empty, pop the history
                    historySites.pop();
                    if (!historySites.isEmpty()) {
                        // Load the previous URL from history
                        String previousURL = historySites.peek();
                        webView.loadUrl(previousURL);
                    }
                } else {
                    // If history is empty, finish the activity and go back to BrowserActivity
                    finish();
                    startActivity(new Intent(ResultActivity.this, BrowserActivity.class));
                }
            }
        });

        forwaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                } else {
                    Toast.makeText(ResultActivity.this, "This is the last screen of your search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadInitialUrl() {
        String query = getIntent().getStringExtra("query");
        String url = getIntent().getStringExtra("url");

        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
            loadFavicon(url);
        } else {
            String websiteUrl = "https://www.google.com/search?q=" + query;
            webView.loadUrl(websiteUrl);
            loadFavicon(websiteUrl);
        }
    }
    private void loadFavicon(String url) {
        String faviconUrl = "https://www.google.com/s2/favicons?domain=" + url;
        Glide.with(ResultActivity.this)
                .load(faviconUrl)
                .into(faviconIcon);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            currentUrl = url;
            search.setText(currentUrl);
            historySites.push(currentUrl);
            loadFavicon(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            loadFavicon(url);

            // Customize this method based on your specific URL handling logic
            if (url.contains("example.com/response1")) {
                performActionForResponse1();
                return true;
            } else if (url.contains("example.com/response2")) {
                performActionForResponse2();
                return true;
            } else {
                return false;
            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            // Handle file upload logic
            boolean result = true;
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, 100);
            } catch (Exception e) {
                mFilePathCallback = null;
                Toast.makeText(ResultActivity.this, "Cannot open file chooser", Toast.LENGTH_SHORT).show();
                result = false;
            }

            return result;
        }




    }

    // Custom DownloadListener to handle downloads
    private class MyDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            // Extract the file name from the contentDisposition
            String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

            // Confirm download with a dialog
            showDownloadConfirmationDialog(url, fileName);
        }
    }

    private void showDownloadConfirmationDialog(final String url, final String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Download")
                .setMessage("Do you want to download this file?")
                .setPositiveButton("Yes", (dialog, which) -> startDownload(url, fileName))
                .setNegativeButton("No", null)
                .show();
    }

    private static final int PERMISSION_REQUEST_NOTIFICATION = 1;
    private boolean startDownload(String url, String fileName) {
        // Check if the app has the notification permission

        if (hasNotificationPermission()) {
            // Proceed with creating and displaying the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(R.drawable.viyo_logo)
                    .setContentTitle("Download Complete")
                    .setContentText("Your file has been downloaded.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Set the intent that will open when the user taps the notification
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), getMimeType(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_NOTIFICATION);
                return false;
            }
            notificationManager.notify(123, builder.build());

            // Start the download
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(fileName);
            request.setDescription("Please wait...");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                handleDownloadCompletion(downloadManager, request);
            }
        } else {
            // Request notification permission from the user
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_NOTIFICATION);
        }
        return false;
    }

    private boolean hasNotificationPermission() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    private String getMimeType(String url)
    {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private void handleDownloadCompletion(DownloadManager downloadManager, DownloadManager.Request request) {

        long downloadId = downloadManager.enqueue(request);
        // Call the helper method to handle download completion
        handleDownloadCompletion(downloadId);
    }

    private void handleDownloadCompletion(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        android.database.Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                @SuppressLint("Range") String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                showDownloadCompleteNotification(localUri);

            } else {
                Toast.makeText(this, "Download failed", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
    }

    @SuppressLint("MissingPermission")
    private void showDownloadCompleteNotification(String localUri) {
        // Check if the app has the notification permission
        if (hasNotificationPermission()) {
            // Proceed with creating and displaying the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(R.drawable.viyo_logo)
                    .setContentTitle("Download Complete")
                    .setContentText("Your file has been downloaded.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Set the intent that will open when the user taps the notification
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(localUri), "application/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(123, builder.build());
        }
        else {
            // Request notification permission from the user
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_NOTIFICATION);
        }
    }

    private void performActionForResponse1() {
        // Customize this method based on your logic for response 1
        search.setText(webView.getUrl());
        // Additional actions...
    }

    private void performActionForResponse2() {
        // Customize this method based on your logic for response 2
        search.setText(webView.getUrl());
        // Additional actions...
    }

    private void differentiateUrlAndQuery(String input) {
        if (isValidUrl(input)) {
            // It's a URL
            Toast.makeText(this, "Input is a URL: " + input, Toast.LENGTH_SHORT).show();
        } else {
            // It's a search query
            Toast.makeText(this, "Input is a search query: " + input, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.result) {
            WebView.HitTestResult result = webView.getHitTestResult();
            String imageUrl = result.getExtra();

            if (result.getType() == WebView.HitTestResult.IMAGE_TYPE || result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                menu.setHeaderTitle("Options");
                menu.add(Menu.NONE, 1, Menu.NONE, "Download Image");
                menu.add(Menu.NONE, 2, Menu.NONE, "Copy Image Url");
                menu.add(Menu.NONE, 3, Menu.NONE, "Share Image Url");
                menu.add(Menu.NONE, 4, Menu.NONE, "Copy Image");
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                // Download Image action
                downloadImage();
                return true;
            case 2:
                // Copy Image Url action
                copyImageUrl();
                return true;
            case 3:
                // Share Image Url action
                shareImageUrl();
                return true;
            case 4:
                // Copy Image action
                copyImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void downloadImage() {
        WebView.HitTestResult result = webView.getHitTestResult();
        String imageUrl = result.getExtra();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Create a DownloadManager.Request with the image URL
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
            // Set the title of the download notification
            request.setTitle("Downloading Image");
            // Set the description of the download notification
            request.setDescription("Please wait while the image is being downloaded...");

            // Specify the local destination for the downloaded image
            String fileName = URLUtil.guessFileName(imageUrl, null, null);
            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            request.setDestinationUri(Uri.fromFile(destinationFile));

            // Get the DownloadManager service and enqueue the download
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                long downloadId = downloadManager.enqueue(request);

                // Optionally, you can listen for download completion or failure
                // and show a notification or handle the result accordingly.

                // Display a toast to indicate the download has started
                Toast.makeText(this, "Downloading Image...", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where DownloadManager is not available on the device
                Toast.makeText(this, "DownloadManager is not available on this device", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the image URL is null or empty
            Toast.makeText(this, "Unable to retrieve image URL", Toast.LENGTH_SHORT).show();
        }
    }


    private void copyImageUrl() {
        WebView.HitTestResult result = webView.getHitTestResult();
        String imageUrl = result.getExtra();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Get the clipboard manager
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            // Create a ClipData object to hold the image URL
            ClipData clipData = ClipData.newPlainText("Image URL", imageUrl);

            // Set the ClipData on the clipboard
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);

                // Display a toast to indicate the copy action
                Toast.makeText(this, "Image URL copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where ClipboardManager is not available on the device
                Toast.makeText(this, "ClipboardManager is not available on this device", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the image URL is null or empty
            Toast.makeText(this, "Unable to retrieve image URL", Toast.LENGTH_SHORT).show();
        }
    }


    private void shareImageUrl() {
        WebView.HitTestResult result = webView.getHitTestResult();
        String imageUrl = result.getExtra();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Create a share intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);

            // Start the activity to show the share dialog
            startActivity(Intent.createChooser(shareIntent, "Share Image URL"));

            // Display a toast to indicate the share action
            Toast.makeText(this, "Sharing Image URL...", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where the image URL is null or empty
            Toast.makeText(this, "Unable to retrieve image URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyImage() {
        WebView.HitTestResult result = webView.getHitTestResult();
        String imageUrl = result.getExtra();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Download the image
            downloadImageForCopying(imageUrl);
        } else {
            // Handle the case where the image URL is null or empty
            Toast.makeText(this, "Unable to retrieve image URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImageForCopying(String imageUrl) {
        // Use your preferred image downloading library or implement your logic to download the image
        // For example, you can use Picasso, Glide, or Android's own DownloadManager

        // Once the image is downloaded, you can copy it to the clipboard
        // Here, we assume you have a method to download the image and provide the Bitmap

        // Implement the logic to download the image and provide a Bitmap
        // ...

        // Get the clipboard manager
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null) {
            // Create a ClipData object with the image
            ClipData clipData = ClipData.newPlainText("Image", "Image from URL");
            ClipData.Item item = new ClipData.Item("Image from URL");

            // Add the item to the ClipData
            clipData.addItem(item);

            // Set the ClipData on the clipboard
            clipboardManager.setPrimaryClip(clipData);

            // Display a toast to indicate the copy action
            Toast.makeText(this, "Image copied to clipboard", Toast.LENGTH_SHORT).show();
        }
        else {
            // Handle the case where ClipboardManager is not available on the device
            Toast.makeText(this, "ClipboardManager is not available on this device", Toast.LENGTH_SHORT).show();
        }
    }
}
