package com.android.viyobrowse.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.viyobrowse.PvtResultActivity;
import com.android.viyobrowse.ResultActivity;
import com.android.viyobrowse.databinding.FragmentPvtBrowsingBinding;

public class PvtBrowsingFragment extends Fragment {

    private FragmentPvtBrowsingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentPvtBrowsingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.editQuery.getText().toString().trim();
                Intent intent = new Intent(requireContext(), PvtResultActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
            }
        });


        final TextView textView = binding.textView;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}