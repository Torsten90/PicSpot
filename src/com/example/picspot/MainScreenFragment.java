package com.example.picspot;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

import com.example.picspot.Objects.Spot;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainScreenFragment extends Fragment{
	
	public MainScreenFragment() {
		
	}
	
	private GoogleMap gMap;
	private Marker lastOpened = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.main_screen, container, false);
        
        loadSpots();
       
        gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
            	 // Check if there is an open info window
                if (lastOpened != null) {
                    // Close the info window
                    lastOpened.hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpened.equals(marker)) {
                        // Nullify the lastOpened object
                        lastOpened = null;
                        // Return so that the info window isn't opened again
                        return true;
                    } 
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	    	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    	    SpotDetailFragment fragment = new SpotDetailFragment();
	    	    
	    	    fragmentTransaction.addToBackStack(null);
	    	    fragmentTransaction.replace(R.id.container, fragment);
	    	    fragmentTransaction.commit();
                
                // Re-assign the last opened such that we can close it later
                lastOpened = marker;

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });
        
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
	
	private void addSpot(double lat, double lng){
		Spot spot = new Spot(lat,lng, "Firstspot");
	}
	
	private void loadSpots(){
		Spot spot = new Spot( 48.0503177,8.2141939, "Firstspot");
		gMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.addMarker( new MarkerOptions().position(new LatLng( spot.getLat(),spot.getLng())).title(spot.getName()));
	}
}
