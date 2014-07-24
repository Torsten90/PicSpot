package com.example.picspot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.picspot.R;

public class ProfileFragment extends Fragment{

	
	public ProfileFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.profile_details, container, false);
         	
		 	SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", getActivity().MODE_PRIVATE); 
		 	
			String firstname = userDetails.getString("firstname", "");
			String lastname = userDetails.getString("lastname", "");
			String username = userDetails.getString("username", "");
			String email = userDetails.getString("email", "");
		 
			TextView tvName = (TextView) rootView.findViewById(R.id.tvProfileDetailName);
			TextView tvMail = (TextView) rootView.findViewById(R.id.tvProfileDetailMail);
			TextView tvUsername = (TextView) rootView.findViewById(R.id.tvProfileDetailUsername);
		 
			tvName.setText(firstname + " " + lastname);
			tvMail.setText(email);
			tvUsername.setText(username);
		 
			return rootView;
	}   
	
}
