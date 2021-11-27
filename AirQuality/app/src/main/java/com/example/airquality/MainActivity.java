package com.example.airquality;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.airquality.Adapters.ViewPagerAdapter;
import com.example.airquality.databinding.ActivityMainBinding;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.view.HomeFragment;
import com.example.airquality.view.InfoFragment;
import com.example.airquality.view.LocationFragment;
import com.example.airquality.view.MapsFragment;
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
}