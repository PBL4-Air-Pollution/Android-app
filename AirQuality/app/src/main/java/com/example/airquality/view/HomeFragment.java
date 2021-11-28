package com.example.airquality.view;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private SpinnerAdapter spinnerAdapter;
    private Location currentLocation;
    private LocationDAO locationDAO;
    private ArrayList<Location> locationArrayList;

    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private ArrayList<HourlyAirQuality> hourArrayList;
    private HourlyAirQuality currentHourlyData;

    private ArrayList<DailyAirQuality> dayArrayList;

    private ArrayList<Location> locationArrayList;

    private AppDatabase appDatabase;
    private FragmentHomeBinding binding;

    private String stringDay = "";
    private String stringDayHour = "";

    @Override
    public void onCreate(Bundle bundle) {
        setHasOptionsMenu(true);
        super.onCreate(bundle);
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
        stringDayHour = new SimpleDateFormat("dd/MM/yyyy HH:00:00").format(new Date());

        // Hourly air quality list (Horizontal)
        binding.tbHome.inflateMenu(R.menu.menu_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvHours.setLayoutManager(layoutManager);
        binding.rvDays.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up app database
        appDatabase = AppDatabase.Instance(requireContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO = appDatabase.hourlyAirQualityDAO();

        // Get UI data
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayArrayList = new ArrayList<DailyAirQuality>();
        locationArrayList = new ArrayList<Location>();

        if (locationDAO.getListHasMark().size() == 0){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Get first station to show if no station marked
                    currentLocation = locationDAO.getAll().get(0);

                    // Lấy data của giờ hiện tại để hiển thị thông tin cuối trang
                    List<HourlyAirQuality> currentDateData = hourlyAirQualityDAO.getListByLocationIDAndDate(currentLocation.getId(), stringDay);
                    currentHourlyData = currentDateData.get(currentDateData.size() - 1);

                    loadHome();
                    loadHours();
                    loadDays();
                }
            });
        }
        else {
            // Get first marked station to load to UI
            currentLocation = locationDAO.getListHasMark().get(0);

            List<HourlyAirQuality> currentDateData = hourlyAirQualityDAO.getListByLocationIDAndDate(currentLocation.getId(), stringDay);
            currentHourlyData = currentDateData.get(currentDateData.size() - 1);

            loadHome();
            loadHours();
            loadDays();

            // Setup spinner data
            locationArrayList.addAll(locationDAO.getListHasMark());
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireContext(), R.layout.spinner_items_category, locationArrayList);
            binding.snLocation.setAdapter(spinnerAdapter);
            binding.snLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    currentLocation = locationArrayList.get(i);
                    loadHome();
                    loadHours();
                    loadDays();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }
    @SuppressLint("DefaultLocale")
    private void loadHome() {
        // Card view top
        binding.tvLocation.setText(currentLocation.getStationName());
        binding.tvAqi.setText(String.format("%.0f", currentLocation.getAqi()));
        binding.tvRate.setText(currentLocation.getRated());

        setBackgroundColor(currentLocation.getRated());

        // Set up air quality info bottom
        binding.tvPM25.setText(String.format("%.1f", currentHourlyData.getPm25()));
        setViewColorPM25(currentHourlyData.getPm25());
        binding.tvPM10.setText(String.format("%.1f", currentHourlyData.getPm10()));
        setViewColorPM10(currentHourlyData.getPm10());
        binding.tvO3.setText(String.format("%.1f", currentHourlyData.getO3()));
        setViewColorO3(currentHourlyData.getO3());
        binding.tvNO2.setText(String.format("%.1f", currentHourlyData.getNo2()));
        setViewColorNO2(currentHourlyData.getNo2());
        binding.tvSO2.setText(String.format("%.1f", currentHourlyData.getSo2()));
        setViewColorSO2(currentHourlyData.getSo2());
        binding.tvCO.setText(String.format("%.1f", currentHourlyData.getCo()));
        setViewColorCO(currentHourlyData.getCo());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadHours() {
        // On click listener go to hour detail fragment
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
        HourAdapter hoursAdapter = new HourAdapter(hourArrayList, hourClickListener);
        hourArrayList.addAll(hourlyAirQualityDAO.getListByLocationIDAndDate(currentLocation.getId(), stringDay));
        Collections.reverse(hourArrayList);
        hoursAdapter.notifyDataSetChanged();
        binding.rvHours.setAdapter(hoursAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDays() {
        // On click listener go to day detail fragment
        DayAdapter.DayClickListener dayClickListener = new DayAdapter.DayClickListener() {
            @Override
            public void onCLick(View view, int i) {
                Bundle bundle = new Bundle();
                DailyAirQuality day = dayArrayList.get(i);
                bundle.putString("Date-LocationID", day.getDate() + "," + day.getLocationID());
                DayDetailFragment dayDetailFragment = new DayDetailFragment();
                dayDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_home, dayDetailFragment).addToBackStack(null).commit();
            }
        };

        // Get daily data of current station
        DayAdapter daysAdapter = new DayAdapter(dayArrayList, dayClickListener);
        DailyAirQualityDAO dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
        dayArrayList.addAll(dailyAirQualityDAO.getListByLocationID(currentLocation.getId()));
        daysAdapter.notifyDataSetChanged();
        binding.rvDays.setAdapter(daysAdapter);
    }

    private void setBackgroundColor(String rate) {
        switch (rate) {
        case "Tốt": // Xanh lá
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_green);
            binding.llAvatar.setBackgroundResource(R.color.green);
            binding.imAvatar.setImageResource(R.drawable.avatar_green);
            binding.llText.setBackgroundResource(R.color.light_green);
            binding.tvRecommended.setText("Không ảnh hướng tới sức khỏe");
            break;
        case "Trung bình": // Vàng
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_yellow);
            binding.llAvatar.setBackgroundResource(R.color.yellow);
            binding.imAvatar.setImageResource(R.drawable.avatar_yellow);
            binding.llText.setBackgroundResource(R.drawable.custom_yellow);
            binding.tvRecommended.setText("Nhóm nhạy cảm nên hạn chế thời gian ở bên ngoài");
            break;
        case "Kém": // Cam
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_orange);
            binding.llAvatar.setBackgroundResource(R.color.orange);
            binding.imAvatar.setImageResource(R.drawable.avatar_orange);
            binding.llText.setBackgroundResource(R.drawable.custom_orange);
            binding.tvRecommended.setText("Nhóm nhạy cảm hạn chế thời gian ở bên ngoài");
            break;
        case "Xấu": // Đỏ
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_red);
            binding.llAvatar.setBackgroundResource(R.color.red);
            binding.imAvatar.setImageResource(R.drawable.avatar_red);
            binding.llText.setBackgroundResource(R.drawable.custom_red);
            binding.tvRecommended.setText("Nhóm nhạy cảm tránh ra ngoài. Những người khác hạn chế ra ngoài");
            break;
        case "Rất xấu": // Tím
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_purple);
            binding.llAvatar.setBackgroundResource(R.color.purple);
            binding.imAvatar.setImageResource(R.drawable.avatar_purple);
            binding.llText.setBackgroundResource(R.drawable.custom_purple);
            binding.tvRecommended.setText("Nhóm nhạy cảm tránh ra ngoài. Những người khác hạn chế ra ngoài");
            break;
        case "Nguy hại": // Nâu
            binding.fmHome.setBackgroundResource(R.drawable.custom_background_brown);
            binding.llAvatar.setBackgroundResource(R.color.brown);
            binding.imAvatar.setImageResource(R.drawable.avatar_brown);
            binding.llText.setBackgroundResource(R.drawable.custom_brown);
            binding.tvRecommended.setText("Mọi người nên ở nhà");
            break;
        }
    }

    private void setViewColorPM25(double pm25) {
        if (pm25 <= 30)
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.green));
        else if (pm25 <= 60)
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (pm25 <= 90)
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (pm25 <= 120)
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.red));
        else if (pm25 <= 250)
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewPM25.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorPM10(double pm10) {
        if (pm10 <= 50)
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.green));
        else if (pm10 <= 100)
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (pm10 <= 250)
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (pm10 <= 350)
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.red));
        else if (pm10 <= 430)
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewPM10.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorO3(double o3) {
        if (o3 <= 50)
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.green));
        else if (o3 <= 100)
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (o3 <= 168)
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (o3 <= 208)
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.red));
        else if (o3 <= 748)
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewO3.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorNO2(double no2) {
        if (no2 <= 40)
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.green));
        else if (no2 <= 80)
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (no2 <= 180)
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (no2 <= 280)
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.red));
        else if (no2 <= 400)
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewNO2.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorSO2(double so2) {
        if (so2 <= 40)
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.green));
        else if (so2 <= 80)
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (so2 <= 380)
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (so2 <= 800)
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.red));
        else if (so2 <= 1600)
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewSO2.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    private void setViewColorCO(double co) {
        if (co <= 1)
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.green));
        else if (co <= 2)
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if (co <= 10)
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (co <= 17)
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.red));
        else if (co <= 34)
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.purple));
        else
            binding.viewCO.setBackgroundColor(getResources().getColor(R.color.brown));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}