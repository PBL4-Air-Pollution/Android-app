package com.example.airquality;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.model.HourlyAirQuality;

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
        holder.tvHour.setText(hour.getDatetime().toString());
        holder.tvAQI.setText(Double.toString(hour.getAQI()));
        holder.tvRate.setText(hour.getRate());
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
        public TextView tvHour;
        public  TextView tvAQI;
        public  TextView tvRate;
        public LinearLayout llCard;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour=itemView.findViewById(R.id.tv_hour);
            tvAQI=itemView.findViewById(R.id.tv_aqi);
            tvRate=itemView.findViewById(R.id.tv_rate);
            llCard=itemView.findViewById(R.id.ll_card);

        }

    }
}
