package com.example.airquality;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.airquality.Adapters.ViewPagerAdapter;
import com.example.airquality.databinding.ActivityMainBinding;
import com.example.airquality.model.Location;
import com.example.airquality.view.HomeFragment;
import com.example.airquality.view.InfoFragment;
import com.example.airquality.view.LocationFragment;
import com.example.airquality.view.MapsFragment;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Intent intent = new Intent(this, FirebaseService.class);
        startService(intent);

        setContentView(view);

        setUpViewPager();

        setUpBottomNavBar();

        setUpLocalDatabase();

        setUpNotification();
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        binding.navBottom.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        binding.navBottom.getMenu().findItem(R.id.action_map).setChecked(true);
                        break;
                    case 2:
                        binding.navBottom.getMenu().findItem(R.id.action_info).setChecked(true);
                        break;
                    case 3:
                        binding.navBottom.getMenu().findItem(R.id.action_location).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpBottomNavBar(){
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                Fragment fragment=fragmentManager.findFragmentById(R.id.fl_home);
                if(fragment!=null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
                binding.viewPager.setOffscreenPageLimit(1);
                switch (item.getItemId()){
                    case R.id.action_home:
                        HomeFragment homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, homeFragment)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.action_map:
                        MapsFragment mapsFragment = new MapsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, mapsFragment)
                          .addToBackStack(null).commit();
//                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_info:
                        InfoFragment infoFragment = new InfoFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, infoFragment)
                                .addToBackStack(null).commit();
//                        binding.viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_location:
                        LocationFragment locationFragment = new LocationFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, locationFragment)
                                .addToBackStack(null).commit();
//                        binding.viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void setUpLocalDatabase() {
        AppDatabase appDatabase = AppDatabase.Instance(getApplicationContext());
        locationDAO = appDatabase.locationDAO();
    }

    private void setUpNotification() {
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