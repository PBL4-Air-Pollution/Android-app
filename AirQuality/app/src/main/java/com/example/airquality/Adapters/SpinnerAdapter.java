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
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Location> locations ) {
        super(context, resource, locations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_items_category,parent,false);
//        Location location=getItem(position);
//        TextView textView=(TextView) convertView.findViewById(R.id.spn_location);
//        textView.setText(location.getStationName());
        return view;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_items_category,parent,false);
        Location location=getItem(position);
        TextView textView=(TextView) convertView.findViewById(R.id.spn_location);
        textView.setText(location.getStationName());
        return convertView;
    }
}
