package com.example.airquality.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentLocationDetailBinding;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.LocationDAO;

import java.util.ArrayList;

public class LocationDetailFragment extends Fragment {
    private FragmentLocationDetailBinding binding;
    private LocationDAO locationDAO;
    private AppDatabase appDatabase;
    private ArrayList<Location> listLocation;
    private ArrayList<String> listLocationName;
    private String locationName;
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
        listLocationName = new ArrayList<String>();
        appDatabase=AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO=appDatabase.locationDAO();
        listLocationName.addAll(locationDAO.getListNameHasNotMark());
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_items_category,listLocationName);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.spnLocation.setAdapter(adapter);
        binding.spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                locationName=listLocationName.get(position);
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
                        listLocation=new ArrayList<Location>();
                        listLocation.addAll(locationDAO.getListByNameNoMark(locationName));
                        Location location=listLocation.get(0);
                        if(location!=null){
                            location.setMarked(true);
                            location.setLabel(binding.tvLabel.getText().toString());
                            locationDAO.updateLocations(location);
                        }

                   }
                });
                LocationFragment hourDetailFragment=new LocationFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_home,hourDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }

        });

//        binding.spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

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

