package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.DayAdapter;
import com.example.airquality.HourAdapter;
import com.example.airquality.R;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private RecyclerView rvHours;
    private HourAdapter.HourClickListener hourClickListener;
    private HourAdapter hoursAdapter;
    private ArrayList<HourlyAirQuality> hourArrayList;


    private RecyclerView rvDays;
    private DayAdapter.DayClickListener dayClickListener;
    private DayAdapter daysAdapter;
    private ArrayList<DailyAirQuality> dayArrayList;
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        rvHours=view.findViewById(R.id.rv_hours);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHours.setLayoutManager(layoutManager);

        rvDays=view.findViewById(R.id.rv_days);
        rvDays.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDays();
        loadHours();
        return view;
    }

    private void loadHours() {
        hourArrayList = new ArrayList<HourlyAirQuality>();
        hourClickListener = new HourAdapter.HourClickListener() {
            @Override
            public void onCLick(View view, int i) {
                HourDetailFragment hourDetailFragment=new HourDetailFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,hourDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        hoursAdapter = new HourAdapter(hourArrayList, hourClickListener);
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc",new Timestamp(new Date().getTime()), 50,25 ,30,20,20,20,100,"Xấu"));

        hoursAdapter.notifyDataSetChanged();
        rvHours.setAdapter(hoursAdapter);
    }

    private void loadDays() {
        dayArrayList = new ArrayList<DailyAirQuality>();
        dayClickListener = new DayAdapter.DayClickListener() {
            @Override
            public void onCLick(View view, int i) {
                DayDetailFragment dayDetailFragment=new DayDetailFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,dayDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        daysAdapter = new DayAdapter(dayArrayList,dayClickListener);
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 50, "Tốt"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 100, "Xấu"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 50, "Tốt"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 100, "Xấu"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 50, "Tốt"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 100, "Xấu"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 50, "Tốt"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 100, "Xấu"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 50, "Tốt"));
        dayArrayList.add(new DailyAirQuality(new Timestamp(new Date().getTime()), 100, "Xấu"));
        daysAdapter.notifyDataSetChanged();
        rvDays.setAdapter(daysAdapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}