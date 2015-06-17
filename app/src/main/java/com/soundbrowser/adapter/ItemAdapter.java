package com.soundbrowser.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.soundbrowser.R;
import com.soundbrowser.controller.AppController;
import com.soundbrowser.controller.UserAvatar;
import com.soundbrowser.persistence.model.Item;

public class ItemAdapter extends ArrayAdapter<Item> {

    List<Item> data;
	Context context;
	int layoutResID;
    
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ItemAdapter(Context context, int layoutResourceId, List<Item> data) 
	{
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent)
	{
		final NewsHolder holder;
		View row = convertView;

		if (row == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new NewsHolder();

			holder.itemName = (TextView)  row.findViewById(R.id.example_itemname);
			holder.button1  = (Button)    row.findViewById(R.id.swipe_button1);
			holder.button2  = (Button)    row.findViewById(R.id.swipe_button2);
			holder.button3  = (Button)    row.findViewById(R.id.swipe_button3);
			holder.icon     = (UserAvatar) row.findViewById(R.id.imageThumbnail2);
//			holder.icon     = (CircledNetworkImageView) row.findViewById(R.id.imageThumbnail2);
			row.setTag(holder);
		} else 
			holder = (NewsHolder) row.getTag();

		Item itemdata = data.get(position);

		holder.itemName.setText(itemdata.getTitle());

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        // TODO optimize this in case all the images are the same for this list
       	holder.icon.setImageUrl(itemdata.getImage(), imageLoader);
//        holder.icon.setImageUrl(Uri.fromFile(new File(itemdata.getImage())).toString(), new MyCache());
//		holder.icon.setImageDrawable(itemdata.getIcon());
        
		return row;
	}
	
//	class MyCache implements ImageLoader.ImageCache {
//
//	    @Override
//	    public Bitmap getBitmap(String key) {
//	        if (key.contains("file://")) {
//	            return BitmapFactory.decodeFile(key.substring(key.indexOf("file://") + 7));
//	        } else {
//	            // Here you can add an actual cache
//	            return null;
//	        }
//	    }
//	    @Override
//	    public void putBitmap(String key, Bitmap bitmap) {
//	        // Here you can add an actual cache
//	    }
//	}
    
	public List<Item> getData() {
        return data;
    }
    public void setData(List<Item> data) {
        this.data = data;
    }

    static class NewsHolder {
		TextView itemName;
		UserAvatar icon;  //ImageView  NetworkImageView		RoundedCornerNetworkImageView
		Button button1;
		Button button2;
		Button button3;
	}
}

//if(!imageUrl.equalsIgnoreCase(itemdata.getImageUrl())) {
//imageUrl = itemdata.getImageUrl();
//ImageRequest request = new ImageRequest(
//	itemdata.getImageUrl(),
////			"http://img0.rtp.pt/EPG/radio/imagens/1070_7907_60189.png",
////	"http://api.androidhive.info/json/movies/2.jpg",
//    new Response.Listener<Bitmap>() {
//        @Override
//        public void onResponse(Bitmap bitmap) {
////            holder.icon.setImageBitmap(bitmap);
//            ItemAdapter.this.bitmap = bitmap;
//        }
//    }, 0, 0, null,
//    new Response.ErrorListener() {
//        public void onErrorResponse(VolleyError error) {
//            //mImageView.setImageResource(R.drawable.image_load_error);
//        }
//    }
//);
//AppController.getInstance().addToRequestQueue(request);
//}
//holder.icon.setImageBitmap(bitmap);
