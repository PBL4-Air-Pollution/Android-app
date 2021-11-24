package com.example.airquality.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    private AppDatabase appDatabase;
    private LocationDAO locationDAO;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            /*LatLng DaNang = new LatLng(16.047079, 108.206230); //vi do va kinh do
            googleMap.addMarker(new MarkerOptions().position(DaNang).title("DaNang"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(DaNang));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));*/
            appDatabase = AppDatabase.Instance(getContext());
            locationDAO = appDatabase.locationDAO();
            List<Location> locationList =  locationDAO.getAll();

            for (Location location : locationList)
            {
                LatLng marker = new LatLng(location.getViDo(), location.getKinhDo());

                String markerTitle = "Trạm: " + location.getStationName();
                String stationInfo =
                        "AQI: " + location.getAqi() + System.lineSeparator() +
                        "Đánh giá: " + location.getRated() + System.lineSeparator() +
                        "Nhãn: " + location.getLabel();

                googleMap.addMarker(new MarkerOptions().position(marker).title(markerTitle).snippet(stationInfo));
                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {

                        return null;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        return null;
                    }
                });

                int color = 0;
                switch (location.getRated()){
                    case "Tốt": // Xanh lá
                        color = Color.argb(150,162,220,97);
                        break;
                    case "Trung bình": // Vàng
                        color = Color.argb(150,252,215,82);
                        break;
                    case "Kém": // Cam
                        color = Color.argb(150,255,153,89);
                        break;
                    case "Xấu": // Đỏ
                        color = Color.argb(150,235,71,74);
                        break;
                    case "Rất xấu": // Tím
                        color = Color.argb(150,170,123,191);
                        break;
                    case "Nguy hại": // Nâu
                        color = Color.argb(150,157 ,88,116);
                        break;
                }

                googleMap.addCircle(new CircleOptions().center(marker)
                        .radius(4000)
                        .strokeColor(Color.TRANSPARENT)
                        .strokeWidth(0)
                        .fillColor(color));
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(16.05700804619488, 108.20257470722422)));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        }
    };

    public MapsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
}