package com.sebastiaanstuij.scrapr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// this Activity extends ListActivity so it indicates that it contains a listview object in its layout
// and can be populated with data with the setListAdapter() method 
public class MainActivity extends ActionBarActivity {
	
	private ActionBar bar;
	private ListView mDrawerList;
	private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

	private String[] menuTitles;
	private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private WebviewFragment mFragment2 = null;
    public static final String PREFS_NAME = "MyPrefsFile";
	
    public String DATA_PATH_SCRAPR;
    public String DATA_PATH_TESS;

    public static final String lang = "eng";
	private static final String TAG = "MainActivity.java";
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// First set the content view of the main activity
		setContentView(R.layout.activity_main);
		
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				DATA_PATH_SCRAPR = Environment.getExternalStorageDirectory().toString() + "/Scrapr/";
				DATA_PATH_TESS = Environment.getExternalStorageDirectory().toString() + "/Scrapr/tessdata/";
			} else{
				DATA_PATH_SCRAPR = getDataDir(getApplicationContext()) + "/Scrapr/";
				DATA_PATH_TESS = getDataDir(getApplicationContext()) + "/Scrapr/tessdata";
			}		
			
	        // Create Scrapr folder if it does not exist and create new  filepath and File
	        File folderScrapr = new File(DATA_PATH_SCRAPR);
	    	folderScrapr.mkdir();
	    	
	    	// Create tessdata folder within Scrapr folder which holds the TESS OCR trained data
	        File folderTess = new File(DATA_PATH_TESS);
	    	folderTess.mkdir();
					    	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
 	    	
    	// Copy the contents of the traineddata OCR file from the assets folder to a local directory on the phone
        if (!(new File(DATA_PATH_TESS + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH_TESS + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();

				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
    		
    	// Set the navigation drawer properties
		mTitle = mDrawerTitle = getTitle();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuTitles = getResources().getStringArray(R.array.menuTitles);
        
        // Set a custom shadow that overlays the main content when the drawer opens
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // Enable ActionBar app icon to behave as action to toggle nav drawer
        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        
        // Set the adapter for the menu drawer list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_list_item, menuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
            	selectItem(position);
            }
        });
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawer,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	getSupportActionBar().setTitle(mTitle);    	
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        // Attacht the toggle to the drawer
        mDrawer.setDrawerListener(mDrawerToggle);   
        
        // Start the first 'home' fragment when the app is started
        selectItem(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// This method is called whenever invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        
        // Set the visibility of menu items when drawer is open or closed
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    //-----------------Helper methods-----------------
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
       /* // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }
    
	// Swaps fragments in the main content view
	private void selectItem(int position) {
	    // Create a new fragment
		Fragment fragment;
        
		// Get fragmentManager
	    FragmentManager fragmentManager = getSupportFragmentManager();

	    mFragment2 = null;
        switch (position)
        {    
            case 0:
                fragment = new HomeScreenFragment();
                // Insert fragment1 by replacing any existing fragment
        	    fragmentManager.beginTransaction()
        	                   .replace(R.id.content_frame, fragment, "fragment1")
        	                   .commit();
                break;
            case 1:
            	
                mFragment2 = new WebviewFragment();
                // Insert fragment2 by replacing any existing fragment
        	    fragmentManager.beginTransaction()
        	                   .replace(R.id.content_frame, mFragment2, "fragment2")
        	                   .commit();
                break;
            case 2:
                fragment = new Fragment3();
                // Insert fragment3 by replacing any existing fragment
        	    fragmentManager.beginTransaction()
        	                   .replace(R.id.content_frame, fragment, "fragment3")
        	                   .commit();
                break;
            default:
                fragment = new HomeScreenFragment();
                // Insert fragment1 by replacing any existing fragment
        	    fragmentManager.beginTransaction()
        	                   .replace(R.id.content_frame, fragment, "fragment1")
        	                   .commit();
        }
	  
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(menuTitles[position]);
	    mDrawer.closeDrawer(mDrawerList);
	}
	
	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    bar.setTitle(mTitle);
	}
    
    @Override
    public void onBackPressed()
    {
    	// Check whether mFragment2 contains an object. If that is the case, it is currently in view.
    	if (mFragment2 != null) {
    		// If the method returns true, end the current method so that super.onBackPressed() is not called.
    		// otherwise the application will go back another screen, which is not what the user wants
    		if (mFragment2.onBackPressed()) {
    			return;
    		}
    	}
    	
        super.onBackPressed();
    }
	
   
    
    // gets this application's datadir when external storage is not available
    public String getDataDir(Context context) throws NameNotFoundException
    {
        return context.getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir;
    }
}
