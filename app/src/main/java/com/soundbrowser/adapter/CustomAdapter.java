package com.soundbrowser.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soundbrowser.EntryActivity;
import com.soundbrowser.R;
import com.soundbrowser.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bold on 16-11-2014.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private List data;
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
            holder.desc.setText(itemIn.getTitle());
            if(itemIn.getTrack() != null)
                holder.duration.setText(itemIn.getTrack().getDuration());

            // Set Item Click Listener for LayoutInflater for each row
            vi.setOnClickListener(new OnItemClickListener( position ));
        }

        return vi;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
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
            EntryActivity sct = (EntryActivity)activity;
            sct.onItemClick(mPosition);
        }
    }
}