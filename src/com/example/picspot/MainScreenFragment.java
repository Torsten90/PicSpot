package com.example.picspot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.example.picspot.R;

import com.example.picspot.Objects.Pic;
import com.example.picspot.Objects.Spot;
import com.example.picspot.Objects.User;
import com.example.picspot.misc.JSONfunctions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainScreenFragment extends Fragment{
	
	public MainScreenFragment() {
		
	}
	
	private boolean hasRegistered = false;
	private User user = null;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Pic lastPic = new Pic();
	
	private GoogleMap gMap;
	private Marker lastOpened = null;
	private Vector<Spot> spotVector = new Vector<Spot>();
	
	private ArrayList<Spot> Spots = new ArrayList<Spot>();
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.main_screen, container, false);
        
        loadSpots();
		
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
		
		CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()));
		CameraUpdate dte = CameraUpdateFactory.zoomTo(15);
		
		gMap.moveCamera(center);
		gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
	    	    fragmentTransaction.replace(R.id.drawer_layout, fragment);
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
		
		SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", getActivity().MODE_PRIVATE); 
		int userId = userDetails.getInt("id", 0);
		
		Spot spot = new Spot(lat,lng, "MySpot",userId);
		String params = spot.genSpotUploadURL();
		
		 AsyncTask loader = new AsyncTask<String, Void, Boolean>() {
	        @Override
	        protected Boolean doInBackground(String ...fields) {
	        	
	        	String url = "http://picspot.weislogel.net/spot.php"+fields[0];
	        	Log.i("url", url);
	        	try {
    	        	HttpClient httpclient = new DefaultHttpClient();
    	        	HttpResponse response = httpclient.execute(new HttpGet(url));
    	        	StatusLine statusLine = response.getStatusLine();
    	        	if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	               ByteArrayOutputStream out = new ByteArrayOutputStream();
    	               response.getEntity().writeTo(out);
    	               out.close();
    	               String responseString = out.toString();
    	               //..more logic
    	        	} else{
    	               //Closes the connection.
    	               response.getEntity().getContent().close();
    	               throw new IOException(statusLine.getReasonPhrase());
    	        	}
	        	} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	return true;	
	        }
	    }.execute(params);
	}
	
	private void loadSpots(){
		Spot spot = null;
		gMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		AsyncTask loader = new AsyncTask<Map, Void, JSONArray>() {
	        @Override
	        protected JSONArray doInBackground(Map ...map) {
	        	String url = "http://picspot.weislogel.net/spot.php?type=selectAll";
	        	
	        	JSONObject jsonResult = JSONfunctions.getJSONfromURL(url);
	        	JSONArray data = new JSONArray();
	        	try {
	        		data = jsonResult.getJSONArray("spots");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	            return data;
	        }
	    }.execute();
		
	    JSONArray data;
	    try {
			data = (JSONArray) loader.get();
			if(data != null) {
    		    for(int i = 0 ; i < data.length() ; i++) {
    		    	
    		    	JSONObject obj = data.getJSONObject(i);
    		    	double lat = Double.parseDouble(obj.getString("s_latitude"));
    		    	double lng = Double.parseDouble(obj.getString("s_longitude"));
    		    	String spotName = obj.getString("s_name");
    		    	
    		    	spot = new Spot(lat,lng,spotName,1);
    		    	
    		    	Spots.add(spot);
    		    	
    		    	MarkerOptions marker = new MarkerOptions().position(new LatLng( spot.getLat(),spot.getLng())).title(spot.getName());
    		    	marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue));
    		    	
    		    	gMap.addMarker(marker);
    		    	spotVector.add(spot);
    		    }
    		    ((MainActivity) getActivity()).setSpots(this.Spots);
    		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
