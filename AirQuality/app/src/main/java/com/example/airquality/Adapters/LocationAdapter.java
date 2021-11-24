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
        hourList.addAll(hourlyAirQualityDAO.getListByLocationID(location.getId()));
        Date date,today;
        for(HourlyAirQuality i: hourlyAirQualityDAO.getListByLocationID(location.getId())){
            try {
                date=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dayHourFormat.format(i.getDatetime()));
                today= new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(stringToday);
                if(date.compareTo(today)==0) {
                    holder.binding.tvLocation.setText(location.getStationName());
                    holder.binding.tvAqi.setText(String.valueOf(i.getAqi()));
                    holder.binding.tvRate.setText(i.getRated());
                    holder.binding.tvLable.setText(location.getLabel());
                    if(i.getAqi()<=50){
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.green);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_green);
                    }
                    else if(i.getAqi()<=100) {
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.yellow);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_yellow);
                    }
                    else if(i.getAqi()<=150){
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.orange);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_orange);
                    }
                    else if(i.getAqi()<=200){
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.red);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_red);
                    }
                    else if(i.getAqi()<=300) {
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.purple);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_purple);
                    }
                    else if(i.getAqi()<=500) {
                        holder.binding.cvLocationItem.setBackgroundResource(R.color.brown);
                        holder.binding.ivAvatar.setImageResource(R.drawable.avatar_brown);
                    }
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
