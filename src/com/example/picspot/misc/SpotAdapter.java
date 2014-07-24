package com.example.picspot.misc;

import java.util.ArrayList;

import com.example.picspot.Objects.Spot;
import com.example.picspot.misc.ImageAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.picspot.R;


public class SpotAdapter extends ArrayAdapter<Spot>{

    Context context; 
    int layoutResourceId;    
    ArrayList data = new ArrayList();
    
    public SpotAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SpotHolder holder = null;
        
        if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new SpotHolder();
			//holder.imageTitle = (TextView) row.findViewById(R.id.text);
			holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
			
            row.setTag(holder);
            
		} else {
			holder = (SpotHolder) row.getTag();
		}
        
        Spot spot = (Spot) data.get(position);
        holder.txtTitle.setText(spot.getName());
        
        return row;
    }
    
    static class SpotHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}