package com.sebastiaanstuij.scrapr;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	Context context;
	List<RowItem> rowItems;

	
	CustomAdapter(Context context, List<RowItem> rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
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

			RowItem row_pos = rowItems.get(position);

			holder.screenshot_pic.setImageResource(row_pos.getProfile_pic_id());
			holder.website_description.setText(row_pos.getMember_name());
			holder.date_added.setText(row_pos.getStatus());
			holder.product_description.setText(row_pos.getContactType());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

}
