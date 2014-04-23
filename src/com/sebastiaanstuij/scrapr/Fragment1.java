package com.sebastiaanstuij.scrapr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class Fragment1 extends Fragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1, null);
		
		ListView listView = (ListView) view.findViewById(R.id.list);
				
		
		return view;
		
		 
	}

	
	 private BaseAdapter mAdapter = new BaseAdapter() {  
		  
	        @Override  
	        public int getCount() {  
				if (mContacts != null)
					return mContacts.length;  
				else
					return 0;
	        }  
	  
	        @Override  
	        public Object getItem(int position) {  
	            return mContacts[position];  
	        }  
	  
	        @Override  
	        public long getItemId(int position) {  
	            return 0; 
	        }
			
			class ViewHolder {
				ImageView image;
				TextView txtName;
			}  
			
	        @Override  
	        public View getView(int position, View convertView, ViewGroup parent) {  
		    	ViewHolder viewHolder;
		    	
		    	if (convertView == null) {
		    	    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	    convertView = inflater.inflate(R.layout.list_item_single, null);
		    	    
		    	    viewHolder  = new ViewHolder();
		    	    
					viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
					viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
		    	    
		    	    convertView.setTag(viewHolder);
		    	} else {
		    		viewHolder = (ViewHolder) convertView.getTag();
		    	}
	        	
		    	//mObjectFetcher.loadBitmap("Avatar/" + mContacts[position].getId() + "/" + Integer.toString(92), viewHolder.image);
				viewHolder.txtName.setText(mContacts[position].getName());
				
	            return convertView;  
	        }  
	          
	    };
	}
		
}


