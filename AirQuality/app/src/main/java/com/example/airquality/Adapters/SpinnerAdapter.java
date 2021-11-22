package com.example.airquality.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.airquality.R;
import com.example.airquality.model.Location;
import com.example.airquality.view.LocationFragment;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Location> {
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Location> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_items_selected,parent,false);
        TextView tvSelected= convertView.findViewById(R.id.tv_selected);
        Location location =this.getItem(position);
        if(location!=null){
            tvSelected.setText(location.getName());
        }

        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_items_category,parent,false);
        TextView tvCategory= convertView.findViewById(R.id.tv_category);
        Location location =this.getItem(position);
        if(location!=null){
            tvCategory.setText(location.getName());
        }

        return convertView;
    }
}
