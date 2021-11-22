package com.example.airquality.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.Adapters.DayDetailAdapter;
import com.example.airquality.Adapters.LocationAdapter;
import com.example.airquality.Adapters.SpinnerAdapter;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentDayDetailBinding;
import com.example.airquality.databinding.FragmentLocationBinding;
import com.example.airquality.databinding.FragmentLocationDetailBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class LocationDetailFragment extends Fragment {
    private ArrayList<Location> listLocation;
    private SpinnerAdapter spinnerAdapter;
    private FragmentLocationDetailBinding binding;
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
        binding= FragmentLocationDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listLocation = new ArrayList<Location>();
        listLocation = getListLocation();
        spinnerAdapter = new SpinnerAdapter(getActivity(),R.layout.spinner_items_selected,getListLocation());
        binding.spnLocation.setAdapter(spinnerAdapter);
        binding.spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),spinnerAdapter.getItem(position).getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private ArrayList<Location> getListLocation() {
        ArrayList<Location> list = new ArrayList<Location>();
        list.add(new Location("Hoa Khanh Bac", "Home A", true));
        list.add(new Location("Hoa Khanh Nam", "Home B", true));
        list.add(new Location("Hoa Khanh Bac", "Home C", true));
        list.add(new Location("Hoa Khanh Nam", "Home D", true));
        return list;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_location_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_back_location){
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

