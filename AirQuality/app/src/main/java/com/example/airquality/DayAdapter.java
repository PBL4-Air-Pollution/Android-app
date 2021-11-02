package com.example.airquality;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.model.DailyAirQuality;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{
    private ArrayList<DailyAirQuality> dayArrayList;
    private DayClickListener listener;
    public DayAdapter(ArrayList<DailyAirQuality> dayArrayList, DayClickListener listener) {

        this.dayArrayList = dayArrayList;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return  new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyAirQuality day=dayArrayList.get(position);
        holder.tvDatetime.setText(day.getDatetime().toString());
        holder.tvAQI.setText(Double.toString(day.getAQI()));
        holder.tvRate.setText(day.getRate());
    }

    @Override
    public int getItemCount() {
        return dayArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvDatetime;
        public  TextView tvAQI;
        public  TextView tvRate;
        public LinearLayout llCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDatetime=itemView.findViewById(R.id.tv_date);
            tvAQI=itemView.findViewById(R.id.tv_aqi);
            tvRate=itemView.findViewById(R.id.tv_rate);
            llCard=itemView.findViewById(R.id.ll_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onCLick(view,getAdapterPosition() );
        }
    }
    public interface DayClickListener{
        void onCLick(View view,int i);

    }

}
