package com.example.airquality.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.AppDatabase;
import com.example.airquality.Notifications;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentAddEditLocationBinding;
import com.example.airquality.databinding.ItemCardviewBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.view.AddEditLocationFragment;
import com.example.airquality.view.LocationFragment;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private ArrayList<Location> mlistLocation;
    private Context context;

    public LocationAdapter(ArrayList<Location> mlistLocation) {
        this.mlistLocation = mlistLocation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);
        context=parent.getContext();
        return  new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location =mlistLocation.get(position);
        if(location==null){
            return;
        }
        AppDatabase appDatabase=AppDatabase.Instance(context);
        HourlyAirQualityDAO hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();
        LocationDAO locationDAO = appDatabase.locationDAO();

        ArrayList<HourlyAirQuality> hourList = new ArrayList<HourlyAirQuality>();
        hourList.addAll(hourlyAirQualityDAO.getListByLocationID(location.getId()));

        holder.binding.tvLocation.setText(location.getStationName());
        holder.binding.tvAqi.setText((int)location.getAqi() + "");
        if (location.getRated().equals("Trung bình")) holder.binding.tvRate.setText("T.Bình");
        else holder.binding.tvRate.setText(location.getRated());
        holder.binding.tvLable.setText(location.getLabel());
        if (location.isFavourite()) holder.binding.btnFavourite.setColorFilter(R.color.dark_green);

        if(location.getAqi()<=50){
           holder.binding.llAvatar.setBackgroundResource(R.color.green);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_green);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_green);
        }
        else if(location.getAqi()<=100) {
           holder.binding.llAvatar.setBackgroundResource(R.color.yellow);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_yellow);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_yellow);
        }
        else if(location.getAqi()<=150){
           holder.binding.llAvatar.setBackgroundResource(R.color.orange);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_orange);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_orange);
        }
        else if(location.getAqi()<=200){
           holder.binding.llAvatar.setBackgroundResource(R.color.red);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_red);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_red);
        }
        else if(location.getAqi()<=300) {
           holder.binding.llAvatar.setBackgroundResource(R.color.purple);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_purple);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_purple);
        }
        else if(location.getAqi()<=500) {
           holder.binding.llAvatar.setBackgroundResource(R.color.brown);
           holder.binding.llText.setBackgroundResource(R.drawable.custom_brown);
           holder.binding.ivAvatar.setImageResource(R.drawable.avatar_brown);
        }

        holder.binding.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDAO.clearFavourite();
                if (!location.isFavourite()) locationDAO.setFavourite(location.getId());

                // Refresh notification
                Notifications notifications = new Notifications(context);
                notifications.setUpNotification();

                // Refresh location fragment
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                LocationFragment locationFragment = new LocationFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, locationFragment).addToBackStack(null).commit();
            }
        });

        holder.binding.locationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase appDatabase=AppDatabase.Instance(context);
                LocationDAO locationDAO=appDatabase.locationDAO();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        location.setMarked(false);
                        location.setLabel("");
                        locationDAO.updateLocations(location);

                        // Refresh notification
                        Notifications notifications = new Notifications(context);
                        notifications.setUpNotification();

                        LocationFragment hourDetailFragment=new LocationFragment();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_home,hourDetailFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });
        holder.binding.locationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("LocationID",String.valueOf(location.getId()));
                AddEditLocationFragment addEditLocationFragment=new AddEditLocationFragment();
                addEditLocationFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_home,addEditLocationFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mlistLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCardviewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCardviewBinding.bind(itemView);
        }
    }
}
