package com.example.picspot;

import java.util.ArrayList;

import com.example.picspot.Objects.Spot;
import com.example.picspot.Objects.User;
import com.example.picspot.misc.SpotAdapter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picspot.R;


public class SpotListFragment extends Fragment {
    
   public SpotListFragment(){}
   
   private ArrayList spots = null;
   private ListView listView;
   private SpotAdapter adapter;
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
       View rootView = inflater.inflate(R.layout.spot_list, container, false);
       
       listView = (ListView) rootView.findViewById(R.id.lvSpots);
       spots = ((MainActivity) getActivity()).getSpots();
       
       this.spots = spots;
       
       // falls keine spots bsiher geladen wurden versuche welche zu laden
       if(spots.size() == 0){
    	   SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", getActivity().MODE_PRIVATE); 
           int userId = userDetails.getInt("id", 0);
    	   
    	   User user = new User(userId);
           spots = user.loadSpots(); 
       }
       
       TextView tvSpotListEmpty = (TextView) rootView.findViewById(R.id.tvSpotListError);
       if(spots.size() == 0){ // Falls der Benutzer noch keine Spots angelegt hat
    	   tvSpotListEmpty.setVisibility(View.VISIBLE);
       } else { // Spot Liste anzeigen
    	   tvSpotListEmpty.setVisibility(View.GONE);
    	   adapter = new SpotAdapter(getActivity(), R.layout.spot_list_adapter, spots);
           
    	   ((MainActivity) getActivity()).setSpotAdapter(adapter);
    	   
    	   listView.setAdapter(adapter);
           listView.setOnItemClickListener(new ListView.OnItemClickListener(){
	           	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	        		FragmentManager fragmentManager = getFragmentManager();
	        	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	        	    SpotDetailFragment fragment = new SpotDetailFragment();
	        	    
	        	    spots = null;
	        	    
	        	    fragment.setSelectedSpot((Spot) listView.getItemAtPosition(position));
	        	    
	        	    fragmentTransaction.addToBackStack(null);
	        	    fragmentTransaction.replace(R.id.drawer_layout, fragment);
	        	    fragmentTransaction.commit();
	        	}
	        }
        );
           
       }
       
       
       
       return rootView;
   }
}
