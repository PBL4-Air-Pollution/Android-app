package com.example.airquality.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private AppDatabase appDatabase;
    private LocationDAO locationDAO;
    private FusedLocationProviderClient myLocation;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        appDatabase = AppDatabase.Instance(getContext());
        locationDAO = appDatabase.locationDAO();

        List<Location> locationList = locationDAO.getAll();

        for (Location location : locationList) {
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
            switch (location.getRated()) {
                case "Tốt": // Xanh lá
                    color = Color.argb(150, 162, 220, 97);
                    break;
                case "Trung bình": // Vàng
                    color = Color.argb(150, 252, 215, 82);
                    break;
                case "Kém": // Cam
                    color = Color.argb(150, 255, 153, 89);
                    break;
                case "Xấu": // Đỏ
                    color = Color.argb(150, 235, 71, 74);
                    break;
                case "Rất xấu": // Tím
                    color = Color.argb(150, 170, 123, 191);
                    break;
                case "Nguy hại": // Nâu
                    color = Color.argb(150, 157, 88, 116);
                    break;
            }

            googleMap.addCircle(new CircleOptions().center(marker)
                    .radius(4000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(0)
                    .fillColor(color));

            markUserCurrentLocation(googleMap);
        }
    }

    @SuppressLint("VisibleForTests")
    private void markUserCurrentLocation(GoogleMap googleMap) {
        myLocation = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 44);
            }
        }
        else {
            //moving the map to user's current location
            googleMap.setMyLocationEnabled(true);
            myLocation.getLastLocation().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    android.location.Location user_location = task.getResult();
                    moveMap(googleMap, user_location.getLatitude(), user_location.getLongitude());
                }
            });
        }
    }

    private void moveMap(GoogleMap googleMap, double latitude, double longitude) {
//        16.074338, 108.205507
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("You are here!"));
        Log.d("debug", "moveMap: move camera to " + latitude + ", " + longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}