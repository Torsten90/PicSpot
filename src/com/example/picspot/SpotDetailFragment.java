package com.example.picspot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.picspot.Objects.Spot;




public class SpotDetailFragment extends Fragment{

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	private Spot spot = null;
	
	public SpotDetailFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.spot_details, container, false);
        
        final Button btnAddPic = (Button) resultView.findViewById(R.id.btnAddPic);
        final Button btnCam = (Button) resultView.findViewById(R.id.btnCam);
        final Button btnGallerie = (Button) resultView.findViewById(R.id.btnGallerie);
        final Button btnShowPics = (Button) resultView.findViewById(R.id.btnShowPics);
        
        TextView tvSpotName = (TextView) resultView.findViewById(R.id.tvSpotName);
        TextView tvSpotDesc = (TextView) resultView.findViewById(R.id.tvSpotDetailDesc);
        
        if(spot != null){
        	tvSpotName.setText(spot.getName());
        	String desc = spot.getDescription();
        	if(desc.equals("")){
        		desc = "Keine Beschreibung hinterlegt";
        	}
        	tvSpotDesc.setText(desc);
        }
        
        btnAddPic.setOnClickListener(new Button.OnClickListener(){
        	@Override
			public void onClick(View view){
        		btnCam.setVisibility(View.VISIBLE);
        		btnGallerie.setVisibility(View.VISIBLE);
        	}    	
        });
        
        btnCam.setOnClickListener(new Button.OnClickListener(){
        		
       		@Override
       	    public void onClick(View v) {
       			// create Intent to take a picture and return control to the calling application
       		    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       		    String name = "IMG_"+ timeStamp + ".jpg";
       		    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name); // create a file to save the image
       		    
       		    String pfad = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +  "/PicSpot/";
       		   ((MainActivity) getActivity()).setLastPicPath(pfad+name);
       		   ((MainActivity) getActivity()).setLastPicName(name);
       		   ((MainActivity) getActivity()).setSelectedSpot(spot);
       		
       		    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
       		    Log.i("test",fileUri.toString());
       		    
       		    // start the image capture Intent
       		    getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
       		}
        });
        
        btnShowPics.setOnClickListener(new Button.OnClickListener(){
        	@Override
       	    public void onClick(View v) {
        		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
 	    	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
 	    	    GalleryFragment fragment = new GalleryFragment();
 	    	    
 	    	    fragment.setSpot(spot);
 	    	    
 	    	    fragmentTransaction.addToBackStack(null);
 	    	    fragmentTransaction.replace(R.id.drawer_layout, fragment);
 	    	    fragmentTransaction.commit();
        	}
        });
        return resultView;
	}   
	
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type, String name){
	      return Uri.fromFile(getOutputMediaFile(type, name));
	}

	
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type, String name){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "PicSpot");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("PicSpot", "failed to create directory");
	            return null;
	        }
	    }
	    // Create a media file name
	    
	    File mediaFile;
	   
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        name);
	        
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	public void setSelectedSpot(Spot spot) {
		// TODO Auto-generated method stub
		this.spot = spot;
	}
}
