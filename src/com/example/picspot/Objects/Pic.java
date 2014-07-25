package com.example.picspot.Objects;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Pic {

	private Bitmap pic;
	private String localPath = "";
	private String name = "";
	private String description ="";
	private int creator=0;
	private double lat=0.0;
	private double lng=0.0;

	public Pic() {

	}
	public Pic(String pPath,int pCreator, double pLat, double pLng ) {
		this.localPath = pPath;
		this.creator = pCreator;
		this.lat = pLat;
		this.lng =pLng;
	}
	
	public void ladeBitmap(){
		//File imgFile = new  File(this.localPath);
		File imgFile = new  File("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
		if(imgFile.exists()){
		    //this.pic = BitmapFactory.decodeFile(imgFile.getAbsolutePath(/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg));
			this.pic = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
		}
	}
	
	
	public String encodePic(){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		this.pic.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return encoded;
			
	}
	public String genPicUploadURL(){
		
		/*return "?type=insert&fields[s_name]=" + this.name +
				"&fields[s_desc]=" + this.desc +
				"&fields[s_latitude]=" + this.lat +
				"&fields[s_longitude]=" + this.lng +
				"&fields[s_date]=2014-07-23" +
				"&fields[s_radius]=" + this.radius +
				"&fields[s_creator]=" + this.creator +
				"&fields[s_state]=" + this.state;*/
	
		
		return "?type=insert&spot=1&fields[i_name]=" + this.name +
				"&fields[i_description]=" + this.description +
				"&fields[i_owner]=" + this.creator +
				"&fields[i_latitude]=" + this.lat +
				"&fields[i_longitude]=" + this.lng + 
				"&image=/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB";
				//encoded;  
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
