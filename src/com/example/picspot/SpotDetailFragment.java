package com.example.picspot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.picspot.Objects.Spot;
import com.example.picspot.Objects.User;
import com.example.picspot.misc.Helper;
import com.example.picspot.misc.SpotAdapter;




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
        
        final Button btnSaveSpotDetails = (Button) resultView.findViewById(R.id.btnSaveSpotDetails);
        
        final EditText etSpotName = (EditText) resultView.findViewById(R.id.tvSpotName);
        final EditText tvSpotDesc = (EditText) resultView.findViewById(R.id.etTextField);
        
        if(spot != null){
        	etSpotName.setText(spot.getName());
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
        
        btnSaveSpotDetails.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
			
			   String params = String.format("?type=update&id=%s&fields[s_name]=%s&fields[s_desc]=%s", 
					   spot.getId(), Helper.urlWhiteSpace(etSpotName.getText().toString()), Helper.urlWhiteSpace(tvSpotDesc.getText().toString()));	 
				
				AsyncTask loader = new AsyncTask<String, Void, Boolean>() {
         	        @Override
         	        protected Boolean doInBackground(String ...fields) {
         	        	
         	        	String url = "http://picspot.weislogel.net/spot.php"+fields[0];
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
        		
	         	   boolean requestSend = false;
					try {
						requestSend = (Boolean) loader.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	         	   
		       	   if(requestSend){
		       		   Toast.makeText(getActivity().getApplicationContext(), "Speichern erfolgreich", Toast.LENGTH_SHORT).show();
		       		   
		       		   SpotAdapter spotAdapter = ((MainActivity) getActivity()).getSpotAdapter();
		       		   if(((MainActivity) getActivity()).getSpotAdapter() != null){
		       			   	SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", getActivity().MODE_PRIVATE); 
		       			   	int userId = userDetails.getInt("id", 0);
		         	   
		         	   		User user = new User(userId);
		         	   		ArrayList<Spot> spots = user.loadSpots(); 
		         	   		
		         	   		spotAdapter.clear();
		         	   		spotAdapter.addAll(spots);
		         	   		spotAdapter.notifyDataSetChanged();
		       		   }
		       		   
		       	   }
				
				
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
        		
        		Toast.makeText(getActivity(), "loading", 
         		Toast.LENGTH_SHORT).show();
        		
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
