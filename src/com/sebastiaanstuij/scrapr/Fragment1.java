package com.sebastiaanstuij.scrapr;

import java.util.ArrayList;
import java.util.List;

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
	TypedArray screen_pics;
	String[] product_names = {"Product A", "Product B"};
	String[] dates_added = {"01-04-2014", "01-05-2014"};
	
	List<RowItem> rowItems;
	ListView mylistView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1, null);	
		
		//update websites_names 
		//update screen_pics;
		//update product_names;
		//update dates_added;
				
		
		
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < website_names.length; i++) {
			RowItem item = new RowItem(website_names[i],
					screen_pics.getResourceId(i, -1), dates_added[i],
					product_names[i]);
			rowItems.add(item);
		}
		
		mylistView = (ListView) view.findViewById(R.id.list);
		CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), rowItems);
		mylistView.setAdapter(adapter);
		screen_pics.recycle();
		mylistView.setOnItemClickListener(this);
			
		return view;		 
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub

		String member_name = rowItems.get(position).getMember_name();
		Toast.makeText(getActivity().getApplicationContext(), "" + member_name,
				Toast.LENGTH_SHORT).show();
	}


}
		



