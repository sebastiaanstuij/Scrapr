package com.sebastiaanstuij.scrapr;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	Context context;
	Screenshot[] rowItems;

	
	CustomAdapter(Context context, Screenshot[] rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}

	@Override
	public int getCount() {
		return rowItems.length;
	}

	@Override
	public Screenshot getItem(int position) {
		return (Screenshot) rowItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	 /* private view holder class */
	private class ViewHolder {
		ImageView screenshot_pic;
		TextView website_description;
		TextView date_added;
		TextView product_description;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_list_item, null);
			holder = new ViewHolder();

			holder.website_description = (TextView) convertView
					.findViewById(R.id.websiteTitle);
			holder.screenshot_pic = (ImageView) convertView
					.findViewById(R.id.list_image);
			holder.date_added = (TextView) convertView
					.findViewById(R.id.dateAdded);
			holder.product_description = (TextView) convertView
					.findViewById(R.id.productSubtitle);

			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Screenshot row_pos = rowItems[position];
		holder.screenshot_pic.setImageURI(Uri.parse(row_pos.iconFilePath));
		holder.website_description.setText(row_pos.url);
		holder.date_added.setText("test");
		holder.product_description.setText("test");


		return convertView;
	}

}
