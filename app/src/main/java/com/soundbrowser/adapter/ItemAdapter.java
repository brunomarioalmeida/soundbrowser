package com.soundbrowser.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundbrowser.R;

public class ItemAdapter extends ArrayAdapter<ItemRow> {

	List<ItemRow> data;
	Context context;
	int layoutResID;

	public ItemAdapter(Context context, int layoutResourceId, List<ItemRow> data) 
	{
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent)
	{
		NewsHolder holder = null;
		View row = convertView;

		if (row == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new NewsHolder();

			holder.itemName = (TextView)  row.findViewById(R.id.example_itemname);
			holder.icon     = (ImageView) row.findViewById(R.id.example_image);
			holder.button1  = (Button)    row.findViewById(R.id.swipe_button1);
			holder.button2  = (Button)    row.findViewById(R.id.swipe_button2);
			holder.button3  = (Button)    row.findViewById(R.id.swipe_button3);
			row.setTag(holder);
		} else 
			holder = (NewsHolder) row.getTag();

		ItemRow itemdata = data.get(position);

		holder.itemName.setText(itemdata.getItemName());
		holder.icon.setImageDrawable(itemdata.getIcon());

		return row;
	}
	
    public List<ItemRow> getData() {
        return data;
    }
    public void setData(List<ItemRow> data) {
        this.data = data;
    }

	static class NewsHolder {
		TextView itemName;
		ImageView icon;
		Button button1;
		Button button2;
		Button button3;
	}
}
