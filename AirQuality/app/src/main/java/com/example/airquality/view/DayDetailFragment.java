package com.example.airquality.view;

import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.airquality.Adapters.DayDetailAdapter;
import com.example.airquality.Adapters.HourAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentDayDetailBinding;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayDetailFragment extends Fragment {
    private FragmentDayDetailBinding binding;
    private DayDetailAdapter dayDetailAdapter;
    private AppDatabase appDatabase;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;

    private DailyAirQualityDAO dailyAirQualityDAO;
    private ArrayList<DailyAirQuality> dayArrayList;

    private Date date=new Timestamp(new Date().getTime());
    private String location="";
    private String stringDate="";
    private DateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
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
        binding= FragmentDayDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvDayDetail.setLayoutManager(layoutManager);
        binding.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Bundle bundle = this.getArguments();
        String[] data = (bundle.getString("Date-Location")).split(",");
        location = data[1];
        stringDate = data[0];
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayArrayList = new ArrayList<DailyAirQuality>();
        dayDetailAdapter = new DayDetailAdapter(hourArrayList);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    hourArrayList.clear();
                    dayArrayList.clear();
                    Date day;
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                    appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
                    hourlyAirQualityDAO  = appDatabase.hourlyAirQualityDAO();
                    dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
//                    for(HourlyAirQuality i: hourlyAirQualityDAO.getListByStationName()){
//                        day=new SimpleDateFormat("dd/MM/yyyy").parse(dayFormat.format(i.getDatetime()));
//                        if(day.compareTo(date)==0){
//                            hourArrayList.add(i);
//                        }
//                    }

                    for(DailyAirQuality i: dailyAirQualityDAO.getListByLocation(location)) {
                        day=new SimpleDateFormat("dd/MM/yyyy").parse(dayFormat.format(i.getDatetime()));
                        if (day.compareTo(date) == 0) {
                            dayArrayList.add(i);
                            break;
                        }
                    }
                    binding.tvDate.setText(dayFormat.format(dayArrayList.get(0).getDatetime()));
                    binding.tvLocation.setText(dayArrayList.get(0).getLocation());
                    binding.tvAqi.setText(String.valueOf((int)dayArrayList.get(0).getAQI()));
                    binding.tvRate.setText(dayArrayList.get(0).getRate());
                    setBackgroundColor(dayArrayList.get(0).getAQI());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        dayDetailAdapter.notifyDataSetChanged();
        binding.rvDayDetail.setAdapter(dayDetailAdapter);
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