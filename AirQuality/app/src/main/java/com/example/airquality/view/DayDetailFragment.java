package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.Adapters.DayDetailAdapter;
import com.example.airquality.Adapters.HourAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentDayDetailBinding;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.util.ArrayList;
import java.util.Collections;

public class DayDetailFragment extends Fragment {
    private FragmentDayDetailBinding binding;
    private DayDetailAdapter dayDetailAdapter;
    private AppDatabase appDatabase;
    private LocationDAO locationDAO;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;

    private DailyAirQualityDAO dailyAirQualityDAO;
    private DailyAirQuality dailyAirQuality;

    private String stringDay;
    private int locationID=0;

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


        Bundle bundle = this.getArguments();
        String[] data = (bundle.getString("Date-LocationID")).split(",");
        locationID = Integer.parseInt(data[1]);
        stringDay = data[0];
        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        dailyAirQualityDAO=appDatabase.dailyAirQualityDAO();
        hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();

        dailyAirQuality=dailyAirQualityDAO.getListByLocationIDAndDate(locationID,stringDay);
        binding.tvDate.setText(stringDay);
        binding.tvLocation.setText(locationDAO.getByID(dailyAirQuality.getLocationID()).getStationName());
        binding.tvAqi.setText((int)dailyAirQuality.getAqi()+"");
        binding.tvRate.setText(dailyAirQuality.getRated());
        setBackgroundColor(dailyAirQuality.getAqi());

        HourAdapter.HourClickListener hourClickListener = new HourAdapter.HourClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                HourlyAirQuality hour = hourArrayList.get(i);
                bundle.putString("Date-LocationID", hour.getDatetime() + "," + hour.getLocationID());
                HourDetailFragment hourDetailFragment = new HourDetailFragment();
                hourDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_home, hourDetailFragment).addToBackStack(null).commit();
            }
        };

        // Get hourly data of current station
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayDetailAdapter = new DayDetailAdapter(hourArrayList,hourClickListener);
        hourArrayList.addAll(hourlyAirQualityDAO.getListByLocationIDAndDate(locationID,stringDay));
        dayDetailAdapter.notifyDataSetChanged();
        binding.rvDayDetail.setAdapter(dayDetailAdapter);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }
    private void setBackgroundColor(double aqi){
        if(aqi<=50) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_green);
        else if(aqi<=100) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_yellow);
        else if(aqi<=150) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_orange);
        else if(aqi<=200) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_red);
        else if(aqi<=300) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_purple);
        else if(aqi<=500) binding.fmDaydetail.setBackgroundResource(R.drawable.custom_background_brown);
    }


}