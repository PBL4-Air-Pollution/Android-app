package com.example.airquality.view;

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
import android.widget.ArrayAdapter;

import com.example.airquality.Adapters.DayAdapter;
import com.example.airquality.Adapters.DayDetailAdapter;
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private ArrayList<Location> locationArrayList;
    private SpinnerAdapter spinnerAdapter;
    private LocationDAO locationDAO;
    private Location location;

    private HourAdapter.HourClickListener hourClickListener;
    private HourAdapter hoursAdapter;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;
    private ArrayList<HourlyAirQuality> hourList;
    private HourlyAirQuality hourlyAirQuality;

    private DayAdapter.DayClickListener dayClickListener;
    private DayAdapter daysAdapter;
    private DailyAirQualityDAO dailyAirQualityDAO;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        stringDay=LocalDateTime.now().getDayOfMonth()+"/"+LocalDateTime.now().getMonthValue()+"/"+LocalDateTime.now().getYear();
        stringDayHour=LocalDateTime.now().getDayOfMonth()+"/"+LocalDateTime.now().getMonthValue()+"/"+LocalDateTime.now().getYear()+" "+LocalDateTime.now().getHour()+":00:00";

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvHours.setLayoutManager(layoutManager);
        binding.rvDays.setLayoutManager(new LinearLayoutManager(getContext()));

        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();

        hourList=new ArrayList<HourlyAirQuality>();
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayArrayList = new ArrayList<DailyAirQuality>();
        locationArrayList=new ArrayList<Location>();
        locationArrayList.addAll(locationDAO.getListHasMark());
        location=locationDAO.getListHasMark().get(0);

        hourlyAirQuality=hourlyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDayHour).get(0);
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(getContext(),R.layout.spinner_items_category,locationArrayList);
        binding.snLocation.setAdapter(spinnerAdapter);
        binding.snLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 location=locationArrayList.get(i);
                 loadHome();
//                 loadDays();
                 loadHours();
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });
         loadHome();
//         loadDays();
         loadHours();
    }

    private void loadHome() {
        if(location!=null) {
            binding.tvLocation.setText(location.getStationName());
            binding.tvAqi.setText(String.format("%.1f", location.getAqi()));
            binding.tvRate.setText(location.getRated());
            binding.tvDate.setText(stringDayHour);
            setBackgroundColor(location.getAqi());
        }
    }

    private void setBackgroundColor(double aqi) {
        if (aqi <= 50)
            getView().setBackgroundColor(getResources().getColor(R.color.light_green));
        else if (aqi <= 100)
            getView().setBackgroundColor(getResources().getColor(R.color.light_yellow));
        else if (aqi <= 150)
            getView().setBackgroundColor(getResources().getColor(R.color.light_orange));
        else if (aqi <= 200)
            getView().setBackgroundColor(getResources().getColor(R.color.light_red));
        else if (aqi <= 300)
            getView().setBackgroundColor(getResources().getColor(R.color.light_purple));
        else if (aqi <= 500)
            getView().setBackgroundColor(getResources().getColor(R.color.light_brown));
    }

    private void loadHours() {
        hourClickListener = new HourAdapter.HourClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                HourlyAirQuality hour=hourArrayList.get(i);
                bundle.putString("Date-LocationID", hour.getDatetime()+ ","
                        + hour.getLocationID());
                HourDetailFragment hourDetailFragment = new HourDetailFragment();
                hourDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, hourDetailFragment)
                        .addToBackStack(null).commit();
            }
        };
        hourArrayList.clear();

        hoursAdapter = new HourAdapter(hourArrayList, hourClickListener);
//        hourArrayList.addAll(hourlyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDay));
        int j=0;
        for(HourlyAirQuality i: hourlyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDay))
            hourArrayList.add(i);
        hoursAdapter.notifyDataSetChanged();
        binding.rvHours.setAdapter(hoursAdapter);
    }

    private void loadDays() {
        dayClickListener = new DayAdapter.DayClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putString("Date-Location", stringDay+ ","
                        + location.getStationName());
                DayDetailFragment dayDetailFragment = new DayDetailFragment();
                dayDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, dayDetailFragment)
                        .addToBackStack(null).commit();
            }
        };
        dayArrayList.clear();
        daysAdapter = new DayAdapter(dayArrayList, dayClickListener);
        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
        dayArrayList.addAll(dailyAirQualityDAO.getListByLocationIDAndDate(location.getId(),stringDay));
        Log.d("tag",dailyAirQualityDAO.getAll().size()+" ");
        daysAdapter.notifyDataSetChanged();
        binding.rvDays.setAdapter(daysAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}