package com.example.airquality;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService extends Service {

    private DatabaseReference mDatabase;
    private AppDatabase appDatabase;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private DailyAirQualityDAO dailyAirQualityDAO;
    private LocationDAO locationDAO;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpFirebaseConnection();

        setUpLocalDatabase();

        fetchAirQualityData();

        return START_STICKY;
    }

    private void setUpFirebaseConnection(){
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpLocalDatabase() {
        appDatabase = AppDatabase.Instance(getApplicationContext());
        hourlyAirQualityDAO = appDatabase.hourlyAirQualityDAO();
        dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
        locationDAO = appDatabase.locationDAO();
    }

    private void fetchAirQualityData(){
        mDatabase.child("HourlyAirQuality").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        HourlyAirQuality hourlyAirQuality = snapshot.getValue(HourlyAirQuality.class);
                        hourlyAirQualityDAO.insertAll(hourlyAirQuality);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
