package com.example.picspot;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.picspot.R;

import com.example.picspot.Objects.Pic;
import com.example.picspot.Objects.Spot;
import com.example.picspot.Objects.User;
import com.example.picspot.slidingmenu.CustomAdapter;
import com.example.picspot.slidingmenu.RowItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MainActivity extends ActionBarActivity {
	
	private boolean hasRegistered = false;
	private User user = null;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Pic lastPic = new Pic();
	
	private GoogleMap gMap;
	private Marker lastOpened = null;
	private Vector<Spot> spotVector = new Vector<Spot>();
	
	private ArrayList<Spot> Spots = new ArrayList<Spot>();
	
	String[] menutitles;
	TypedArray menuIcons;

	// nav drawer title
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinear;

	private List<RowItem> rowItems;
	private CustomAdapter adapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		menutitles = getResources().getStringArray(R.array.titles);
		menuIcons = getResources().obtainTypedArray(R.array.icons);
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
	    mDrawerList = (ListView) findViewById(R.id.slider_list);
		
		/*mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.slider_list);*/

		rowItems = new ArrayList<RowItem>();

		for (int i = 0; i < menutitles.length; i++) {
			RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(
					i, -1));
			rowItems.add(items);
		}

		menuIcons.recycle();

		adapter = new CustomAdapter(getApplicationContext(), rowItems);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new SlideitemListener());

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			updateDisplay(1);
		}
		
		SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE); 
		int userId = userDetails.getInt("id", 0);
		
		if(userId == 0 ){
			new android.os.Handler().postDelayed(new Runnable() {
				public void run() {
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					LoginFragment fragment = new LoginFragment();

					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.replace(R.id.drawer_layout, fragment);
					fragmentTransaction.commit();
				}
			}, 1000);
		} else {
			TextView userNameMenu = (TextView) findViewById(R.id.userNameMenu);
			String firstname = userDetails.getString("firstname", "");
			String lastname = userDetails.getString("lastname", "");
			String name = firstname + " " + lastname;
			userNameMenu.setText(name);
		}
		
	}

	class SlideitemListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			updateDisplay(position);
		}

	}

	private void updateDisplay(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new MainScreenFragment();
			break;
		case 1:
            fragment = new SpotListFragment();
            break;
        case 2:
            fragment = new ProfileFragment();
            break;
        case 3:
        	SharedPreferences settings = getSharedPreferences("userdetails", MODE_PRIVATE);
        	settings.edit().clear().commit();
        	fragment = new LoginFragment();
            break;
		default:
			break;
		}
 
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			// update selected item and title, then close the drawer
			setTitle(menutitles[position]);
			mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			mDrawerLayout.closeDrawer(mDrawerLinear);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
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
			    lastPic.setCreator(user.getId());
			    lastPic.ladeBitmap();
				
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

	public ArrayList<Spot> getSpots() {
		return Spots;
	}

	public void setSpots(ArrayList<Spot> spots) {
		Spots = spots;
	}
	

}