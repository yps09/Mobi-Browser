package com.android.viyobrowse.ui.notifications;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.viyobrowse.WebAdapter;
import com.android.viyobrowse.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private RecyclerView recyclerView;
    private NotificationsViewModel notificationsViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);



        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRecyclerView();
            }
        });

        final TextView textView = binding.textView;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void clearRecyclerView() {
        // Clear data in ViewModel


        // Clear data in RecyclerView adapter
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            WebAdapter adapter = (WebAdapter) recyclerView.getAdapter();
            adapter.clearData(); // You need to implement a clearData() method in your adapter
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}