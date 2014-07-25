package com.example.picspot;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.picspot.Objects.Spot;
import com.example.picspot.Objects.User;
import com.example.picspot.misc.ImageAdapter;
import com.example.picspot.misc.JSONfunctions;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.picspot.R;

public class GalleryFragment extends Fragment{

	private GridView gridView;
	private ImageAdapter customGridAdapter;
	
	private Spot spot = null;
 
	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View detailView = inflater.inflate(R.layout.fragment_gallery, container, false);
 
			gridView = (GridView) detailView.findViewById(R.id.gridView);
			customGridAdapter = new ImageAdapter(getActivity(), R.layout.row_grid, getPics());
			gridView.setAdapter(customGridAdapter);
		
		return detailView;
	}
	 
	 private ArrayList getPics(){
		 final ArrayList imageItems = new ArrayList();
		 
		 AsyncTask loader = new AsyncTask<Map, Void, JSONArray>() {
 	        @Override
 	        protected JSONArray doInBackground(Map ...map) {
 	        	String url = "http://picspot.weislogel.net/image.php?type=selectAll&spot="+spot.getId();
 	        	
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
 	    
 	    String base;
 	    JSONArray data;
	    try {
			data = (JSONArray) loader.get();
			if(data != null) {
   		    for(int i = 0 ; i < data.length() ; i++) {
   		    	JSONObject obj = data.getJSONObject(i);
   		    	
   		    	
   		    	base = obj.getString("i_base");
   		    	
   		    	byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
   		    	Bitmap picBmp = decodeSampledBitmapFromResource( 100, 100, decodedString);
   		    	imageItems.add(picBmp);
   		    	//this.pic = BitmapFactory.decodeFile(imgFile.getAbsolutePath(/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg));
				//Bitmap picBmp = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
   		    	
   		    }
   		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	    
 	    
 	    
		 
		 /*File imgFile = new  File("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
			if(imgFile.exists()){
			    //this.pic = BitmapFactory.decodeFile(imgFile.getAbsolutePath(/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg));
				//Bitmap picBmp = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
				Bitmap picBmp = decodeSampledBitmapFromResource( 100, 100);
				imageItems.add(picBmp);
			}*/
		 return imageItems;
	 }
	 
	 public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	 
	 public static Bitmap decodeSampledBitmapFromResource(
		        int reqWidth, int reqHeight, byte[] decodedString) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
		    
		    BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg",options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    Bitmap bmp1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
			return bmp1;
		}
}
