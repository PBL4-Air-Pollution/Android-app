package com.example.airquality.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.airquality.Adapters.LocationAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentLocationBinding;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocationFragment extends Fragment {
    private FragmentLocationBinding binding;
    private HourlyAirQualityDAO hourlyAirQualityDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLocationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint({"SimpleDateFormat", "NotifyDataSetChanged"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.tbLocation.inflateMenu(R.menu.menu_location);
        String stringDayHour = new SimpleDateFormat("dd/MM/yyyy hh:00:00").format(new Date());
        binding.tbLocation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_add) {
                    LocationDetailFragment locationDetailFragment = new LocationDetailFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_home, locationDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            }
        });
        binding.rcvLocation.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Location> locationArrayList = new ArrayList<Location>();
        LocationAdapter locationAdapter = new LocationAdapter(locationArrayList);
        AppDatabase appDatabase = AppDatabase.Instance(requireContext().getApplicationContext());
        LocationDAO locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO = appDatabase.hourlyAirQualityDAO();

        locationArrayList.addAll(locationDAO.getListHasMark());
        locationAdapter.notifyDataSetChanged();
        binding.rcvLocation.setAdapter(locationAdapter);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_location, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            LocationDetailFragment locationDetailFragment = new LocationDetailFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_home, locationDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}