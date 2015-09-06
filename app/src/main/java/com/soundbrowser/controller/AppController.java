package com.soundbrowser.controller;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
 
public class AppController extends Application {
 
    public static final String TAG = AppController.class.getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
 
    private static AppController mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() 
    {
        if (mRequestQueue == null) 
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mRequestQueue = newRequestQueue(getApplicationContext());
 
        return mRequestQueue;
    }
    
   // Default maximum disk usage in bytes
    private static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;

    // Default cache folder name
    private static final String DEFAULT_CACHE_DIR = "volley";
    
    // Most code copied from "Volley.newRequestQueue(..)", we only changed cache directory
    private RequestQueue newRequestQueue(Context context) 
    {
        // define cache folder
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) 
        {
            Log.w(
            	"soundbrowser", 
            	"Can't find External Cache Dir, switching to application specific cache directory"
            );
            rootCache = context.getCacheDir();
        }

        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();

        RequestQueue queue = new RequestQueue(
        	new DiskBasedCache(
            	cacheDir, DEFAULT_DISK_USAGE_BYTES
            ), 
            this.new CustomNetwork(new HurlStack())
        );
        queue.start();

        return queue;
    }    
    
    class CustomNetwork extends BasicNetwork 
    {
		public CustomNetwork(HttpStack httpStack) {
			super(httpStack);
		}
    	
		@Override
		public NetworkResponse performRequest(Request<?> request)
		  throws VolleyError 
		{
//			if(true)
//			  return super.performRequest(request);
			
			NetworkInfo activeNetwork = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

//			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//			if (!ConnectivityUtils.isNetworkEnabled(AppController.getInstance().getApplicationContext()) && request instanceof ImageRequest) {
//			if (false && tm .getDataState() != tm .DATA_CONNECTED && request instanceof ImageRequest) 
			if (!isConnected && request instanceof ImageRequest) 
			{
				Map<String, String> responseHeaders = Collections.emptyMap();
                VolleyLog.e("Cached response", "No Network Connectivity for Url=", request.getUrl());

                return new NetworkResponse(
                	HttpStatus.SC_NOT_MODIFIED, request.getCacheEntry().data, 
                    responseHeaders, true
                );
            }
			return super.performRequest(request);
		}
    }
 
    public ImageLoader getImageLoader() 
    {
        getRequestQueue();
        if (mImageLoader == null) 
            mImageLoader = new ImageLoader(
            	this.mRequestQueue, new LruBitmapCache()
            );
        
        return this.mImageLoader;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) 
            mRequestQueue.cancelAll(tag);
    }
}