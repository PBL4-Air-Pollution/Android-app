package com.example.airquality.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airquality.R;
import com.example.airquality.databinding.ItemDayBinding;
import com.example.airquality.model.DailyAirQuality;

import java.text.SimpleDateFormat;
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
        DailyAirQuality day = dayArrayList.get(position);
        holder.binding.tvDate.setText(day.getDate());
        holder.binding.tvAqi.setText((int)day.getAqi()+"");
        holder.binding.tvRate.setText(day.getRated());
        switch (day.getRated()){
            case "Tốt": // Xanh lá
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_green);
                break;
            case "Trung bình": // Vàng
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_yellow);
                break;
            case "Kém": // Cam
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_orange);
                break;
            case "Xấu": // Đỏ
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_red);
                break;
            case "Rất xấu": // Tím
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_purple);
                break;
            case "Nguy hại": // Nâu
                holder.binding.imCircle.setImageResource(R.drawable.ic_baseline_circle_brown);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dayArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ItemDayBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDayBinding.bind(itemView);
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
