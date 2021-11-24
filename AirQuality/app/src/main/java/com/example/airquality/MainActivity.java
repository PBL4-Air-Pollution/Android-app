package com.example.airquality;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.airquality.Adapters.ViewPagerAdapter;
import com.example.airquality.databinding.ActivityMainBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

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
    }

    public void TaoDuLieu()
    {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase = AppDatabase.Instance(getApplicationContext().getApplicationContext());
//                LocationDAO locationDAO= appDatabase.locationDAO();
//                locationDAO.insertLocations(new Location("Hoa Khanh Bac","School",true));
//                locationDAO.insertLocations(new Location("Hai Chau","Home",true));
//                locationDAO.insertLocations(new Location("Nui Thanh","",false));
//                locationDAO.insertLocations(new Location("Hoa Xuan","",false));
//
//            }
//        });
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase = AppDatabase.Instance(getApplicationContext());
//                dailyAirQualityDAO= appDatabase.dailyAirQualityDAO();
//
//                try {
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy").parse("12/11/2021"), 20, "Totb"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy").parse("13/11/2021"), 50, "Totb"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy").parse("14/11/2021"), 60, "TBb"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy").parse("15/11/2021"), 70, "TBb"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy").parse("16/11/2021"), 100, "TBb"));
//
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy").parse("12/11/2021"), 25, "Totc"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy").parse("13/11/2021"), 55, "Totc"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy").parse("14/11/2021"), 65, "TBc"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy").parse("15/11/2021"), 75, "TBc"));
//                    dailyAirQualityDAO.insertAll(new DailyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy").parse("16/11/2021"), 105, "TBc"));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase = AppDatabase.Instance(getApplicationContext());
//                hourlyAirQualityDAO= appDatabase.hourlyAirQualityDAO();
//                try {
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 01:00"), 111,11,12,13,14,15,50,"Tốt50c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 02:00"), 112,11,12,13,14,15,51,"Xấu51c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 03:00"), 113,11,12,13,14,15,52,"Xấu52c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 01:00"), 114,11,12,13,14,15,60,"Xấu53c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 02:00"), 115,11,12,13,14,15,61,"Xấu61c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 03:00"), 116,11,12,13,14,15,62,"Xấu62c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 01:00"), 117,11,12,13,14,15,70,"Xấu70c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 02:00"), 118,11,12,13,14,15,71,"Xấu71c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 03:00"), 119,11,12,13,14,15,72,"Xấu72c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 01:00"), 120,11,12,13,14,15,80,"Xấu80c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 02:00"), 121,11,12,13,14,15,81,"Xấu81c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 03:00"), 122,11,12,13,14,15,82,"Xấu82c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 01:00"), 123,11,12,13,14,15,90,"Xấu90c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 02:00"), 124,11,12,13,14,15,91,"Xấu91c"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hai Chau", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 03:00"), 125,11,12,13,14,15,92,"Xấu92c"));
//
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 01:00"), 11,11,12,13,14,15,50,"Tốt50b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 02:00"), 12,11,12,13,14,15,51,"Xấu51b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2021 03:00"), 13,11,12,13,14,15,52,"Xấu52b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 01:00"), 14,11,12,13,14,15,60,"Xấu53b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 02:00"), 15,11,12,13,14,15,61,"Xấu61b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/11/2021 03:00"), 16,11,12,13,14,15,62,"Xấu62b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 01:00"), 17,11,12,13,14,15,70,"Xấu70b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 02:00"), 18,11,12,13,14,15,71,"Xấu71b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("14/11/2021 03:00"), 19,11,12,13,14,15,72,"Xấu72b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 01:00"), 20,11,12,13,14,15,80,"Xấu80b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 02:00"), 21,11,12,13,14,15,81,"Xấu81b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("15/11/2021 03:00"), 22,11,12,13,14,15,82,"Xấu82b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 01:00"), 23,11,12,13,14,15,90,"Xấu90b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 02:00"), 24,11,12,13,14,15,91,"Xấu91b"));
//                    hourlyAirQualityDAO.insertAll(new HourlyAirQuality("Hoa Khanh Bac", new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/11/2021 03:00"), 25,11,12,13,14,15,92,"Xấu92b"));
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

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
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                Fragment fragment=fragmentManager.findFragmentById(R.id.fl_home);
                if(fragment!=null){
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
                binding.viewPager.setOffscreenPageLimit(1);
                switch (item.getItemId()){
                    case R.id.action_home:
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_map:
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_info:
                        binding.viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_location:
                        binding.viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}