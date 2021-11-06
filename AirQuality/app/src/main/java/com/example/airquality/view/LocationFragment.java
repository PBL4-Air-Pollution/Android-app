package com.example.airquality.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.LocationAdapter;
import com.example.airquality.R;
import com.example.airquality.model.Location;

import java.util.ArrayList;

public class LocationFragment extends Fragment {
    private RecyclerView rcvLocation;
    private  ArrayList<Location> listLocation;
    private LocationAdapter locationAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvLocation=view.findViewById(R.id.rcv_location);
        rcvLocation.setLayoutManager( new LinearLayoutManager(getContext()));
        listLocation=new ArrayList<Location>();
        listLocation=getListLocation();
        locationAdapter = new LocationAdapter(listLocation);
        locationAdapter.notifyDataSetChanged();
        rcvLocation.setAdapter(locationAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_location, container, false);



        return view;
    }
    private ArrayList<Location> getListLocation() {
        ArrayList<Location> list = new ArrayList<Location>();
        list.add(new Location("Hoa Khanh Bac", "Home A", true));
        list.add(new Location( "Hoa Khanh Nam", "Home B", true));
        list.add(new Location("Hoa Khanh Bac", "Home C", true));
        list.add(new Location( "Hoa Khanh Nam", "Home D", true));
        return list;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.location_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:  {

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}