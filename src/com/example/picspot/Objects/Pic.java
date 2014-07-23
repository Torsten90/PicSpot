package com.example.picspot.Objects;

import android.graphics.Bitmap;

public class Pic {

	private Bitmap pic;
	private String localPath;
	private String name;
	private String description;
	private int creator;
	private double lat;
	private double lng;

	public Pic() {

	}
	public Pic(String pPath,int pCreator, double pLat, double pLng ) {
		this.localPath = pPath;
		this.creator = pCreator;
		this.lat = pLat;
		this.lng =pLng;
	}
	
	private void uploadPic(){
		
	}
	
	
	@Override
	public String toString() {
		return "Pic [pic=" + pic + ", localPath=" + localPath + ", name="
				+ name + ", description=" + description + ", creator="
				+ creator + ", lat=" + lat + ", lng=" + lng + "]";
	}
	public Bitmap getPic() {
		return pic;
	}
	public void setPic(Bitmap pic) {
		this.pic = pic;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}

}
