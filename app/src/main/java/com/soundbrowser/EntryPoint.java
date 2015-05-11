package com.soundbrowser;

import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.soundbrowser.adapter.ItemAdapter;
import com.soundbrowser.adapter.ItemRow;
import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Track;
import com.soundbrowser.persistence.ormlite.DatabaseHelper;
import com.soundbrowser.receivers.DownloadBroadcastReceiver;
import com.soundbrowser.services.PodcastScheduler;
import com.soundbrowser.services.PodcastService;
import com.soundbrowser.utils.GeneralUtils;

public class EntryPoint extends OrmLiteBaseActivity<DatabaseHelper> {

	SwipeListView swipelistview;
	ItemAdapter adapter;
	List<ItemRow> itemData;
    public List<Item> currentListItem;
	
    private MediaPlayer mediaPlayer;

    public int currentPosition;
    private int oldVolume;
    
    private EntryPoint.VolumeBroadcastReceiver volumeBReceiver;
    public DownloadBroadcastReceiver downloadBReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        itemData = new ArrayList<ItemRow>();
        adapter = new ItemAdapter(this, R.layout.custom_row, itemData);

        swipelistview = (SwipeListView)findViewById(R.id.example_swipe_lv_list);
        swipelistview.setSwipeListViewListener(
        	new BaseSwipeListViewListener() {
	            @Override
	            public void onClickFrontView(int position) {
	                Log.d("soundbrowser", String.format("onClickFrontView %d", position));

	                swipelistview.openAnimate(position); //when you touch front view it will open
	                playAndSchedule(position);
	            }
	
	            @Override
	            public void onClickBackView(int position) {
	                Log.d("soundbrowser", String.format("onClickBackView %d", position));
	                
	                swipelistview.closeAnimate(position);//when you touch back view it will close
	            }
        	}
        );
        
        swipelistview.setOffsetLeft(GeneralUtils.convertDpToPixel(0f, getResources())); // left side offset
        swipelistview.setOffsetRight(GeneralUtils.convertDpToPixel(80f, getResources())); // right side offset
        swipelistview.setAdapter(adapter);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
        		
        	Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		try {
//			currentListItem = PodcastService.getCurrentListFromServer("PROVA ORAL"); 
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
            itemData.add(
                new ItemRow(
                    it.getTitle(), it.getTrack().getUrl(),
                    getResources().getDrawable(R.drawable.ic_launcher)
                )
            );
        adapter.notifyDataSetChanged();
        
//        mediaPlayer = new MediaPlayer();

        volumeBReceiver = new EntryPoint.VolumeBroadcastReceiver();
        registerReceiver(
        	volumeBReceiver, 
        	new IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        );
        downloadBReceiver = new DownloadBroadcastReceiver(
    		(DownloadManager) getSystemService(DOWNLOAD_SERVICE)
    	);
        registerReceiver(
        	downloadBReceiver, 
        	new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
        
        currentPosition = 0;
    }

    @Override
    protected void onDestroy() 
    {
        super.onDestroy();

        killMediaPlayer();

        unregisterReceiver(volumeBReceiver);
        unregisterReceiver(downloadBReceiver);
    }
    
    private String playAndSchedule(int position)
    {
    	if(currentListItem.get(position).getTrack() == null)
    		return "";

    	currentPosition = position;
    		
        String url = currentListItem.get(position).getTrack().getUrl();
        // TODO refactor this!!!
//	        String localUrl = 
//	        	Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + 
//	        	Environment.DIRECTORY_DOWNLOADS + "/" + 
//	        	url.substring(url.indexOf("podcasts"));
        
//	        File f = new File(localUrl);
    	try {
//				playAudio(f.exists()?localUrl:url);
			playAudio(url);

			com.soundbrowser.persistence.model.Track track = currentListItem.get(position).getTrack();
    		if(!(track.getTimmings() == null || track.getTimmings().isEmpty()))
    			new PodcastScheduler().setupAudioSchedules(track, mediaPlayer);
    		
        } catch (Exception e) {
            Log.i("error : ", e.getLocalizedMessage());
            e.printStackTrace();
        }
    	
    	return url;
    }
    
	private void playAudio(String url)
      throws Exception
    {
        killMediaPlayer();

        //if(mediaPlayer == null)     //TODO check why is null at this time!!!!
            mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(url);

        mediaPlayer.prepare();
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

    public void onItemClick(int mPosition) {
        playAndSchedule(mPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    class VolumeBroadcastReceiver extends BroadcastReceiver {

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
    			playAndSchedule(currentPosition);

    			oldVolume = volume;
            }
    	}
    }
}

// These settings are already configured in the correspondent xml file
//swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are five swiping modes
//swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS); //there are four swipe actions 
//swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
//swipelistview.setAnimationTime(500); // Animation time
//swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress

//SourceRoot sourceData = JsonToObjectConverter.convert(
//new InputStreamReader(getAssets().open("rtp_antena_3_flat.json"), "UTF-8")
//);
// TODO You should clean empty Itens
//currentListItem = PodcastService.getItemBaseForPodcastItensList(
//sourceData.getItem().get(0)
//).getItem();

//@Override
//public boolean dispatchKeyEvent(KeyEvent event)
//{
//  Log.i("soundbrowser", "dispatchKeyEvent");
//  if(true)
//      return super.dispatchKeyEvent(event);
//
//  int action = event.getAction();
//  int keyCode = event.getKeyCode();
//
//  if (action == KeyEvent.ACTION_UP) {
//      if (keyCode == KeyEvent.KEYCODE_BACK) 
//      {
//          Log.i("soundbrowser", "KEYCODE_BACK pressed");
//          try {
//              currentListItem = PodcastService.
//              	recursiveListUp(currentListItem.get(0)).
//              		getParent().getParent().getItemLst();
//          } catch (NullPointerException e) {
//              e.printStackTrace();
//              return super.dispatchKeyEvent(event);
//          }
//
//          itemData.clear();
//          for (Item it : currentListItem)
//              itemData.add(
//                  new ItemRow(
//                      it.getTitle(),
//                      getResources().getDrawable(R.drawable.ic_launcher)
//                  )
//              );
//          
//          
//          adapter.setData(itemData);
//          adapter.notifyDataSetChanged();
//
//          currentPosition = 0;
//          
//          return true;
//      } else
//          return super.dispatchKeyEvent(event);
//  }
//  else
//      return super.dispatchKeyEvent(event);
//}

//Log.i(
//	"soundbrowser", 
//	Environment.getExternalStoragePublicDirectory(".").getAbsolutePath()
//);
