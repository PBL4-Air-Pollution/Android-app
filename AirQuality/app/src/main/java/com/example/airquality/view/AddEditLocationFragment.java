package com.example.airquality.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentAddEditLocationBinding;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.LocationDAO;

public class AddEditLocationFragment extends Fragment {
    private FragmentAddEditLocationBinding binding;

    private AppDatabase appDatabase;
    private LocationDAO locationDAO;
    private Location location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_back, menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentAddEditLocationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tbLocationEdit.inflateMenu(R.menu.menu_back);
        binding.tbLocationEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        Bundle bundle = this.getArguments();
        String locationID = (bundle.getString("LocationID"));
        appDatabase = AppDatabase.Instance(getContext().getApplicationContext());
        locationDAO = appDatabase.locationDAO();
        location=locationDAO.getByID(Integer.parseInt(locationID));

        binding.tvLabel.setText(location.getLabel());
        binding.tvLocationName.setText(location.getStationName());
        binding.tvAddDescribe.setText(location.getDescribe());
        binding.tvAddRate.setText(location.getRated());
        binding.tvAddAqi.setText(String.format("%.1f",location.getAqi()));
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(location!=null){
                            location.setLabel(binding.tvLabel.getText().toString());
                            locationDAO.updateLocations(location);
                        }

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