package com.example.airquality.view;

import android.os.Bundle;
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

import com.example.airquality.Adapters.LocationAdapter;
import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentLocationBinding;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.LocationDAO;

import java.util.ArrayList;

public class LocationFragment extends Fragment {
    private FragmentLocationBinding binding;

    private RecyclerView rcvLocation;
    private ArrayList<Location> locationArrayList;
    private LocationAdapter locationAdapter;
    private AppDatabase appDatabase;
    private LocationDAO locationDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentLocationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.rcvLocation.setLayoutManager(new LinearLayoutManager(getContext()));
        locationArrayList = new ArrayList<Location>();
        locationAdapter = new LocationAdapter(locationArrayList);
        appDatabase=AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO=appDatabase.locationDAO();
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