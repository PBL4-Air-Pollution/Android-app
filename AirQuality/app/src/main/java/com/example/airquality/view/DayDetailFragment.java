package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.Adapters.DayDetailAdapter;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentDayDetailBinding;
import com.example.airquality.model.HourlyAirQuality;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class DayDetailFragment extends Fragment {
    private FragmentDayDetailBinding binding;
    private RecyclerView rvDayDetail;
    private DayDetailAdapter dayDetailAdapter;
    private ArrayList<HourlyAirQuality> hourArrayList;
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
        binding= FragmentDayDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvDayDetail.setLayoutManager(layoutManager);
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayDetailAdapter = new DayDetailAdapter(hourArrayList);
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));

        dayDetailAdapter.notifyDataSetChanged();
        binding.rvDayDetail.setAdapter(dayDetailAdapter);
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