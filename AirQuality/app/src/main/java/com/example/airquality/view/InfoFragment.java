package com.example.airquality.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airquality.R;
import com.example.airquality.databinding.FragmentInfoBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.sv1.scrollTo(0, 0);
            }
        });


    }



}