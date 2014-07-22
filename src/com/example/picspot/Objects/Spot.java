package com.example.picspot.Objects;

public class Spot {
	String name;
	

	//poster_image
	//desc;
	double lng;
	double lat;
	//date
	int radius;
	//creator
	//state
	
	public Spot(double pLat, double pLng,String pName) {
		this.radius = 10;
		this.lat = pLat;
		this.lng = pLng;
		this.name = pName;
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
}
