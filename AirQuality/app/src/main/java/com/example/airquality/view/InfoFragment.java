package com.example.airquality.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.adapters.AbsListViewBindingAdapter;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.sv1.scrollTo(0, 0);

            }
        });

        binding.btnScroll.hide();
        binding.sv1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
              if (scrollY == 0)
              {
                  binding.btnScroll.hide();
              }
              else if (scrollY != oldScrollY)
              {
                  binding.btnScroll.show();
              }
            }

        });
       binding.tvContent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tv1;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tv2;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tv3;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvA;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvB;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvC;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvD;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvE;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvF;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvG;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContenth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvH;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvK;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvL;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });
        binding.tvContentm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = binding.tvM;
                binding.sv1.scrollTo((int) view1.getX(),(int)view1.getY());
            }
        });





    }



}