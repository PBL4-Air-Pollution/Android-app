package com.example.airquality.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.airquality.AppDatabase;
import com.example.airquality.R;
import com.example.airquality.databinding.FragmentMapsBinding;
import com.example.airquality.model.Location;
import com.example.airquality.modules.DirectionFinder;
import com.example.airquality.modules.DirectionFinderListener;
import com.example.airquality.modules.Route;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MapsFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener, DirectionFinderListener {

    private GoogleMap myGoogleMap;
    private FusedLocationProviderClient client;

    private Marker userLocationMarker;
    private Marker currentLocationMarker;

    private FragmentMapsBinding binding;

    private List<Polyline> polylinePaths;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        setUpActionListener();
    }

    private void setUpActionListener(){
        // Set search view
        binding.svMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String txtSearch = binding.svMap.getQuery().toString();
                List<Address> addressList = null;

                try {
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

                    addressList = geocoder.getFromLocationName(txtSearch, 1);

                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);

                        if (currentLocationMarker != null) {
                            currentLocationMarker.remove();
                        }

                        currentLocationMarker = myGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(address.getLatitude(), address.getLongitude()))
                                .title(address.getAddressLine(0)));

                        // Clear polyline and distance, duration text view
                        if (polylinePaths != null) {
                            for (Polyline polyline : polylinePaths) {
                                polyline.remove();
                            }
                            binding.tvDistance.setText("");
                            binding.tvDuration.setText("");
                        }

                        moveCamera(address.getLatitude(), address.getLongitude());
                    } else {
                        Toast.makeText(requireContext(), "Location not found!", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // My location button
        binding.btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get last location with Google API
//            getUserCurrentLocation();

                // Set my own location
                markUserCurrentLocation(16.074380, 108.205576);
                moveCamera(16.074380, 108.205576);
            }
        });

        // Find direction button
        binding.btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocationMarker != null) {
                    sendRequest();
                }
                else {
                    Toast.makeText(requireContext(), "Please enter the destination name!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendRequest(){
        String startLocation = userLocationMarker.getPosition().latitude + "," + userLocationMarker.getPosition().longitude;
        String destination = currentLocationMarker.getPosition().latitude + "," + currentLocationMarker.getPosition().longitude;

        try {
            new DirectionFinder(this, startLocation, destination).execute();
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myGoogleMap = googleMap;
        AppDatabase appDatabase = AppDatabase.Instance(getContext());
        LocationDAO locationDAO = appDatabase.locationDAO();

        List<Location> locationList = locationDAO.getAll();

        for (Location location : locationList) {
            addStationMarker(location);
        }

        // Get current user location
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get last location with Google API
//            myGoogleMap.setMyLocationEnabled(true);
//            getUserCurrentLocation();

            // Set my own location
            markUserCurrentLocation(16.074380, 108.205576);
            moveCamera(16.074380, 108.205576);
        }
        else{
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }

    private void addStationMarker(Location location){
        // Draw marker
        LatLng marker = new LatLng(location.getViDo(), location.getKinhDo());

        String markerTitle = "Trạm: " + location.getStationName();
        String stationInfo = "AQI: " + location.getAqi() + "\n" +
                            "Đánh giá: " + location.getRated() + "\n" +
                            "Nhãn: " + location.getLabel();

        int markerColor = R.color.green;
        switch (location.getRated()) {
            case "Tốt": // Xanh lá
                markerColor = R.color.green;
                break;
            case "Trung bình": // Vàng
                markerColor = R.color.yellow;
                break;
            case "Kém": // Cam
                markerColor = R.color.orange;
                break;
            case "Xấu": // Đỏ
                markerColor = R.color.red;
                break;
            case "Rất xấu": // Tím
                markerColor = R.color.purple;
                break;
            case "Nguy hại": // Nâu
                markerColor = R.color.brown;
                break;
        }

        myGoogleMap.addMarker(new MarkerOptions()
                .position(marker)
                .title(markerTitle)
                .snippet(stationInfo)
                .icon(createStationLocationPin(requireContext(),
                        ContextCompat.getColor(requireContext(), markerColor),
                        location.getAqi())));

        // Draw zone
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

        myGoogleMap.addCircle(new CircleOptions().center(marker)
                .radius(4000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(0)
                .fillColor(color));
    }

    @SuppressLint("VisibleForTests")
    private void getUserCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<android.location.Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null){
                    markUserCurrentLocation(location.getLatitude(), location.getLongitude());
                    moveCamera(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    private void markUserCurrentLocation(double latitude, double longitude){
        LatLng latLng = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("You are here!")
                .draggable(true)
                .icon(createUserLocationPin(requireContext(), ContextCompat.getColor(requireContext(), R.color.red)));

        if (userLocationMarker != null){
            userLocationMarker.remove();
        }

        userLocationMarker = myGoogleMap.addMarker(markerOptions);
    }

    private static BitmapDescriptor createUserLocationPin(@NonNull Context context, @ColorInt int tintColor){
        // Get drawables
        Drawable vectorDrawableBackground = ResourcesCompat.getDrawable(
                context.getResources(), R.drawable.ic_baseline_circle_24, null);
        Drawable vectorDrawableForeground = ResourcesCompat.getDrawable(
                context.getResources(), R.drawable.ic_baseline_person_pin_24, null);

        if (vectorDrawableBackground == null || vectorDrawableForeground == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        // Create bitmap icon
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        // Create drawing canvas
        Canvas canvas = new Canvas(bitmap);

        // Formatting drawable vectors
        vectorDrawableBackground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight() - 10);
        vectorDrawableForeground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw vectors on canvas
        DrawableCompat.setTint(vectorDrawableBackground, ContextCompat.getColor(context, R.color.white));
        DrawableCompat.setTint(vectorDrawableForeground, tintColor);
        vectorDrawableBackground.draw(canvas);
        vectorDrawableForeground.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void moveCamera(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        markUserCurrentLocation(location.getLatitude(), location.getLongitude());
        moveCamera(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Get last location with Google API
//            getUserCurrentLocation();

                // Set my own location
                markUserCurrentLocation(16.074380, 108.205576);
                moveCamera(16.074380, 108.205576);
            }
        }
    }

    public static BitmapDescriptor createStationLocationPin(@NonNull Context context,
                                                       @ColorInt int tintColor,
                                                       double aqi) {
        // Get drawables
        Drawable vectorDrawableBackground = ResourcesCompat.getDrawable(
                context.getResources(), R.drawable.ic_baseline_person_pin_24, null);
        Drawable vectorDrawableForeground = ResourcesCompat.getDrawable(
                context.getResources(), R.drawable.ic_baseline_circle_24, null);

        if (vectorDrawableBackground == null || vectorDrawableForeground == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        // Create bitmap icon
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        // Create drawing canvas
        Canvas canvas = new Canvas(bitmap);

        // Formatting drawable vectors
        vectorDrawableBackground.setBounds(0, 10, canvas.getWidth(), canvas.getHeight());
        vectorDrawableForeground.setBounds(15, 20, canvas.getWidth() - 15, canvas.getHeight() - 20);

        // Draw vectors on canvas
        DrawableCompat.setTint(vectorDrawableBackground, ContextCompat.getColor(context, R.color.white));
        DrawableCompat.setTint(vectorDrawableForeground, tintColor);
        vectorDrawableBackground.draw(canvas);
        vectorDrawableForeground.draw(canvas);

        // Show AQI on canvas
        Paint text = new Paint();
        text.setTextSize(54);
        text.setColor(Color.WHITE);
        text.setStrokeWidth(10);
        int x = 0;
        if (aqi < 100) x = 45;
        else x = 28;
        canvas.drawText((int)aqi + "", x, 93, text);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private ProgressDialog progressDialog;

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this.getContext(), "Please wait...",
                "Finding direction...", true);
    }

    @Override
    public void onDirectionFinderSuccess(List<com.example.airquality.modules.Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();

        for (Route route : routes) {
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 14));
            binding.tvDistance.setText(route.distance.text);
            binding.tvDuration.setText(route.duration.text);

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
            }

            polylinePaths.add(myGoogleMap.addPolyline(polylineOptions));
        }
    }
}