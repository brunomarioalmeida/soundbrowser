package com.soundbrowser.activities;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.soundbrowser.R;
import com.soundbrowser.adapter.ItemAdapter;
import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Track;
import com.soundbrowser.persistence.ormlite.DatabaseHelper;
import com.soundbrowser.receivers.DownloadBroadcastReceiver;
import com.soundbrowser.services.PodcastService;

public class PodcastActivity extends OrmLiteBaseActivity<DatabaseHelper> 
//implements OnFocusChangeListener 
{ 

	// Progress Dialog
//    private ProgressDialog pDialog;
 
    List<Item> currentListItem;
    int currentPosition;
    String previousTitle;
    
    private MediaPlayer mediaPlayer;
    
    private PodcastActivity.VolumeBroadcastReceiver volumeBReceiver;
    private DownloadBroadcastReceiver downloadBReceiver;
    
    LoadItens loadItens;
    AutoCompleteTextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

    	super.onCreate(savedInstanceState);
//    if(true)
//    	return;
        setContentView(R.layout.inbox_list);
        
//        AppController.getInstance().getRequestQueue().getCache().clear();
//        if(true)
//        	return;
         
//        buildAutocompleteWidget();

        // Hashmap for ListView
        currentListItem = new ArrayList<Item>();
  
        // Loading Itens in Background Thread
        loadItens = new LoadItens();
//        loadItens.execute("Mixórdia de Temáticas");
//        loadItens.execute("PROVA ORAL");
//        loadItens.execute("Antena 3");
        loadItens.execute("Geral");
        
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

	private void buildAutocompleteWidget() 
	{
		// Get a reference to the AutoCompleteTextView in the layout
        textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
//    	textView.setOnFocusChangeListener(this);
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, countries
        );
        textView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) 
            {
//            	Animation animation = new TranslateAnimation(0,0,0,1000);
//            	animation.setDuration(3000);
//            	textView.startAnimation(animation);
            	textView.setVisibility(View.GONE);
            	
                String selection = (String)parent.getItemAtPosition(position);
                new LoadItens().execute(selection);
//                Toast.makeText(PodcastActivity.this, selection, Toast.LENGTH_SHORT).show();
            }
        });
        textView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				 if (keyCode == KeyEvent.KEYCODE_ENTER)
			     {
//	                String selection = (String)parent.getItemAtPosition(position);
					 new LoadItens().execute("Antena 3");
					 return true;
			     }
				 return false;
			}
		});
        textView.setAdapter(adapter);
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
		String url = "";
		if(currentListItem.get(currentPosition).getTrack() != null)
			url = currentListItem.get(currentPosition).getTrack().getUrl();

		String subPath = "";
		try {
			subPath = url.substring(url.indexOf("wavrss"));
		} catch (StringIndexOutOfBoundsException e) {
			subPath = url.substring(url.indexOf("mixordiadetematicas"));
		}
        
		String localUrl = 
        	Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + 
        	Environment.DIRECTORY_DOWNLOADS + "/" + subPath;
		
		File f = new File(localUrl);
		playAudio(f.exists()?localUrl:url);
	}

	public void clickDownloadBtn(View view) {
		Toast.makeText(
			this, 
			"Going to download the podcast ...", 
			Toast.LENGTH_SHORT
		).show();

		if(currentListItem.get(currentPosition).getTrack() != null)
			downloadBReceiver.downloadFile(
				currentListItem.get(currentPosition).getTrack().getUrl()
			);
	}

	public void clickExtraBtn(View view) throws SQLException {
		Toast.makeText(
			this, 
			"Extra feature to be ...", 
			Toast.LENGTH_SHORT
		).show();
		
		currentListItem.get(currentPosition).setVisto(true);
		getHelper().getItemDao().update(currentListItem.get(currentPosition));
	}
	
	@Override
    public void onBackPressed() {
		killMediaPlayer();
//		unregisterReceiver(downloadBReceiver);
		
		finish();
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {
        if(previousTitle != null) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        	new LoadItens().execute(previousTitle);
		    	previousTitle = null;
	            return true;
	        }
        }
        return super.onKeyDown(keyCode, event);
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
    	            public void onClickFrontView(int position) 
    	            {
    	                Log.d("soundbrowser", String.format("onClickFrontView %d", position));
    	                currentPosition = position;

    	        		if (currentListItem.get(currentPosition).getTrack().getUrl() == null)
    	        		{
    	        			previousTitle = currentListItem.get(currentPosition).getParent().getTitle();
    	        			new LoadItens().execute(
    	        				currentListItem.get(currentPosition).getTitle()
    	        			);
    	        		} else {
        	                swipelistview.closeOpenedItems();
        	                swipelistview.openAnimate(position); //when you touch front view it will open
//        	                playAndSchedule(position);
    	        		}

//    	                SharedPreferences preferences=getSharedPreferences("session", getApplicationContext().MODE_PRIVATE);
//                        String searchStr=preferences.getString("searchStr",null);
//                        Toast.makeText(PodcastActivity.this, searchStr, Toast.LENGTH_SHORT).show();
    	            }
    	
    	            @Override
    	            public void onClickBackView(int position) {
    	                Log.i("soundbrowser", String.format("onClickBackView %d", position));
    	                currentPosition = 0;
    	                swipelistview.closeAnimate(position);//when you touch back view it will close
//    	                new LoadItens().execute(
//    	                	// TODO Preencher o title do parent em termos de BD
////	        				currentListItem.get(currentPosition).getParent().getTitle()
//    	                	previousTitle
////	        				"Mixórdia de Temáticas"
//	        			);
    	            }
    	            
    	            public void onFirstListItem() {
    	                Log.i("soundbrowser", String.format("onFirstListItem"));
    	                
//    	                Animation animation = new TranslateAnimation(0,0,0,1000);
//    	                animation.setDuration(3000);
//    	                textView.startAnimation(animation);
//    	                textView.setVisibility(View.VISIBLE);
    	            };
    	            
    	            public void onListChanged() {
    	                Log.i("soundbrowser", String.format("onListChanged"));
    	            };

    	            public void onChoiceChanged(int position, boolean selected) {
    	                Log.i("soundbrowser", String.format("onChoiceChanged %d %b", position, selected));
    	            };
    	            
    	            public void onLastListItem() {
    	                Log.i("soundbrowser", String.format("onLastListItem"));
    	            };
    	            
    	            public void onMove(int position, float x) {
    	                Log.i("soundbrowser", String.format("onMove %d %f", position, x));
    	            };
            	}
            );

