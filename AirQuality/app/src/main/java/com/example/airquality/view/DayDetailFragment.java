package com.example.airquality.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.airquality.DayDetailAdapter;
import com.example.airquality.R;
import com.example.airquality.model.HourlyAirQuality;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayDetailFragment extends Fragment {

    private RecyclerView rvDayDetail;
    private DayDetailAdapter dayDetailAdapter;
    private ArrayList<HourlyAirQuality> hourArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_day_detail, container, false);
        rvDayDetail=view.findViewById(R.id.rv_dayDetail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvDayDetail.setLayoutManager(layoutManager);
        hourArrayList = new ArrayList<HourlyAirQuality>();
        dayDetailAdapter = new DayDetailAdapter(hourArrayList);
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","1AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","2AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","3AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","4AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","5AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","6AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","7AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","8AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","6AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","7AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","8AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","6AM", 50,25 ,30,20,20,20,100,"Xấu"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","7AM", 50,25 ,30,20,20,20,50,"Tốt"));
        hourArrayList.add(new HourlyAirQuality("Hòa Khánh Bắc","8AM", 50,25 ,30,20,20,20,100,"Xấu"));

        dayDetailAdapter.notifyDataSetChanged();
        rvDayDetail.setAdapter(dayDetailAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_back,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_back){
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();

            Fragment fragment=fragmentManager.findFragmentById(R.id.fl_home);
            if(fragment!=null){
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}