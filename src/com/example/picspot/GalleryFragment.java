package com.example.picspot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.picspot.Objects.Pic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class GalleryFragment extends Fragment{

	private GridView gridView;
	private ImageAdapter customGridAdapter;
 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View detailView = inflater.inflate(R.layout.fragment_gallery, container, false);
 
			gridView = (GridView) detailView.findViewById(R.id.gridView);
			customGridAdapter = new ImageAdapter(getActivity(), R.layout.row_grid, getPics());
			gridView.setAdapter(customGridAdapter);
		
		return detailView;
	}
 
	/*private ArrayList getData() {
		final ArrayList imageItems = new ArrayList();
		// retrieve String drawable array
		TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
		for (int i = 0; i < imgs.length(); i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
					imgs.getResourceId(i, -1));
			imageItems.add(new Pic());
		}
 
		return imageItems;
 
	}*/
	 
	 private ArrayList getPics(){
		 final ArrayList imageItems = new ArrayList();
		 File imgFile = new  File("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
			if(imgFile.exists()){
			    //this.pic = BitmapFactory.decodeFile(imgFile.getAbsolutePath(/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg));
				//Bitmap picBmp = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg");
				Bitmap picBmp = decodeSampledBitmapFromResource( 100, 100);
				imageItems.add(picBmp);
			}
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
		        int reqWidth, int reqHeight) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg",options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeFile("/storage/emulated/0/Pictures/PicSpot/IMG_20140723_174628.jpg",options);
		}
}
