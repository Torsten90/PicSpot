package com.example.picspot;

import java.io.Console;

import com.example.picspot.Objects.Pic;
import com.example.picspot.Objects.User;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import android.provider.Settings;

public class MainActivity extends ActionBarActivity {

	private boolean hasRegistered = false;
	private User user = null;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Pic lastPic = new Pic();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		final MainActivity activity = this;

		new android.os.Handler().postDelayed(new Runnable() {
			public void run() {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				LoginFragment fragment = new LoginFragment();

				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.replace(R.id.container, fragment);
				fragmentTransaction.commit();
			}
		}, 1000);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image taken", Toast.LENGTH_LONG).show();

				Pic pic = new Pic();

				LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				boolean enabled = locManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				if (!enabled) {
					Intent intentLoc = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intentLoc);
				}

				Location currentLoc = locManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (currentLoc == null)
					currentLoc = locManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				lastPic.setLat(currentLoc.getLatitude());
				lastPic.setLng(currentLoc.getLongitude());

				Log.i("test", pic.toString());
			}
		} else if (resultCode == RESULT_CANCELED) {
			// User cancelled the image capture
		} else {
			// Image capture failed, advise user
		}
	}

	public boolean getHasRegistered() {
		return this.hasRegistered;
	}

	public void setHasRegistered(boolean registered) {
		this.hasRegistered = registered;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pic getLastPic() {
		return this.lastPic;
	}

	public void setLastPicPath(String pPath) {
		this.lastPic.setLocalPath(pPath);
	}

	public void setLastPicName(String pName) {
		this.lastPic.setName(pName);
	}
}
