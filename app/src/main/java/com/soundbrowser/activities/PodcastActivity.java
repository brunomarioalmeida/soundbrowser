package com.soundbrowser.activities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.soundbrowser.R;
import com.soundbrowser.adapter.ItemAdapter;
import com.soundbrowser.adapter.ItemRow;
import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Track;
import com.soundbrowser.persistence.ormlite.DatabaseHelper;
import com.soundbrowser.receivers.DownloadBroadcastReceiver;
import com.soundbrowser.services.PodcastService;

public class PodcastActivity extends OrmLiteBaseActivity<DatabaseHelper> { 

	// Progress Dialog
    private ProgressDialog pDialog;
 
    List<ItemRow> itemData;
    int currentPosition;
    
    private MediaPlayer mediaPlayer;
    
    private PodcastActivity.VolumeBroadcastReceiver volumeBReceiver;
    private DownloadBroadcastReceiver downloadBReceiver;
    
    LoadItens loadItens;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_list);
         
        // Hashmap for ListView
        itemData = new ArrayList<ItemRow>();
  
        // Loading Itens in Background Thread
        loadItens = new LoadItens();
        loadItens.execute();
        
        downloadBReceiver = new DownloadBroadcastReceiver(
    		(DownloadManager) getSystemService(DOWNLOAD_SERVICE)
    	);
        registerReceiver(
        	downloadBReceiver, 
        	new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
        volumeBReceiver = new PodcastActivity.VolumeBroadcastReceiver();
        registerReceiver(
        	volumeBReceiver, 
        	new IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        );
    }
 
	public void clickPlayBtn(View view) {
		Toast.makeText(
			this, 
			"Going to play podcast ...", 
//			view.getClass().getCanonicalName() + " - " + currentPosition, 
			Toast.LENGTH_LONG
		).show();

		playLocalOrExternalAudio();
	}

	private void playLocalOrExternalAudio() 
	{
		String url = itemData.get(currentPosition).getUrl();
		String localUrl = 
        	Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + 
        	Environment.DIRECTORY_DOWNLOADS + "/" + 
        	url.substring(url.indexOf("wavrss"));
        File f = new File(localUrl);
		playAudio(f.exists()?localUrl:url);
//		playAudio(itemData.get(currentPosition).getUrl());
	}

	public void clickDownloadBtn(View view) {
		Toast.makeText(
			this, 
			"Going to download the podcast ...", 
			Toast.LENGTH_SHORT
		).show();

		downloadBReceiver.downloadFile(itemData.get(currentPosition).getUrl());
	}

	public void clickExtraBtn(View view) {
		Toast.makeText(
			this, 
			"Extra feature to be ...", 
			Toast.LENGTH_SHORT
		).show();
	}
	
	@Override
    public void onBackPressed() {
		killMediaPlayer();
//		unregisterReceiver(downloadBReceiver);
		
		finish();
	}
	
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();

        killMediaPlayer();

        unregisterReceiver(volumeBReceiver);
        unregisterReceiver(downloadBReceiver);
    }
    
	private void playAudio(String url)
    {
		killMediaPlayer();

        //if(mediaPlayer == null)     //TODO check why is null at this time!!!!
            mediaPlayer = new MediaPlayer();

        try {
			mediaPlayer.setDataSource(url);
	        mediaPlayer.prepare();
//		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if(mediaPlayer != null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Background Async Task to Load all Itens
     * */
    class LoadItens extends AsyncTask<String, String, String> {
 
    	SwipeListView swipelistview;

       /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            
            swipelistview = (SwipeListView)findViewById(R.id.example_swipe_lv_list);
            swipelistview.setSwipeListViewListener(
            	new BaseSwipeListViewListener() {
    	            @Override
    	            public void onClickFrontView(int position) {
    	                Log.d("soundbrowser", String.format("onClickFrontView %d", position));
    	                swipelistview.closeOpenedItems();
    	                swipelistview.openAnimate(position); //when you touch front view it will open
//    	                playAndSchedule(position);
    	                currentPosition = position;
    	            }
    	
    	            @Override
    	            public void onClickBackView(int position) {
    	                Log.d("soundbrowser", String.format("onClickBackView %d", position));
    	                swipelistview.closeAnimate(position);//when you touch back view it will close
    	                currentPosition = 0;
    	            }
            	}
            );

            pDialog = new ProgressDialog(PodcastActivity.this);
            pDialog.setMessage("Loading Itens ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting Inbox JSON
         * */
        protected String doInBackground(String... args) 
        {
            List<Item> currentListItem = null;
    		try {
    			currentListItem = PodcastService.getCreatePodcastList(
    				getHelper().getItemDao(), 
    				getHelper().getDao(Track.class), 
    				"PROVA ORAL"
    			);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            for (Item it : currentListItem)
				try {
					itemData.add(
					    new ItemRow(
//                        it.getTitle().substring(10),	//TODO check first if it has the date in the title
//                    	StringEscapeUtils.unescapeHtml4(it.getTitle().substring(10)),
//					    	new String(it.getTitle().substring(10).getBytes(), "UTF-8"),
					    	URLDecoder.decode(it.getTitle().substring(10), "UTF-8"),
					    	it.getTrack().getUrl(),
					    	getResources().getDrawable(R.drawable.ic_launcher)
					    )
					);
//		            new String(nodevalue.getBytes(), "UTF-8");
//		            StringEscapeUtils.unescapeHtml(
//		            	new String(ch, start, length).trim()
//		            );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) 
        {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ItemAdapter adapter = new ItemAdapter(
                    	PodcastActivity.this, R.layout.custom_row, itemData
                    );
                    swipelistview.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                }
            });
        }
        
        void closeOpenedItems() {
        	swipelistview.closeOpenedItems();
        }
    }
    
    class VolumeBroadcastReceiver extends BroadcastReceiver {

        private int oldVolume;
        
    	@Override
    	public void onReceive(Context context, Intent intent) 
    	{
    		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    		Log.i("soundbrowser", "screen on? : " + pm.isScreenOn());
    		if(pm.isScreenOn())
    			return;
    		
    		int volume = intent.getExtras().
    			getInt("android.media.EXTRA_VOLUME_STREAM_VALUE");
    		
            if(volume != oldVolume)
            {
                loadItens.closeOpenedItems();

                if((volume - oldVolume) > 0) 
            		currentPosition++;
                else
            		currentPosition--;
            	
    	        // Border Safety
    	        if (currentPosition < 0) currentPosition = 0;
    	        if (currentPosition >= itemData.size())
    	        	currentPosition = itemData.size() - 1;

    	        Log.i("soundbrowser", 
	    			"volume changed saw by broadcast volumeBReceiver -> (new:" + volume + ") - (old:" + oldVolume + ")" +
	    			"current position : " + currentPosition 
	    		);
    			
    	        playLocalOrExternalAudio();

    			oldVolume = volume;
            }
    	}
    }
}