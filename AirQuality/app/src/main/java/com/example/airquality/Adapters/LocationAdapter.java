package com.example.airquality.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.airquality.R;
import com.example.airquality.databinding.CardviewItemsBinding;
import com.example.airquality.model.Location;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private ArrayList<Location> mlistLocation;

    public LocationAdapter(ArrayList<Location> mlistLocation) {
        this.mlistLocation = mlistLocation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location locations = mlistLocation.get(position);
        if(locations==null){
            return;
        }
        //viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(locations.getId()));
        holder.binding.tvLocation.setText(locations.getName());
        holder.binding.tvAqi.setText("37");
        holder.binding.tvCondition.setText("Good");
        holder.binding.tvMp.setText("PM2.5");
        holder.binding.tvDetailLocation.setText(locations.getLabel());
    }

    @Override
    public int getItemCount() {
        return mlistLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardviewItemsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardviewItemsBinding.bind(itemView);
        }
    }
}
