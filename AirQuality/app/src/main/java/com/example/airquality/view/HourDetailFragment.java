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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourDetailFragment extends Fragment {

    private FragmentHourDetailBinding binding;
    private Date date=new Timestamp(new Date().getTime());
    private String location,stringDate;
    private DateFormat hourFormat = new SimpleDateFormat("HH:mm");
    private DateFormat dayHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
        String[] data=(bundle.getString("Date-Location")).split(",");
         location=data[1];
         stringDate=data[0];
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<HourlyAirQuality> hourArrayList=new ArrayList<HourlyAirQuality>();
                try {
                    date=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(stringDate);
                    AppDatabase appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
                    HourlyAirQualityDAO hourlyAirQualityDAO  = appDatabase.hourlyAirQualityDAO();
                    for(HourlyAirQuality i: hourlyAirQualityDAO.getListByLocation(location))
                        if(i.getDatetime().compareTo(date)==0){
                            hourArrayList.add(i);
                            break;
                        }

                    binding.tvHour.setText(hourFormat.format(hourArrayList.get(0).getDatetime()));
                    binding.tvLocation.setText(hourArrayList.get(0).getLocation());
                    binding.tvRate.setText(hourArrayList.get(0).getRate());
                    binding.tvAqi.setText(Double.toString(hourArrayList.get(0).getAQI()));
                    binding.tvPM25.setText(Double.toString(hourArrayList.get(0).getPM25()));
                    binding.tvPM10.setText(Double.toString(hourArrayList.get(0).getPM10()));
                    binding.tvNO2.setText(Double.toString(hourArrayList.get(0).getNO2()));
                    binding.tvCO.setText(Double.toString(hourArrayList.get(0).getCO()));
                    binding.tvSO2.setText(Double.toString(hourArrayList.get(0).getSO2()));
                    binding.tvO3.setText(Double.toString(hourArrayList.get(0).getO3()));
                    setBackgroundColor(hourArrayList.get(0).getAQI());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private void setBackgroundColor(double aqi){
        if(aqi<=50) getView().setBackgroundColor(getResources().getColor(R.color.light_green));
        else if(aqi<=100) getView().setBackgroundColor(getResources().getColor(R.color.light_yellow));
        else if(aqi<=150) getView().setBackgroundColor(getResources().getColor(R.color.light_orange));
        else if(aqi<=200) getView().setBackgroundColor(getResources().getColor(R.color.light_red));
        else if(aqi<=300) getView().setBackgroundColor(getResources().getColor(R.color.light_purple));
        else if(aqi<=500) getView().setBackgroundColor(getResources().getColor(R.color.light_brown));
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