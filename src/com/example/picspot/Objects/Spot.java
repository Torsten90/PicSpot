package com.example.picspot.Objects;

public class Spot {
	//name
	//poster_image
	//desc;
	double lng;
	double lat;
	//date
	int radius;
	//creator
	//state
	
	public Spot(double pLng, double pLat) {
		this.radius = 10;
		this.lng = pLng;
		this.lat = pLat;
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
	
}
