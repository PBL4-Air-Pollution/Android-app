package com.example.airquality.view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentHourDetailBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourDetailFragment extends Fragment {

    private FragmentHourDetailBinding binding;
    private String locationID,stringDayHour;

    private AppDatabase appDatabase;
    private LocationDAO locationDAO;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private HourlyAirQuality hourlyAirQuality;
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=this.getArguments();
        String[] data=(bundle.getString("Date-LocationID")).split(",");
        locationID=data[1];
        stringDayHour=data[0];

        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();
        hourlyAirQuality=hourlyAirQualityDAO.getListByLocationIDAndDate(Integer.parseInt(locationID),stringDayHour).get(0);
        String stringTime[]=hourlyAirQuality.getDatetime().split(" ");
        String stringHour[]=stringTime[1].split(":");
        binding.tvHour.setText(stringHour[0]+":00");
        binding.tvLocation.setText(locationDAO.getListByID(Integer.parseInt(locationID)).get(0).getStationName());
        binding.tvRate.setText(hourlyAirQuality.getRated());
        binding.tvAqi.setText(String.format("%.1f",hourlyAirQuality.getAqi()));
        binding.tvPM25.setText(String.format("%.1f",hourlyAirQuality.getPm25()));
        binding.tvPM10.setText(String.format("%.1f",hourlyAirQuality.getPm10()));
        binding.tvNO2.setText(String.format("%.1f",hourlyAirQuality.getNo2()));
        binding.tvCO.setText(String.format("%.1f",hourlyAirQuality.getCo()));
        binding.tvSO2.setText(String.format("%.1f",hourlyAirQuality.getSo2()));
        binding.tvO3.setText(String.format("%.1f",hourlyAirQuality.getO3()));
        setBackgroundColor(hourlyAirQuality.getAqi());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                Fragment fragment=fragmentManager.findFragmentById(R.id.fl_home);
                if(fragment!=null){
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }
    private void setBackgroundColor(double aqi){
        if(aqi<=50) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_green);
        else if(aqi<=100) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_yellow);
        else if(aqi<=150) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_orange);
        else if(aqi<=200) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_red);
        else if(aqi<=300) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_purple);
        else if(aqi<=500) binding.fmHomeDetail.setBackgroundResource(R.drawable.custom_background_brown);
    }

}