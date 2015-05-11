package com.soundbrowser.adapter;

import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soundbrowser.EntryPoint;
import com.soundbrowser.R;
import com.soundbrowser.persistence.model.Item;

/**
 * Created by balmeida on 16-11-2014.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private List<Item> data;
    public  Resources res;
    private static LayoutInflater inflater = null;

    Item itemIn;
    int i = 0;

    public CustomAdapter(Activity a, List<Item> d,Resources resLocal) {
        activity = a;
        data = d;
        res = resLocal;

        // Layout inflator to call external xml layout ()
        inflater = (LayoutInflater) activity.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if(data == null || data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // Create a holder Class to contain inflated xml file elements
    public static class ViewHolder {
        public TextView desc;
        public TextView duration;
    }

    // Depends upon data size called for each row
    // Create each ListView row
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView == null)
        {
            // Inflate tabitem.xml file for each row ( Defined below )
            vi = inflater.inflate(R.layout.tabitem, null);

            // View Holder Object to contain tabitem.xml file elements
            holder = new ViewHolder();
            holder.desc = (TextView) vi.findViewById(R.id.desc);
            holder.duration = (TextView)vi.findViewById(R.id.duration);
            //holder.image=(ImageView)vi.findViewById(R.id.image);

            //Set holder with LayoutInflater
            vi.setTag( holder );
        }
        else
            holder = (ViewHolder) vi.getTag();

        if(data == null || data.size()<=0)
            holder.desc.setText("No Data");
        else
        {
            // Get each Model object from Arraylist
            itemIn = (Item) data.get(position);

            if(itemIn == null)
                return null;

            // Set Model values in Holder elements
            holder.desc.setText(
            	itemIn.getTitle() != null ? 
            	itemIn.getTitle() : 
            	itemIn.getItemLst().get(0).getTitle()
            );
            // TODO Some cleaning, but this should be done somewhere else
            if(Pattern.compile("\\d{4}-\\d{2}-\\d{2}.*").matcher(holder.desc.getText()).matches())
            	holder.desc.setText(((String)holder.desc.getText()).substring(10).trim());
            
            if(itemIn.getTrack() != null)
                holder.duration.setText(itemIn.getTrack().getDuration());

            // Set Item Click Listener for LayoutInflater for each row
            vi.setOnClickListener(new OnItemClickListener( position ));
        }

        return vi;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0)
        {
            EntryPoint sct = (EntryPoint)activity;
            sct.onItemClick(mPosition);
        }
    }
}