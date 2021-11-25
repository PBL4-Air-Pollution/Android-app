package com.example.airquality.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.R;
import com.example.airquality.databinding.ItemHourBinding;
import com.example.airquality.model.HourlyAirQuality;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder>{
    private ArrayList<HourlyAirQuality> hourArrayList;
    private HourClickListener onClickListener;

    public HourAdapter(ArrayList<HourlyAirQuality> hourArrayList, HourClickListener onClickListener) {
        this.hourArrayList = hourArrayList;
        this.onClickListener=onClickListener;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hour, parent, false);
        return  new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyAirQuality hour=hourArrayList.get(position);
        String stringTime[]=hour.getDatetime().split(" ");
        String stringHour[]=stringTime[1].split(":");
        holder.binding.tvHour.setText(stringHour[0]+":00");
        holder.binding.tvAqi.setText(String.format("%.1f", hour.getAqi()));
        if(hour.getRated().equals("Trung bình")) holder.binding.tvRate.setText("T.Bình");
        else holder.binding.tvRate.setText(hour.getRated());
    }

    @Override
    public int getItemCount() {
        return hourArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public ItemHourBinding binding;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHourBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onCLick(view,getBindingAdapterPosition());
        }
    }

    public interface HourClickListener{
        void onCLick(View view,int i);
    }
}
