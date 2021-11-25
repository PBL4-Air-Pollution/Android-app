package com.example.airquality;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.view.HourDetailFragment;
import com.example.airquality.view.MapsFragment;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirebaseService extends Service {
    private DatabaseReference mDatabase;
    private AppDatabase appDatabase;
    private HourlyAirQualityDAO hourlyAirQualityDAO;
    private DailyAirQualityDAO dailyAirQualityDAO;
    private LocationDAO locationDAO;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private void setUpFirebaseConnection() {
        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("HourlyAirQuality");
    }

    private void setUpLocalDatabase() {
        appDatabase = AppDatabase.Instance(getApplicationContext());
        hourlyAirQualityDAO = appDatabase.hourlyAirQualityDAO();
        dailyAirQualityDAO = appDatabase.dailyAirQualityDAO();
        locationDAO = appDatabase.locationDAO();
    }

    @SuppressLint("SimpleDateFormat")
    private void fetchAirQualityData() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        HourlyAirQuality hourlyAirQuality = snapshot.getValue(HourlyAirQuality.class);

                        if (hourlyAirQuality != null) {
                            if (hourlyAirQualityDAO.findByLocationIdAndDatetime(hourlyAirQuality.getLocationID(), hourlyAirQuality.getDatetime()) == null){
                                // Add into local database
                                hourlyAirQualityDAO.insertAll(hourlyAirQuality);

                                // Delete the 7 days earlier data
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -7);
                                String deleteDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                                hourlyAirQualityDAO.deleteByDate(deleteDate);
                                dailyAirQualityDAO.deleteByDate(deleteDate);

                                // Check AQI -> push notification
                                // Bitmap bitmap=
                                // BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                                // Notification notification= new Notification.Builder(getApplicationContext())
                                // .setContentTitle("Title")
                                // .setContentText(hourlyAirQuality.getLocationID()+"
                                // "+hourlyAirQuality.getDatetime()+" "+hourlyAirQuality.getAqi())
                                // .setLargeIcon(bitmap)
                                // .build();
                                // NotificationManager notificationManager=(NotificationManager)
                                // getSystemService(Context.NOTIFICATION_SERVICE);
                                // if(notificationManager!=null)
                                // notificationManager.notify(1,notification);

                                // Update currentAQI and Rated of location
                                int locationID = hourlyAirQuality.getLocationID();
                                double aqi = hourlyAirQuality.getAqi();
                                String rate = hourlyAirQuality.getRated();
                                locationDAO.updateAqiAndRate(locationID, aqi, rate);

                                // Calculate average AQI of day if end of day
                                String date = hourlyAirQuality.getDatetime().split(" ")[0];
                                String time = hourlyAirQuality.getDatetime().split(" ")[1];
                                if (time.equals("23:00:00")) {
                                    double sumAqi = 0;
                                    int count = 0;
                                    List<HourlyAirQuality> hourlyAirQualityList = hourlyAirQualityDAO
                                            .getListByLocationIDAndDate(locationID, date);
                                    for (HourlyAirQuality hourly : hourlyAirQualityList) {
                                        if (hourly.getDatetime().contains(date)) {
                                            sumAqi += hourly.getAqi();
                                            count++;
                                        }
                                    }
                                    double avgAqi = sumAqi / count;

                                    String dailyRate = "";
                                    if (avgAqi < 50)
                                        dailyRate = "Tốt";
                                    else if (avgAqi < 100)
                                        dailyRate = "Trung bình";
                                    else if (avgAqi < 150)
                                        dailyRate = "Kém";
                                    else if (avgAqi < 200)
                                        dailyRate = "Xấu";
                                    else if (avgAqi < 300)
                                        dailyRate = "Rất xấu";
                                    else if (avgAqi < 500)
                                        dailyRate = "Nguy hại";

                                    dailyAirQualityDAO.insertAll(new DailyAirQuality(locationID, date, avgAqi, dailyRate));
                                }
                            }
                        }
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
}
