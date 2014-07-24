package com.example.picspot;

import java.util.ArrayList;

import com.example.picspot.Objects.Spot;
import com.example.picspot.misc.SpotAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.picspot.R;


public class SpotListFragment extends Fragment {
    
   public SpotListFragment(){}
    
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
       View rootView = inflater.inflate(R.layout.spot_list, container, false);
       
       ListView listView = (ListView) rootView.findViewById(R.id.lvSpots);
       ArrayList<Spot> Spots = ((MainActivity) getActivity()).getSpots();
       
       TextView tvSpotListEmpty = (TextView) rootView.findViewById(R.id.tvSpotListError);
       if(Spots.size() == 0){
    	   tvSpotListEmpty.setVisibility(View.VISIBLE);
       } else {
    	   SpotAdapter adapter = new SpotAdapter(getActivity(), R.layout.spot_list_adapter, Spots);
           listView.setAdapter(adapter);
           tvSpotListEmpty.setVisibility(View.GONE);
       }
       
       
       
       return rootView;
   }
}
