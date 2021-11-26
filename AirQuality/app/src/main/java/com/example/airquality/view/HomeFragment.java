package com.example.airquality.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.airquality.Adapters.DayAdapter;
import com.example.airquality.Adapters.HourAdapter;
import com.example.airquality.Adapters.SpinnerAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentHomeBinding;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private SpinnerAdapter spinnerAdapter;
    private Location location;

    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;
    private HourlyAirQuality hourlyAirQuality;

    private ArrayList<DailyAirQuality> dayArrayList;

    private AppDatabase appDatabase;
    private FragmentHomeBinding binding;


    private String stringDay="";
    private String stringDayHour="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        stringDay = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        stringDayHour = new SimpleDateFormat("dd/MM/yyyy hh:00:00").format(new Date());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvHours.setLayoutManager(layoutManager);
        binding.rvDays.setLayoutManager(new LinearLayoutManager(getContext()));

        appDatabase = AppDatabase.Instance(requireContext().getApplicationContext());
        LocationDAO locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();

        ArrayList<HourlyAirQuality> hourList = new ArrayList<HourlyAirQuality>();
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayArrayList = new ArrayList<DailyAirQuality>();
        ArrayList<Location> locationArrayList = new ArrayList<Location>(locationDAO.getListHasMark());
//        location=locationDAO.getListHasMark().get(0);
//
//        hourlyAirQuality=hourlyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDayHour).get(0);
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(requireContext(),R.layout.spinner_items_category, locationArrayList);
        binding.snLocation.setAdapter(spinnerAdapter);
        binding.snLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                 location=locationArrayList.get(i);
//                 loadHome();
//                 loadHours();
//                 loadDays();
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });
//        loadHome();
//        loadHours();
//        loadDays();
    }

    @SuppressLint("DefaultLocale")
    private void loadHome() {
        if(location!=null) {
            binding.tvLocation.setText(location.getStationName());
            binding.tvAqi.setText(String.format("%.1f", location.getAqi()));
            binding.tvRate.setText(location.getRated());
            binding.tvDate.setText(stringDay);
            setBackgroundColor(location.getAqi());
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void loadHours() {
        HourAdapter.HourClickListener hourClickListener = new HourAdapter.HourClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                HourlyAirQuality hour = hourArrayList.get(i);
                bundle.putString("Date-LocationID", hour.getDatetime() + "," + hour.getLocationID());
                HourDetailFragment hourDetailFragment = new HourDetailFragment();
                hourDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, hourDetailFragment)
                        .addToBackStack(null).commit();
            }
        };
        hourArrayList.clear();

        HourAdapter hoursAdapter = new HourAdapter(hourArrayList, hourClickListener);
        hourArrayList.addAll(hourlyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDay));
        hoursAdapter.notifyDataSetChanged();
        binding.rvHours.setAdapter(hoursAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDays() {
        DayAdapter.DayClickListener dayClickListener = new DayAdapter.DayClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                DailyAirQuality day = dayArrayList.get(i);
                bundle.putString("Date-LocationID", day.getDate() + "," + day.getLocationID());
                DayDetailFragment dayDetailFragment = new DayDetailFragment();
                dayDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, dayDetailFragment)
                        .addToBackStack(null).commit();
            }
        };
        dayArrayList.clear();
        DayAdapter daysAdapter = new DayAdapter(dayArrayList, dayClickListener);
        appDatabase = AppDatabase.Instance(requireContext().getApplicationContext());
        DailyAirQualityDAO dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
        dayArrayList.addAll(dailyAirQualityDAO.getListByLocationID(location.getId()));
        daysAdapter.notifyDataSetChanged();
        binding.rvDays.setAdapter(daysAdapter);
    }
    private void setBackgroundColor(double aqi) {
        if (aqi <= 50)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_green));
        else if (aqi <= 100)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_yellow));
        else if (aqi <= 150)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_orange));
        else if (aqi <= 200)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_red));
        else if (aqi <= 300)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_purple));
        else if (aqi <= 500)
            requireView().setBackgroundColor(getResources().getColor(R.color.light_brown));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}