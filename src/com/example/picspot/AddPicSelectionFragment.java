package com.example.picspot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.picspot.R;

public class AddPicSelectionFragment extends Fragment {

	public AddPicSelectionFragment() {
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.add_pic_selection, container, false);
        
        return resultView;
	};
}
