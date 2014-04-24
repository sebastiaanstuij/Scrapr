package com.sebastiaanstuij.scrapr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ZoomButtonsController;


public class Fragment2 extends Fragment {

	private String currentURL = "http://slashdot.org/";
	private WebView mWebView;
	private SelectionView selectionView;


	public void init(String url) {
		currentURL = "http://slashdot.org/";
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
                    Bitmap bitmap = getBitmapForVisibleRegion(mWebView);    
                    Rect rect = selectionView.getSelection();
                    
                    bitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, (rect.width()), rect.height());
                    
                    // Determine filepath
                    String filePath = Environment.getExternalStorageDirectory() + "/Scrapr.png";
                    
                    // Create new File
                    File file = new File(filePath);

                    // And try to compress and write it 
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
	                mode.finish(); // Action picked, so close the CAB
	                return true;
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
