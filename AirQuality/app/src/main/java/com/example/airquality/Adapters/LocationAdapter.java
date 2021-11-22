package com.example.airquality.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.ItemCardviewBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.view.AddEditLocationFragment;
import com.example.airquality.view.LocationFragment;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private ArrayList<Location> mlistLocation;
    private Context context;
    private DateFormat dayHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private String stringToday="16/11/2021 03:00";
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
   @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location =mlistLocation.get(position);
        if(location==null){
            return;
        }
        //viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(locations.getId()));
        AppDatabase appDatabase=AppDatabase.Instance(context);
        HourlyAirQualityDAO hourlyAirQualityDAO=appDatabase.hourlyAirQualityDAO();
        ArrayList<HourlyAirQuality> hourList = new ArrayList<HourlyAirQuality>();
        hourList.addAll(hourlyAirQualityDAO.getListByLocation(location.getStationName()));
        Date date,today;
        for(HourlyAirQuality i: hourlyAirQualityDAO.getListByLocation(location.getStationName())){
            try {
                date=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dayHourFormat.format(i.getDatetime()));
                today= new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(stringToday);
                if(date.compareTo(today)==0) {
                    holder.binding.tvLocation.setText(location.getStationName());
                    holder.binding.tvAqi.setText(String.valueOf(i.getAQI()));
                    holder.binding.tvRate.setText(i.getRate());
                    holder.binding.tvLable.setText(location.getLabel());
                    if(i.getAQI()<=50) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_yellow);
                    else if(i.getAQI()<=100) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_yellow);
                    else if(i.getAQI()<=150) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_orange);
                    else if(i.getAQI()<=200) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_red);
                    else if(i.getAQI()<=300) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_purple);
                    else if(i.getAQI()<=500) holder.binding.cvLocationItem.setBackgroundResource(R.color.light_brown);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        int _position=position;
        holder.binding.locationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase appDatabase=AppDatabase.Instance(context);
                LocationDAO locationDAO=appDatabase.locationDAO();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Location location=mlistLocation.get(_position);
                        location.setMarked(false);
                        location.setLabel("");
                        locationDAO.updateLocations(location);
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
            public void onClick(View v) {
                AddEditLocationFragment addEditLocationFragment = new AddEditLocationFragment();
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fl_home, addEditLocationFragment)
//                        .addToBackStack(null)
//                        .commit();
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
