package com.example.airquality;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.airquality.model.Location;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private ArrayList<Location> mlistLocation;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

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
        holder.tvLocation.setText(locations.getName());
        holder.tvAqi.setText("37");
        holder.tvCondition.setText("Good");
        holder.tvMp.setText("PM2.5");
        holder.tvDetail.setText(locations.getLabel());

//        holder.linearLayoutDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mlistLocation.remove(holder.getAbsoluteAdapterPosition());
//                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mlistLocation.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout linearLayoutDelete;
        private TextView tvLocation;
        private TextView tvAqi;
        private TextView tvCondition;
        private TextView tvMp;
        private TextView tvDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //swipeRevealLayout =itemView.findViewById(R.id.SwipeRevealLayout);
            linearLayoutDelete=itemView.findViewById(R.id.lo_delete);
            tvLocation=itemView.findViewById(R.id.tv_location);
            tvAqi=itemView.findViewById(R.id.tv_aqi);
            tvCondition=itemView.findViewById(R.id.tv_condition);
            tvMp=itemView.findViewById(R.id.tv_mp);
            tvDetail=itemView.findViewById(R.id.tv_detail_location);
        }
    }


}
