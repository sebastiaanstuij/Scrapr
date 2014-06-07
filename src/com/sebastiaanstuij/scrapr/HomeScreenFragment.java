package com.sebastiaanstuij.scrapr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class HomeScreenFragment extends Fragment implements OnItemClickListener {

	
	Screenshot[] screenshotArray;
	//List<RowItem> rowItems;
	ListView mylistView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1, null);	
		
		SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
		
        int size = settings.getInt("array_size", 0);
        screenshotArray = new Screenshot[size];
        
        for (int i=0; i<size; i++) {
        	screenshotArray[i] = new Screenshot();
        	screenshotArray[i].x = settings.getInt("x_" + i, 0);
        	screenshotArray[i].y = settings.getInt("y_" + i, 0);
        	screenshotArray[i].zoom = settings.getInt("zoom_" + i, 0);
        	screenshotArray[i].url = settings.getString("url_" + i, "");
        	screenshotArray[i].screenshotFilePath = settings.getString("screenshotFilePath_" + i, "NA");
        	screenshotArray[i].iconFilePath = settings.getString("iconFilePath_" + i, "NA");
        	screenshotArray[i].recognizedText = settings.getString("recognizedText_" + i, "NA");
        	
        }

		
		mylistView = (ListView) view.findViewById(R.id.list);
		CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), screenshotArray);
		mylistView.setAdapter(adapter);
		//screenshots.recycle();
		mylistView.setOnItemClickListener(this);
			
		return view;		 
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		//screen_pics = getResources().obtainTypedArray(R.array.screenshot_pics);
		super.onAttach(activity);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub

		String recognizedText = screenshotArray[position].recognizedText;
		String x_cor = String.valueOf(screenshotArray[position].x);
		String y_cor = String.valueOf(screenshotArray[position].y);
		Toast.makeText(getActivity().getApplicationContext(), x_cor + "-" + y_cor,
				Toast.LENGTH_SHORT).show();
	}


}
		