//            pDialog = new ProgressDialog(PodcastActivity.this);
//            pDialog.setMessage("Loading Itens ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }
 
        /**
         * getting Inbox JSON
         * */
        protected String doInBackground(String... args) 
        {
//    		try {
//    			new PodcastBundleService().buildAndStorePortugueseSamplePodcasts(
//    				true, getHelper().getItemDao(), getHelper().getTrackDao()
//    			);
//    		} catch (SQLException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
    		try {
    			currentListItem = PodcastService.getCreatePodcastList(
    				getHelper().getItemDao(), 
    				getHelper().getDao(Track.class), 
    				args[0]
    			);
            } catch (Exception e) {
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
//            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ItemAdapter adapter = new ItemAdapter(
                    	PodcastActivity.this, R.layout.custom_row, currentListItem
                    );
                    swipelistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
    	        if (currentPosition >= currentListItem.size())
    	        	currentPosition = currentListItem.size() - 1;

    	        Log.i("soundbrowser", 
	    			"volume changed saw by broadcast volumeBReceiver -> (new:" + volume + ") - (old:" + oldVolume + ")" +
	    			"current position : " + currentPosition 
	    		);
    			
    	        playLocalOrExternalAudio();

    			oldVolume = volume;
            }
    	}
    }

//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		Log.i("soundbrowser", "onFocusChange? : " + v.getClass().getCanonicalName() + " - " + hasFocus);
////		if(textView.requestFocus()) 
////		    getWindow().setSoftInputMode(
////		    	WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
////		    );
//		textView.requestFocus();
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//	}
}

//StringEscapeUtils.unescapeHtml4(it.getTitle().substring(10)),
//URLDecoder.decode(it.getTitle().substring(10), "UTF-8"),
