package com.sebastiaanstuij.scrapr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ZoomButtonsController;


public class Fragment2 extends Fragment {

	private String currentURL = "http://www.google.com";
	private WebView mWebView;
	private SelectionView selectionView;
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Scrapr/";
    public static final String lang = "eng";
	public static final String OCR_DATA_PATH = DATA_PATH + "tessdata/" + lang + ".traineddata";
			
	public void init(String url) {
		currentURL = "http://www.google.com";
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment2, container, false);
        selectionView = (SelectionView)v.findViewById(R.id.selectionView1);
        selectionView.setVisibility(View.INVISIBLE);
        
        EditText address = (EditText) v.findViewById(R.id.address);
        address.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                mWebView.loadUrl("http://" + v.getText().toString());
                
                return true;
            }
        });
               
        
        Button buttonSelectionArea = (Button) v.findViewById(R.id.btnSelectionArea);
        buttonSelectionArea.setOnClickListener(new OnClickListener() {         
            @Override
            public void onClick(View v) {
            	((ActionBarActivity) getActivity()).startSupportActionMode(mActionModeCallback);            	 
            	selectionView.setVisibility(View.VISIBLE);                                 
        }});
         
        
        
        if (currentURL != null) {
            Log.d("SwA", "Current URL  1[" + currentURL + "]");
            
            //setup webview, zoom options and load specified URL
            mWebView = (WebView) v.findViewById(R.id.webPage);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new SwAWebClient());
            mWebView.getSettings().setBuiltInZoomControls(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                // Use the API 11+ calls to disable the controls
                // Use a seperate class to obtain 1.6 compatibility
                new Runnable() {
                  @SuppressLint("NewApi")
				public void run() {
                    mWebView.getSettings().setDisplayZoomControls(false);
                  }
                }.run();
              } else {
                ZoomButtonsController zoom_controll;
				try {
					zoom_controll = (ZoomButtonsController) mWebView.getClass().getMethod("getZoomButtonsController").invoke(mWebView, null);
					zoom_controll.getContainer().setVisibility(View.GONE);
				} catch (Exception e) {
					e.printStackTrace();
				}
                
              }
            
            
            //mWebView.getSettings().setDisplayZoomControls(false);
            mWebView.loadUrl(currentURL);
        }
        return v;
    }

	
	@SuppressLint("SetJavaScriptEnabled")
	public void updateUrl(String url) {
		Log.d("SwA", "Update URL [" + url + "] - View [" + getView() + "]");
		currentURL = url;

		WebView wv = (WebView) getView().findViewById(R.id.webPage);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(url);

	}

	// called from activity onbackpressed
	@SuppressLint("SetJavaScriptEnabled")
	public boolean onBackPressed() {
		// return true if you have really gone back to a previous webpage
		// When you have arived at the first visited webpage you would probably want to go back to the 
		// previous navigation option of the app by pressing the back button
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		} else {
			return false;
		}
	}
	
	public static Bitmap getBitmapForVisibleRegion(WebView webview) {
	    Bitmap returnedBitmap = null;
	    webview.setDrawingCacheEnabled(true);
	    returnedBitmap = Bitmap.createBitmap(webview.getDrawingCache());
	    webview.setDrawingCacheEnabled(false);
	    return returnedBitmap;
	}
	

	private class SwAWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.context_menu, menu);
	        
	        return true;
	    }

	    // Called each time the action mode is shown. Always called after onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }

	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {	
	        switch (item.getItemId()) {
	            case R.id.action_selection:
	            	// Create bitmap and call visibleRegion method
                    Bitmap screenshotBitmap = getBitmapForVisibleRegion(mWebView);         
                    Rect rect = selectionView.getSelection();
                    
                    screenshotBitmap = Bitmap.createBitmap(screenshotBitmap, rect.left, rect.top, (rect.width()), rect.height());
     
                    Bitmap iconBitmap =  mWebView.getFavicon();
                    if (iconBitmap == null){
                    	iconBitmap = screenshotBitmap;
                    }
                    
                    
                    String curDate = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
					String screenshotFilePath = DATA_PATH + "screenshot_" + curDate + ".png";
					String iconFilePath = DATA_PATH + "icon_" + curDate + ".png";

					
                    File screenshotFile = new File(screenshotFilePath);
                    File iconFile = new File(iconFilePath);
                    
                    
                    TessBaseAPI baseApi = new TessBaseAPI();
                    baseApi.init(DATA_PATH, "eng");
                    baseApi.setImage(screenshotBitmap);
                    String recognizedText = baseApi.getUTF8Text();
                    baseApi.end();
                    
                    try { 
                    	screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(screenshotFile));                      
                    	iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(iconFile));                      
                    	
                        // Add screenshot properties to sharedPreferences so that it can be accessed in MainActivity
                        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        
                    	int size = settings.getInt("array_size", 0); // Get size of Sceenshot Array, or create new size preference 
                        editor.putString("screenshotFilePath_" + size, screenshotFilePath);
                        editor.putString("iconFilePath_" + size, iconFilePath);
                        editor.putString("recognizedText_" + size, recognizedText);
                        editor.putString("url_" + size, mWebView.getUrl());
                        editor.putInt("x_cor_" + size, (int) mWebView.getLeft());
                        editor.putInt("y_cor_" + size, (int) mWebView.getTop());
                        editor.putInt("zoom_lvl_" + size, (int) mWebView.getScale());
                        editor.putInt("array_size", size+1);                        
                        
                       
                        // Commit the edits!
                        editor.commit();
               
                        // Action picked, so close the CAB and return true
    	                mode.finish(); 
    	                return true;
                    	
                    } 
                    
                    catch (IOException e) { 
                    	//TODO opslaan ging fout
                    	e.printStackTrace();                  	
                    }
   
	                
	            default:
	                return false;
	        }
	    }
	    

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	        //mActionMode = null;
	    }
	};
	
	
	
}
