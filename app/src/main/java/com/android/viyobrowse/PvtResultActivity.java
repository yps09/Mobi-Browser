package com.android.viyobrowse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PvtResultActivity extends AppCompatActivity {
    WebView webView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvt_result);

        webView = findViewById(R.id.result);

        String query = getIntent().getStringExtra("query");

        // Enable JavaScript (if your website requires it)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set up WebViewClient to open links within the WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Open links within the WebView itself
                view.loadUrl(url);
                return true;
            }
        });

        // Load a website URL
        String websiteUrl = "https://www.google.com/search?q=" + query; // Replace with your desired website URL
        webView.loadUrl(websiteUrl);
    }

}