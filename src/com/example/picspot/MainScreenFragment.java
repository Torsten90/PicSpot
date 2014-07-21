package com.example.picspot;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.picspot.Objects.Spot;

public class MainScreenFragment extends Fragment{

	Context context;
	
	public MainScreenFragment() {
		
	}	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.main_screen, container, false);
        
        final Button btnAddSpot = (Button) resultView.findViewById(R.id.btnAddSpot);
        
        btnAddSpot.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
        		LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        		boolean enabled = locManager
        		  .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        		if (!enabled) {
        		  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        		  startActivity(intent);
        		} 
        		
        		Location currentLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        		if(currentLoc == null)
        			currentLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        		addSpot(currentLoc.getLatitude() ,currentLoc.getLongitude());	
        	}
        });
        
        return resultView;
    }
    
	
	private void addSpot(double latitude, double longitude){
		Spot spot = new Spot(longitude,latitude);
		Log.i("tag","Lonitude: " + spot.getLng()+ " Latitude: " + spot.getLat());
	}
	
}
