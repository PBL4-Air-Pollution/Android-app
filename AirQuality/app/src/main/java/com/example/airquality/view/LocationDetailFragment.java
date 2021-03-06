package com.example.airquality.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.airquality.Adapters.SpinnerAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.Notifications;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentLocationDetailBinding;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.util.ArrayList;

public class LocationDetailFragment extends Fragment {
    private FragmentLocationDetailBinding binding;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private LocationDAO locationDAO;
    private AppDatabase appDatabase;
    private ArrayList<Location> locationArrayList;
    private ArrayList<String> listLocationName;
    private Location location;

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
        binding= FragmentLocationDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tbLocationDetail.inflateMenu(R.menu.menu_back);
        binding.tbLocationDetail.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_back){
                    LocationFragment locationFragment=new LocationFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_home,locationFragment)
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            }
        });

        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        hourlyAirQualityDAO = appDatabase.hourlyAirQualityDAO();

        locationArrayList = new ArrayList<Location>();
        locationArrayList.addAll(locationDAO.getListHasNotMark());
        location = locationArrayList.get(0);

        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(getContext(),R.layout.spinner_items_category,locationArrayList);
        binding.spnLocation.setAdapter(spinnerAdapter);
        binding.spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = locationArrayList.get(i);
                binding.tvLocationName.setText(location.getStationName());
                binding.tvAqiDetail.setText((int)location.getAqi()+"");
                binding.tvDescribe.setText(String.format( location.getDescribe()));
                binding.tvRateDetail.setText(String.format( location.getRated()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(location!=null){
                            location.setMarked(true);
                            location.setLabel(binding.tvLabel.getText().toString());
                            locationDAO.updateLocations(location);
                        }
                        
                        // Refresh notification
                        Notifications notifications = new Notifications(getContext());
                        notifications.setUpNotification();
                   }
                });

                LocationFragment locationFragment=new LocationFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_home,locationFragment)
                        .addToBackStack(null)
                        .commit();
            }

        });
        Log.d("tag",locationDAO.getListHasMark().size()+"");


    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_back, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_back){
            LocationFragment locationFragment=new LocationFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_home,locationFragment)
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}

