package com.sebastiaanstuij.scrapr;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
    
    private Fragment2 mFragment2 = null;
    public static final String PREFS_NAME = "MyPrefsFile";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuTitles = getResources().getStringArray(R.array.menuTitles);

        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
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
                R.drawable.appthemepurple_ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
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
        
        mDrawer.setDrawerListener(mDrawerToggle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// Whenever invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
    
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
    
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    // Create a new fragment
		Fragment fragment;
        
		// Get fragmentManager
	    FragmentManager fragmentManager = getSupportFragmentManager();

	    mFragment2 = null;
        switch (position)
        {    
            case 0:
                fragment = new Fragment1();
                // Insert fragment1 by replacing any existing fragment
        	    fragmentManager.beginTransaction()
        	                   .replace(R.id.content_frame, fragment, "fragment1")
        	                   .commit();
                break;
            case 1:
            	
                mFragment2 = new Fragment2();
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
                fragment = new Fragment1();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    @Override
    public void onBackPressed()
    {
    	// Kijk of mFragment2 een object bevat. Zo ja, dan is hij in beeld. In selectItem wordt namelijk mFragment2
    	if (mFragment2 != null) {
    		// Als de functie waar is, beeindig de huidige functie, zodat super.onBackPressed(); niet wordt uitgevoerd
    		// Als je deze wel uitvoert, heb je ook nog dat je een scherm terug gaat en dat wil je niet
    		if (mFragment2.onBackPressed()) {
    			return;
    		}
    	}
    	
        super.onBackPressed();
    }
	
	
}
