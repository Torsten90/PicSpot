package com.example.picspot.Objects;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Vector;

import android.graphics.Bitmap;

public class Spot {
	private String name ="";
	private Bitmap poster_image;
	private String desc = "";
	private double lng = 0;
	private double lat = 0;
	private Date date;
	private int radius = 0;
	private int creator = 0;
	private int state = 1;
	
	Vector<Bitmap> picArray = new Vector<Bitmap>();
	
	public Spot(double pLat, double pLng,String pName, int pCreator) {
		this.radius = 10;
		this.lat = pLat;
		this.lng = pLng;
		this.name = pName;
		this.creator = pCreator;
	}
	
	public String genSpotUploadURL(){
		
		return "?type=insert&fields[s_name]=" + this.name +
				"&fields[s_desc]=" + this.desc +
				"&fields[s_latitude]=" + this.lat +
				"&fields[s_longitude]=" + this.lng +
				"&fields[s_date]=2014-07-23" +
				"&fields[s_radius]=" + this.radius +
				"&fields[s_creator]=" + this.creator +
				"&fields[s_state]=" + this.state;
	}
	
	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}

	public String getDescription() {
		return this.desc;
	}
}
