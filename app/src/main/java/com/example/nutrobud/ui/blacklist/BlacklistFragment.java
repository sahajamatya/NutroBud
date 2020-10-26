package com.example.nutrobud.ui.blacklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nutrobud.R;

public class BlacklistFragment extends Fragment {

    private BlacklistViewModel blacklistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        blacklistViewModel =
                ViewModelProviders.of(this).get(BlacklistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blacklist, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        blacklistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}