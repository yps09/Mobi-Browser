package com.android.viyobrowse.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.viyobrowse.R;
import com.android.viyobrowse.ResultActivity;
import com.android.viyobrowse.WebviewController;
import com.android.viyobrowse.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ImageView imageView;
    private final  int GALLERY_REQ_CODE =1000;
    Button openGallery;
    private Uri selectedImageURI;


    ImageView popupimg;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = root.findViewById(R.id.NewsWebview);
        webView .loadUrl("https://news.google.com/home?hl=en-IN&gl=IN&ceid=IN:en");
        webView.setWebViewClient(new WebviewController());



        binding.searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.editQuery.getText().toString().trim();
                Intent intent = new Intent(requireContext(), ResultActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
            }
        });

        //for interval time



//        binding.cutompopup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCustomPopup();            }
//        });


        binding.googlrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),ResultActivity.class);
                intent.putExtra("url","https://www.google.com/");
                startActivity(intent);
            }
        });

        binding.youtubebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),ResultActivity.class);
                intent.putExtra("url", "https://www.youtube.com/");
                startActivity(intent);
            }
        });

        binding.instabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),ResultActivity.class);
                intent.putExtra("url", "https://www.instagram.com/");
                startActivity(intent);
            }
        });

        final TextView textView = binding.textView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


//    private void showCustomPopup() {
//        // Create a custom layout for the popup
//        View popupView = getLayoutInflater().inflate(R.layout.custompopup, null);
//
//        // Create a PopupWindow with the specified width and height
//        PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//
//        // Set focusable to true to enable touch events outside of the popup
//        popupWindow.setFocusable(true);
//
//        // Set up TextViews inside the popup
//        TextView copy = popupView.findViewById(R.id.CopyUrl);
//        TextView share = popupView.findViewById(R.id.Sharingbtn);
//        TextView newtab = popupView.findViewById(R.id.NewTab);
//        TextView close = popupView.findViewById(R.id.Closetab);
//
//        // Set up button click listeners
//        copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle copy button click
//                // You can implement the copy functionality here
//                popupWindow.dismiss(); // Dismiss the popup after performing the action
//            }
//        });
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle share button click
//                // You can implement the share functionality here
//                popupWindow.dismiss(); // Dismiss the popup after performing the action
//            }
//        });
//
//        newtab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle new tab button click
//                // You can implement the new tab functionality here
//                popupWindow.dismiss(); // Dismiss the popup after performing the action
//            }
//        });
//
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle close button click
//                // You can implement the close functionality here
//                popupWindow.dismiss(); // Dismiss the popup after performing the action
//            }
//        });
//
//        // Show the popup at a specific location on the screen
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}