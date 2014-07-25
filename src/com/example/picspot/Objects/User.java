package com.example.picspot.Objects;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.picspot.MainActivity;
import com.example.picspot.R;
import com.example.picspot.misc.JSONfunctions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class User {
	
	int id;
	String firstname;
	String lastname;
	String email;
	String password;
	String username;
	Bitmap image;
	
	ArrayList<Spot> spots = new ArrayList<Spot>();
	
	public User(int id, String firstname, String lastname, String email,
			String password, String username) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.username = username;
	}
	
	public User(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public ArrayList<Spot> loadSpots(){
		Spot spot = null;
		
		AsyncTask loader = new AsyncTask<Map, Void, JSONArray>() {
	        @Override
	        protected JSONArray doInBackground(Map ...map) {
	        	String url = "http://picspot.weislogel.net/spot.php?type=selectAll&id="+id;
	        	
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
    		    	int id = obj.getInt("s_id");
    		    	String spotName = obj.getString("s_name");
    		    	
    		    	spot = new Spot(id,lat,lng,spotName,1);
    		    	spots.add(spot);
    		    }
    		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    return spots;
	}
	
	
}
