package com.sebastiaanstuij.scrapr;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class Fragment1 extends Fragment implements OnItemClickListener {

	String[] website_names = {"Website A", "Website B"};
	//find a way to store images in array
	String[] product_names = {"Product A", "Product B"};
	String[] dates_added = {"01-04-2014", "01-05-2014"};
	
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
        	screenshotArray[i].filePath = settings.getString("filePath_" + i, "NA");
        }
		
		/*rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < size; i++) {
			RowItem item = new RowItem(website_names[i],
					screenshots.getResourceId(i, -1), dates_added[i],
					product_names[i]);
			rowItems.add(item);
		}*/
		
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

		String member_name = screenshotArray[position].url;
		Toast.makeText(getActivity().getApplicationContext(), "" + member_name,
				Toast.LENGTH_SHORT).show();
	}


}
		



