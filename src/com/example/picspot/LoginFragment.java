package com.example.picspot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.picspot.Objects.User;
import com.example.picspot.misc.Helper;
import com.example.picspot.misc.JSONfunctions;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.picspot.R;

public class LoginFragment extends Fragment{
	
	private EditText  username=null;
	private EditText  password=null;
	private TextView attempts;
	private Button login;
	private Button register;
	
	Context context;
	
    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_login, container, false);
        
        getActivity().getActionBar().hide();
        
        username = (EditText)detailView.findViewById(R.id.editText1);
        password = (EditText)detailView.findViewById(R.id.editText2);
        login = (Button)detailView.findViewById(R.id.button1);
        
        register = (Button)detailView.findViewById(R.id.btnLoginRegister);
        register.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
        		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	    	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    	    RegisterFragment fragment = new RegisterFragment();
	    	    
	    	    fragmentTransaction.addToBackStack(null);
	    	    fragmentTransaction.replace(R.id.drawer_layout, fragment);
	    	    fragmentTransaction.commit();
        	}
        });
        
        if(((MainActivity) getActivity()).getHasRegistered()){
        	((MainActivity) getActivity()).setHasRegistered(false);
        	Toast.makeText(getActivity().getApplicationContext(), "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
        }
        
        
        login.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
        		
        		String strPassword 			= password.getText().toString();
        		final String strUsername 	= username.getText().toString();
        		final String passwordHash = Helper.md5(strPassword);
        		
        		final List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user", strUsername));
                params.add(new BasicNameValuePair("pass", passwordHash));
                
                AsyncTask loader = new AsyncTask<Map, Void, JSONArray>() {
        	        @Override
        	        protected JSONArray doInBackground(Map ...map) {
        	        	String url = "http://picspot.weislogel.net/user.php?type=getUser&name="+strUsername+"&pass="+passwordHash;
        	        	
        	        	JSONObject jsonResult = JSONfunctions.getJSONfromURL(url);
        	        	JSONArray data = new JSONArray();
        	        	try {
        	        		data = jsonResult.getJSONArray("user");
						} catch (JSONException e) {
							e.printStackTrace();
						}
        	            return data;
        	        }
        	    }.execute();
        	    
        	    String serverUsername = "";
	        	String serverPassword = "";
        	    
        	    JSONArray data;
        	    try {
    				data = (JSONArray) loader.get();
    				if(data != null) {
            		    for(int i = 0 ; i < data.length() ; i++) {
            		    	JSONObject obj = data.getJSONObject(i);
            		    	serverUsername = obj.getString("u_username");
            		    	serverPassword = obj.getString("u_pass");
            		    	
            		    	String serverFirstname = obj.getString("u_firstname");
            		    	String serverLastname = obj.getString("u_lastname");
            		    	String serverEmail = obj.getString("u_email");
            		    	int serverId = (int) obj.getInt("u_id");
            		    	
            		    	SharedPreferences sharedpreferences = getActivity().getSharedPreferences("userdetails", getActivity().MODE_PRIVATE);
            		    	SharedPreferences.Editor prefsEditor;
            		    	Editor edit = sharedpreferences.edit();
            		    	
            		    	edit.clear();
            		    	edit.putString("username", serverUsername);
            		    	edit.putString("pass", serverPassword);
            		    	edit.putString("firstname", serverFirstname);
            		    	edit.putString("lastname", serverLastname);
            		    	edit.putString("email", serverEmail);
            		    	edit.putInt("id", serverId);
            		    	edit.commit();
            		    	
            		    	User user = new User(serverId, serverFirstname, serverLastname, serverEmail, serverPassword, serverUsername); 
            		    	((MainActivity) getActivity()).setUser(user);
            		    }
            		}
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			} catch (ExecutionException e) {
    				e.printStackTrace();
    			} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	    
        	    if(strUsername.equals(serverUsername) && passwordHash.equals(serverPassword)){
    		        Toast.makeText(getActivity(), "Redirecting...", 
    		        Toast.LENGTH_SHORT).show();
    		        
    		        Intent intent = new Intent(getActivity(), MainActivity.class);
    		        startActivity(intent);
    		        
    		        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    	    	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	    	    MainScreenFragment fragment = new MainScreenFragment();
    	    	    
    	    	    fragmentTransaction.addToBackStack(null);
    	    	    fragmentTransaction.replace(R.id.container, fragment);
    	    	    fragmentTransaction.commit();*/
    	        } else {
    		        Toast.makeText(getActivity(), "Falsche Benutzerdaten!",
    		        Toast.LENGTH_SHORT).show();
    	        }
        	}
        });
        return detailView;
    }
   
}
	
