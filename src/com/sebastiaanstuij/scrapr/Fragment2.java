package com.sebastiaanstuij.scrapr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class Fragment2 extends Fragment {

	private String currentURL = "http://slashdot.org/";
	WebView mWebView;

	public void init(String url) {
		currentURL = "http://slashdot.org/";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v = inflater.inflate(R.layout.fragment2, container, false);
        
        
        EditText address = (EditText) v.findViewById(R.id.address);
        address.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                mWebView.loadUrl("http://" + v.getText().toString());

                return true;
            }
        });
        
        Button button = (Button) v.findViewById(R.id.btnScreenshot);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                    // Bekijk hoe groot de kaart is
                    int height = mWebView.getHeight();
                    int width = mWebView.getWidth();

                    // Creeer een witte afbeelding (canvas) waar opgetekend kan worden
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    
                    // Draw the webview content to the canvas
                    mWebView.draw(canvas);
                    
                    // Determine filepath
                    String filePath = Environment.getExternalStorageDirectory() + "/Scrapr.png";
                    
                    // Create new File
                    File file = new File(filePath);

                    // And try to compress and write it 
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        }});

        if (currentURL != null) {
            Log.d("SwA", "Current URL  1[" + currentURL + "]");
            
            //setup webview, zoom options and load specified URL
            mWebView = (WebView) v.findViewById(R.id.webPage);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new SwAWebClient());
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);
            mWebView.loadUrl(currentURL);
        }
        return v;
    }

	
	public void updateUrl(String url) {
		Log.d("SwA", "Update URL [" + url + "] - View [" + getView() + "]");
		currentURL = url;

		WebView wv = (WebView) getView().findViewById(R.id.webPage);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(url);

	}

	// called from activity onbackpressed
	public boolean onBackPressed() {
		// return true als je daadwerkelijk een webpagina terug bent gegaan.
		// Als je op de eerste pagina zit, zou je bv. willen dat je de app
		// afsluit met de terugknop
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		} else {
			return false;
		}
	}

	private class SwAWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

	}

}