package com.example.picspot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment{
	
	private EditText  username=null;
	private EditText  password=null;
	private TextView attempts;
	private Button login;
	Context context;
	
    public LoginFragment(Context context) {
    	context = context;
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_login, container, false);
        
        username = (EditText)detailView.findViewById(R.id.editText1);
        password = (EditText)detailView.findViewById(R.id.editText2);
        login = (Button)detailView.findViewById(R.id.button1);
        
        login.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
        		
        		String passwordHash = Helper.md5(password.getText().toString());
        		Log.i("hash", passwordHash);
        		
        		String strPassword = password.getText().toString();
        		String strUsername = username.getText().toString();
        		
        		String url = "http://picspot.weislogel.net?user="+strUsername+"&pass="+strPassword;
        		
        		Map<String, String> map = new HashMap<String, String>();
        		map.put("strPassword", strPassword);
        		map.put("strUsername", strUsername);
        		
        		new AsyncTask<Map, Void, Void>() {
        	        @Override
        	        protected Void doInBackground(Map ...map) {
        	        	JSONObject jsonobject = JSONfunctions.getJSONfromURL("http://picspot.weislogel.net?user="+map.get("strUsername")+"&pass="+map.get("strPassword"));
        	            //Log.v("Stackoverflow", json.toString());
        	            return null;
        	        }
        	    }.execute();
        		
        		try {
					JSONArray user = (JSONArray) jsonobject.get("user");
					for (int i = 0; i < user.length(); i++)
					{
					    String struser = user.getJSONObject(i).getString("post_id");
					    //Log.i("user", struser);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		
        		if(strUsername.equals("admin") && strPassword.equals("admin")){
    		        Toast.makeText(getActivity(), "Redirecting...", 
    		        Toast.LENGTH_SHORT).show();
    	        } else {
    		        Toast.makeText(getActivity(), "Wrong Credentials",
    		        Toast.LENGTH_SHORT).show();
    	        }
        	}
        });
        
        
        return detailView;
    }
   
}
	
