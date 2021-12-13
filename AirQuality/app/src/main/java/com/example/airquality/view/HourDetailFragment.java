package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentHourDetailBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

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
        binding.tvLocation.setText(locationDAO.getByID(Integer.parseInt(locationID)).getStationName());
        binding.tvRate.setText(hourlyAirQuality.getRated());
        binding.tvAqi.setText((int)hourlyAirQuality.getAqi()+"");
        setBackgroundColor(hourlyAirQuality.getAqi());
        setText(hourlyAirQuality);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
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

    private void setText(HourlyAirQuality hourlyAirQuality) {
        binding.tvPM25.setText(String.format("%.1f",hourlyAirQuality.getPm25()));
        setViewColorPM25(hourlyAirQuality.getPm25());
        binding.tvPM10.setText(String.format("%.1f",hourlyAirQuality.getPm10()));
        setViewColorPM10(hourlyAirQuality.getPm10());
        binding.tvO3.setText(String.format("%.1f",hourlyAirQuality.getO3()));
        setViewColorO3(hourlyAirQuality.getO3());
        binding.tvNO2.setText(String.format("%.1f",hourlyAirQuality.getNo2()));
        setViewColorNO2(hourlyAirQuality.getNo2());
        binding.tvSO2.setText(String.format("%.1f",hourlyAirQuality.getSo2()));
        setViewColorSO2(hourlyAirQuality.getSo2());
        binding.tvCO.setText(String.format("%.1f",hourlyAirQuality.getCo()));
        setViewColorCO(hourlyAirQuality.getCo());
        binding.tvNO.setText(String.format("%.1f",hourlyAirQuality.getNo()));
        setViewColorNO(hourlyAirQuality.getNo());
        binding.tvNOx.setText(String.format("%.1f",hourlyAirQuality.getNox()));
        setViewColorNOx(hourlyAirQuality.getNox());
        binding.tvNH3.setText(String.format("%.1f",hourlyAirQuality.getNh3()));
        setViewColorNH3(hourlyAirQuality.getNh3());
        binding.tvBenzen.setText(String.format("%.1f",hourlyAirQuality.getBenzene()));
        setViewColorBenzen(hourlyAirQuality.getBenzene());
        binding.tvToluene.setText(String.format("%.1f",hourlyAirQuality.getToluene()));
        setViewColorToluene(hourlyAirQuality.getToluene());
        binding.tvXylene.setText(String.format("%.1f",hourlyAirQuality.getXylene()));
        setViewColorXylene(hourlyAirQuality.getXylene());
    }

    private void setViewColorPM25(double pm25) {
        if(pm25<=30) binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.green));
        else if(pm25<=60) binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(pm25<=90) binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(pm25<=120) binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.red));
        else if(pm25<=250) binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorPM10(double pm10) {
        if(pm10<=50) binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.green));
        else if(pm10<=100) binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(pm10<=250) binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(pm10<=350) binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.red));
        else if(pm10<=430) binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorO3(double o3) {
        if(o3<=50) binding.viewO3.setBackgroundColor(getResources().getColor(R.color.green));
        else if(o3<=100) binding.viewO3.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(o3<=168) binding.viewO3.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(o3<=208) binding.viewO3.setBackgroundColor(getResources().getColor(R.color.red));
        else if(o3<=748) binding.viewO3.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewO3.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorNO2(double no2) {
        if(no2<=40) binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.green));
        else if(no2<=80) binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(no2<=180) binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(no2<=280) binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.red));
        else if(no2<=400) binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorSO2(double so2) {
        if(so2<=40) binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.green));
        else if(so2<=80) binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(so2<=380) binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(so2<=800) binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.red));
        else if(so2<=1600) binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorCO(double co) {
        if(co<=1) binding.viewCO.setBackgroundColor(getResources().getColor(R.color.green));
        else if(co<=2) binding.viewCO.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(co<=10) binding.viewCO.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(co<=17) binding.viewCO.setBackgroundColor(getResources().getColor(R.color.red));
        else if(co<=34) binding.viewCO.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewCO.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorNO(double no) {
        if(no<=200) binding.viewNO.setBackgroundColor(getResources().getColor(R.color.green));
        else if(no<=400) binding.viewNO.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(no<=800) binding.viewNO.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(no<=1200) binding.viewNO.setBackgroundColor(getResources().getColor(R.color.red));
        else if(no<=1800) binding.viewNO.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewNO.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorNOx(double nox) {
        if(nox<=200) binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.green));
        else if(nox<=400) binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(nox<=800) binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(nox<=1200) binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.red));
        else if(nox<=1800) binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewNOx.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorNH3(double nh3) {
        if(nh3<=200) binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.green));
        else if(nh3<=400) binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(nh3<=800) binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(nh3<=1200) binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.red));
        else if(nh3<=1800) binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewNH3.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorBenzen(double benzen) {
        if(benzen<=40) binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.green));
        else if(benzen<=80) binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(benzen<=380) binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(benzen<=800) binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.red));
        else if(benzen<=1600) binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewBenzen.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorToluene(double toluene) {
        if(toluene<=40) binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.green));
        else if(toluene<=80) binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(toluene<=380) binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(toluene<=800) binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.red));
        else if(toluene<=1600) binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewToluene.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorXylene(double xylene) {
        if(xylene<=10) binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.green));
        else if(xylene<=80) binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(xylene<=300) binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.orange));
        else if(xylene<=800) binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.red));
        else if(xylene<=1600) binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.purple));
        else binding.viewXylene.setBackgroundColor(getResources().getColor(R.color.brown));
    }






}