package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.R;
import com.example.airquality.databinding.FragmentHourDetailBinding;

public class HourDetailFragment extends Fragment {

    private FragmentHourDetailBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHourDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
//        HourlyAirQuality hour=new HourlyAirQuality("Hòa Khánh Bắc","1AM", 50,25 ,30,20,20,20,50,"Tốt");
//
//        binding.tvLocation.setText(hour.getLocation());
//        binding.tvHour.setText(hour.getDatetime());
//        binding.tvAqi.setText(Double.toString(hour.getAQI()));
//        binding.tvPM25.setText(Double.toString(hour.getPM25()));
//        binding.tvPM10.setText(Double.toString(hour.getPM10()));
//        binding.tvNO2.setText(Double.toString(hour.getNO2()));
//        binding.tvCO.setText(Double.toString(hour.getCO()));
//        binding.tvSO2.setText(Double.toString(hour.getSO2()));
//        binding.tvO3.setText(Double.toString(hour.getO3()));


    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_back,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_back){
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();

            Fragment fragment=fragmentManager.findFragmentById(R.id.fl_home);
            if(fragment!=null){
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}