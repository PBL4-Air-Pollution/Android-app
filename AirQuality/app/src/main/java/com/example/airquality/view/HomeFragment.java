package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.airquality.Adapters.DayAdapter;
import com.example.airquality.Adapters.HourAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentHomeBinding;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private ArrayList<Location> locationArrayList;

    private HourAdapter.HourClickListener hourClickListener;
    private HourAdapter hoursAdapter;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;
    private ArrayList<HourlyAirQuality> hourList;

    private DayAdapter.DayClickListener dayClickListener;
    private DayAdapter daysAdapter;
    private DailyAirQualityDAO dailyAirQualityDAO;
    private ArrayList<DailyAirQuality> dayArrayList;

    private AppDatabase appDatabase;
    private LocationDAO locationDAO;
    private String locationName;
    private FragmentHomeBinding binding;

    private String stringToday="16/11/2021 03:00";
    private DateFormat dayHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private DateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DateFormat hourFormat = new SimpleDateFormat("HH:mm");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentHomeBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        ArrayList<String> locationString=new ArrayList<String>();
        locationString.addAll(locationDAO.getListNameByHasMark());
        locationName=locationString.get(0);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_items_category,locationString);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.snLocation.setAdapter(adapter);
        binding.snLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                locationName=locationString.get(position);
                binding.tvLocation.setText(locationName);
                loadHome();
                loadDays();
                loadHours();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadHome();
        loadDays();
        loadHours();
    }
    private  void loadHome(){
        hourList.clear();
        hourList.addAll(hourlyAirQualityDAO.getListByLocation(locationName));
        Date day,today;
        try {
            for(HourlyAirQuality i: hourList){
                day=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dayHourFormat.format(i.getDatetime()));
                today= new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(stringToday);
                if(day.compareTo(today)==0){
                    binding.tvLocation.setText(locationName);
                    binding.tvAqi.setText(String.valueOf((int)i.getAQI()));
                    binding.tvRate.setText(i.getRate());
                    binding.tvDate.setText(dayFormat.format(today));
                    setBackgroundColor(i.getAQI());
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setBackgroundColor(double aqi){
        if(aqi<=50) getView().setBackgroundColor(getResources().getColor(R.color.light_green));
        else if(aqi<=100) getView().setBackgroundColor(getResources().getColor(R.color.light_yellow));
        else if(aqi<=150) getView().setBackgroundColor(getResources().getColor(R.color.light_orange));
        else if(aqi<=200) getView().setBackgroundColor(getResources().getColor(R.color.light_red));
        else if(aqi<=300) getView().setBackgroundColor(getResources().getColor(R.color.light_purple));
        else if(aqi<=500) getView().setBackgroundColor(getResources().getColor(R.color.light_brown));
    }
    private void loadHours() {
        hourClickListener = new HourAdapter.HourClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle=new Bundle();
                bundle.putString("Date-Location",dayHourFormat.format(hourArrayList.get(i).getDatetime())+","+hourArrayList.get(i).getLocation());
                HourDetailFragment hourDetailFragment=new HourDetailFragment();
                hourDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_home,hourDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        hourArrayList.clear();
        hoursAdapter = new HourAdapter(hourArrayList, hourClickListener);
        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        hourlyAirQualityDAO  = appDatabase.hourlyAirQualityDAO();
        Date hour1,hour2;
        try {
            for(HourlyAirQuality i: hourlyAirQualityDAO.getListByLocation(locationName)){
                hour1=new SimpleDateFormat("dd/MM/yyyy").parse(dayFormat.format(i.getDatetime()));
                hour2=new SimpleDateFormat("dd/MM/yyyy").parse(stringToday);
                if(hour1.compareTo(hour2)==0) hourArrayList.add(i);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hoursAdapter.notifyDataSetChanged();
        binding.rvHours.setAdapter(hoursAdapter);

    }

    private void loadDays() {
        dayClickListener = new DayAdapter.DayClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle=new Bundle();
                bundle.putString("Date-Location",dayHourFormat.format(dayArrayList.get(i).getDatetime())+","+dayArrayList.get(i).getLocation());
                DayDetailFragment dayDetailFragment=new DayDetailFragment();
                dayDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_home,dayDetailFragment)
                            .addToBackStack(null)
                            .commit();
            }
        };
        dayArrayList.clear();
        daysAdapter = new DayAdapter(dayArrayList,dayClickListener);
        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        dailyAirQualityDAO= appDatabase.dailyAirQualityDAO();
        Date hour1,hour2;
        try {
            for(DailyAirQuality i: dailyAirQualityDAO.getListByLocation(locationName)){
                hour1=new SimpleDateFormat("dd/MM/yyyy").parse(dayFormat.format(i.getDatetime()));
                hour2=new SimpleDateFormat("dd/MM/yyyy").parse(stringToday);
                if(hour1.compareTo(hour2)<7) dayArrayList.add(i);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        daysAdapter.notifyDataSetChanged();
        binding.rvDays.setAdapter(daysAdapter);
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}