package com.example.airquality.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.R;
import com.example.airquality.databinding.ItemDayDetailBinding;
import com.example.airquality.model.HourlyAirQuality;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DayDetailAdapter extends RecyclerView.Adapter<DayDetailAdapter.ViewHolder>{
    private ArrayList<HourlyAirQuality> hourArrayList;
    public DayDetailAdapter(ArrayList<HourlyAirQuality> hourArrayList) {
        this.hourArrayList = hourArrayList;
    }
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_detail, parent, false);
        return  new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyAirQuality hour=hourArrayList.get(position);
        SimpleDateFormat format=new SimpleDateFormat("HH:mm");
        holder.binding.tvHour.setText(format.format(hour.getDatetime()));
        holder.binding.tvAqi.setText(Double.toString(hour.getAQI()));
        holder.binding.tvRate.setText(hour.getRate());
//        if(Integer.parseInt(hour.getAQI().toString())<=50)
//            holder.llCard.setBackgroundColor(R.color.red);
//        else if(Integer.parseInt(hour.getAQI().toString())>50)
//            holder.llCard.setBackgroundColor(R.color.red);
    }

    @Override
    public int getItemCount() {
        return hourArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ItemDayDetailBinding binding;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDayDetailBinding.bind(itemView);
        }
    }
}
