package com.soundbrowser.receivers;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    private long enqueue;
    private DownloadManager dm;
    
    public DownloadBroadcastReceiver(DownloadManager dm){
    	this.dm = dm;
    }

    @Override
    public void onReceive(Context context, Intent intent) 
    {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) 
        {
//            long downloadId = intent.getLongExtra(
//            	DownloadManager.EXTRA_DOWNLOAD_ID, 0
//            );
            Query query = new Query();
            query.setFilterById(enqueue);
            Cursor c = dm.query(query);
            if (c.moveToFirst()) 
            {
                int columnIndex = c.getColumnIndex(
                	DownloadManager.COLUMN_STATUS
                );
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) 
                {
//                    ImageView view = (ImageView) findViewById(R.id.imageView1);
                    String uriString = c.getString(
                    	c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                    );
//                    view.setImageURI(Uri.parse(uriString));
//                    Toast.makeText(
//                        getApplicationContext(), uriString, Toast.LENGTH_LONG
////                        getApplicationContext(), dm.getUriForDownloadedFile(1).getPath(), Toast.LENGTH_LONG
//                    ).show();
                }
            }
        }
    }
    
    public void downloadFile(String url) 
    {
    	File f = new File(
    		Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + 
    		Environment.DIRECTORY_DOWNLOADS + "/" + 
    		url.substring(url.indexOf("podcasts"))
    	);
        Log.i("soundbrowser", f.getAbsolutePath());
        
    	if(!f.exists())
    	{
        	Request r = new Request(Uri.parse(url));
        	
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	    r.allowScanningByMediaScanner();
        	    r.setNotificationVisibility(
        	    	DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        	    );
        	}
            
            /*
            //File mydownload = new File (
            File mydownload	= Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
            ); // + "/myFolder"
            //);
            Log.i("soundbrowser", mydownload.getAbsolutePath());
            */
//        	r.setDestinationInExternalFilesDir(context, dirType, subPath)
            
            r.setDestinationInExternalPublicDir(
            	Environment.DIRECTORY_DOWNLOADS,
            	url.substring(url.indexOf("podcasts"))
        	);

            enqueue = dm.enqueue(r);
    	}
    }
}
