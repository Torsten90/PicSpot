package com.example.picspot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SpotDetailFragment extends Fragment{

	public SpotDetailFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.spot_details, container, false);
        
        final Button btnAddPic = (Button) resultView.findViewById(R.id.btnAddPic);
        final Button btnCam = (Button) resultView.findViewById(R.id.btnCam);
        final Button btnGallerie = (Button) resultView.findViewById(R.id.btnGallerie);
        
        btnAddPic.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
        		
        		AddPicSelectionFragment fragment = new AddPicSelectionFragment();
        		
        		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	    	    /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    	    
	    	    fragmentTransaction.addToBackStack(null);
	    	    fragmentTransaction.replace(R.id.container, fragment);
	    	    fragmentTransaction.commit();
        		
        		fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                					 .show(fragment)
                					 .commit();
        		*/
        		
        		btnCam.setVisibility(View.VISIBLE);
        		btnGallerie.setVisibility(View.VISIBLE);
        	}    	
        });
        
        return resultView;
	}   
}
