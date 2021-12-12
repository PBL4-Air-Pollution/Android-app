package com.example.airquality;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirebaseService extends Service {
    private DatabaseReference mDatabase;
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
        AppDatabase appDatabase = AppDatabase.Instance(getApplicationContext());
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
                            if (hourlyAirQualityDAO.findByLocationIdAndDatetime(hourlyAirQuality.getLocationID(),
                                    hourlyAirQuality.getDatetime()) == null) {
                                // Add into local database
                                hourlyAirQualityDAO.insertAll(hourlyAirQuality);

                                // Delete the 7 days earlier data
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -7);
                                String deleteDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                                hourlyAirQualityDAO.deleteByDate(deleteDate);
                                dailyAirQualityDAO.deleteByDate(deleteDate);

                                // Check AQI -> push notification
                                Location location;

                                if (locationDAO.getFavouriteLocation() == null) location = locationDAO.getByID(1);
                                else location = locationDAO.getFavouriteLocation();

                                NotificationManager manager = (NotificationManager) getSystemService(
                                        NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= 26) {
                                    // When sdk version is larger than26
                                    String id = "channel_1";
                                    String description = "143";
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_green);
                                    int importance = NotificationManager.IMPORTANCE_LOW;
                                    NotificationChannel channel = new NotificationChannel(id, description, importance);
                                    manager.createNotificationChannel(channel);
                                    Notification notification = new Notification.Builder(getApplicationContext(), id)
                                            .setCategory(Notification.CATEGORY_MESSAGE)
                                            .setSmallIcon(R.drawable.app_logo)
                                            .setLargeIcon(getCroppedBitmap(location.getRated()))
                                            .setContentTitle("AQI :"+(int)location.getAqi()+" - "+location.getRated())
                                            .setContentText(getRecommended(location.getRated()))
                                            .setSubText(location.getStationName())
                                            .setAutoCancel(false).setOngoing(true).build();
                                    manager.notify(1, notification);
                                } else {
                                    // When sdk version is less than26
                                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                            .setContentTitle("This is content title")
                                            .setContentText("This is content text").setSmallIcon(R.mipmap.ic_launcher)
                                            .build();
                                    manager.notify(1, notification);
                                }

                                // Update currentAQI and Rated of location
                                int locationID = hourlyAirQuality.getLocationID();
                                double aqi = hourlyAirQuality.getAqi();
                                String rate = hourlyAirQuality.getRated();
                                locationDAO.updateAqiAndRate(locationID, aqi, rate);

                                // Calculate average AQI of day if end of day
                                String date = hourlyAirQuality.getDatetime().split(" ")[0];
                                String time = hourlyAirQuality.getDatetime().split(" ")[1];
                                if (time.equals("23:00:00")) {
                                    if (dailyAirQualityDAO.findByLocationIdAndDate(locationID, date) == null){
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

                                        dailyAirQualityDAO
                                                .insertAll(new DailyAirQuality(locationID, date, avgAqi, dailyRate));
                                    }
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

    public Bitmap getCroppedBitmap(String rate) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_green);;
        switch (rate) {
            case "Tốt": // Xanh lá
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_green);
                break;
            case "Trung bình": // Vàng
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_yellow);
                break;
            case "Kém": // Cam
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_orange);
                break;
            case "Xấu": // Đỏ
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_red);
                break;
            case "Rất xấu": // Tím
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_purple);
                break;
            case "Nguy hại": // Nâu
                bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar_brown);
                break;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public String getRecommended(String rate){
        String recommended="";
        switch (rate) {
            case "Tốt": // Xanh lá
                recommended="Không ảnh hướng tới sức khỏe";
                break;
            case "Trung bình": // Vàng
                recommended="Nhóm nhạy cảm nên hạn chế thời gian ở bên ngoài";
                break;
            case "Kém": // Cam
                recommended="Nhóm nhạy cảm hạn chế thời gian ở bên ngoài";
                break;
            case "Xấu": // Đỏ
                recommended="Nhóm nhạy cảm tránh ra ngoài. Những người khác hạn chế ra ngoài";
                break;
            case "Rất xấu": // Tím
                recommended="Nhóm nhạy cảm tránh ra ngoài. Những người khác hạn chế ra ngoài";
                break;
            case "Nguy hại": // Nâu
                recommended="Mọi người nên ở nhà";
                break;
        }
        return recommended;
    }

}
