package com.android.viyobrowse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.viyobrowse.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    private static final int SPLASH_TIMER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       binding.viyotext.setText("Viyo 🧜🏽‍♂️" + "\n" + "Browser");

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {

               startActivity(new Intent(SplashActivity.this,BrowserActivity.class));
           }
       },SPLASH_TIMER);

    }
}