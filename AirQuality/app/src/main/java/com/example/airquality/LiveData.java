package com.example.airquality;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveData extends ViewModel {
    private MutableLiveData<Integer> locationID;

    public androidx.lifecycle.LiveData<Integer> getLocationID(){
        if(locationID==null){
            locationID=new MutableLiveData<>();
            locationID.setValue(0);
        }
        return locationID;
    }
    public void setLocationID(Integer integer){
        if(locationID!=null)
            locationID.setValue(integer);
    }

}
