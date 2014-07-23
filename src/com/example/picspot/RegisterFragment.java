package com.example.picspot;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.widget.Toast;



public class RegisterFragment extends Fragment{
	
	private EditText  etFirstname=null;
	private EditText  etLastname=null;
	private EditText  etEmail=null;
	private EditText  etPass=null;
	private EditText  etUsername=null;
	private EditText  etPassRep=null;
	private Button register;
	
    public RegisterFragment() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_register, container, false);
        
        etFirstname = (EditText)detailView.findViewById(R.id.eTFirstname);
        etLastname 	= (EditText)detailView.findViewById(R.id.eTLastname);
        etUsername 	= (EditText)detailView.findViewById(R.id.eTUsername);
        etEmail 	= (EditText)detailView.findViewById(R.id.eTEmail);
        etPass 		= (EditText)detailView.findViewById(R.id.eTPass);
        etPassRep 	= (EditText)detailView.findViewById(R.id.eTPassrep);
        
        register = (Button)detailView.findViewById(R.id.btnRegister);
        
        register.setOnClickListener(new Button.OnClickListener(){
        	@Override
			public void onClick(View view){
        		 boolean invalid = false; 
        		 
        		 String etFirstnameStr 	= etFirstname.getText().toString();
        		 String etLastnameStr 	= etLastname.getText().toString();
        		 String etUsernameStr 	= etUsername.getText().toString();
        		 String etEmailStr 		= etEmail.getText().toString();
        		 String etPassStr 		= etPass.getText().toString();
        		 String etPassRepStr 	= etPassRep.getText().toString();
        		 
        		 String errorMsg = "";
        		 if(etFirstnameStr.equals("")){
        		    invalid = true;
        		    errorMsg += "Geben Sie einen Vornamen ein \n";
        		 }
        		   
        		 if(etLastnameStr.equals("")){
        		    invalid = true;
        		    errorMsg += "Geben Sie einen Nachnamen ein \n";
        		 }
        		         
        		 if(etUsernameStr.equals("")){
        		     invalid = true;
        		     errorMsg += "Geben Sie einen Benutzernamen ein \n";
        		 }
        		    
        		 if(etPassStr.equals("")){
        		      invalid = true;
        		      errorMsg += "Geben Sie einen Passwort ein \n";
        		 } else if (!etPassStr.equals(etPassRepStr)){
        			 invalid = true;
        			 errorMsg += "Die Passwörter sind nicht identisch \n"; 
        		 } else if (etPassStr.length()<=4 || etPassRepStr.length()<=4){
        			 invalid = true;
        			 errorMsg += "Das Passwort muss mindesten 5 Zeichen haben \n"; 
        		 }
        		 
        		 if(etEmailStr.equals("")){
        		    invalid = true;
        		    errorMsg += "Geben Sie einen E-Mail Adresse ein \n";
        		 }
        		 
        		 if(invalid){
        			 Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        		 } else {
        		
        		 String passwortHash = Helper.md5(etPassStr);	 
        			 
        		 String params = String.format("?type=insert&fields[u_firstname]=%s&fields[u_lastname]=%s&fields[u_email]=%s&fields[u_username]=%s&fields[u_pass]=%s", 
        				 etFirstnameStr, etLastnameStr, etEmailStr, etUsernameStr, passwortHash);	 
    			 
    			 AsyncTask loader = new AsyncTask<String, Void, Boolean>() {
         	        @Override
         	        protected Boolean doInBackground(String ...fields) {
         	        	
         	        	String url = "http://picspot.weislogel.net/user.php"+fields[0];
         	        	Log.i("url", url);
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
			       		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			    	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    	    LoginFragment fragment = new LoginFragment();
			    	    
			    	    ((MainActivity) getActivity()).setHasRegistered(true);
			    	    
			    	    fragmentTransaction.addToBackStack(null);
			    	    fragmentTransaction.replace(R.id.container, fragment);
			    	    fragmentTransaction.commit();
		       	   }
         	    
         	    
        		 }
        		 
        		 
        		
        	}
        });
        
        return detailView;
    }
   
}
	
