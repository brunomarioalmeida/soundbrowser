package com.soundbrowser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import com.soundbrowser.adapter.CustomAdapter;
import com.soundbrowser.converter.JsonToObjectConverter;
import com.soundbrowser.model.Item;
import com.soundbrowser.model.SourceRoot;
import com.soundbrowser.model.Track;
import com.soundbrowser.services.PodcastScheduler;
import com.soundbrowser.services.PodcastService;

public class EntryActivity extends Activity {

    private MediaPlayer mediaPlayer;
//    private AudioManager am;

    private ListView listView;
    private List<Item> currentListItem;
    private SourceRoot sourceData;
    private int currentPosition;
    private int oldVolume;
    private EntryActivity.MyBroadcastReceiver receiver;

    private CustomAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i("soundbrowser", "onCreate");

        setContentView(R.layout.activity_entry);
        
        receiver = new EntryActivity.MyBroadcastReceiver();
        registerReceiver(
        	receiver, 
        	new IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        );
        
        currentPosition = 0;

//        am = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();

        listView = (ListView) findViewById(R.id.list);

        List<Item> listViewArr = new ArrayList<Item>();
        try {
            sourceData = JsonToObjectConverter.convert(
                new InputStreamReader(getAssets().open("rtp_antena_3_flat.json"), "UTF-8")
            );

            // TODO You should clean empty Itens
            currentListItem = PodcastService.getItemBaseForPodcastItensList(
            	sourceData.getItem().get(0)
            ).getItem();
            
            listViewArr.addAll(currentListItem);

        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter( this, listViewArr, getResources() );
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        killMediaPlayer();

        unregisterReceiver(receiver);
    }

    // TODO This method is only triggered when screen is ON. We need the opposite.
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        Log.i("soundbrowser", "dispatchKeyEvent");
        
//        if (((PowerManager) getSystemService(POWER_SERVICE)).isScreenOn())
//            return super.dispatchKeyEvent(event);

        int action = event.getAction();
        int keyCode = event.getKeyCode();

//        if (action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
//            try {
//                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) ;
//                    currentPosition++;
//                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
//                    currentPosition--;
//                    currentPosition--; // TODO Verifica o porque da necessidade da dupla invocacao
//                }
//
//                // Safety
//                if (currentPosition < 0) currentPosition = 0;
//                if (currentPosition >= currentListItem.size())
//                    currentPosition = currentListItem.size() - 1;
//
//                playAndSchedule(currentPosition);
//
//            } catch (Exception e) {
//                Log.i("error : ", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
//            return true;
//        }
//        else
            //return super.dispatchKeyEvent(event);
	        if (action == KeyEvent.ACTION_UP) {
	            if (keyCode == KeyEvent.KEYCODE_BACK) 
	            {
	                Log.i("soundbrowser", "KEYCODE_BACK pressed");
	                try {
	                    currentListItem = PodcastService.
	                    	recursiveListUp(currentListItem.get(0)).
	                    		getParent().getParent().getItem();
	                } catch (NullPointerException e) {
	                    Log.i("soundbrowser", e.getLocalizedMessage());
	                    return super.dispatchKeyEvent(event);
	                }
	
	                adapter.setData(currentListItem);
	                adapter.notifyDataSetChanged();
	
	                currentPosition = 0;
	                
	                return true;
	            } else
		            return super.dispatchKeyEvent(event);
	        }
	        else
	            return super.dispatchKeyEvent(event);
    }

    // This function used by adapter
    public void onItemClick(int mPosition)
    {
        if(currentListItem.get(mPosition).getTrack() == null)
        {
            currentListItem = currentListItem.get(mPosition).getItem();
            adapter.setData(currentListItem);
            adapter.notifyDataSetChanged();

            currentPosition = 0;
        }
        else {
            // Show Alert
            /*
            Toast.makeText(
                getApplicationContext(),
                " Position : " + mPosition +
                " ListItem : " + ((TextView)listView.getChildAt(mPosition)).getContentDescription(),
                Toast.LENGTH_LONG
            ).show();
            */
//        	currentPosition = mPosition;
            playAndSchedule(mPosition);
        }

//        colorSwitcher();
    }

    private void playAndSchedule(int position)
    {
    	try {
			playAudio(currentListItem.get(position).getTrack().getUrl());

			Track track = currentListItem.get(position).getTrack();
    		if(!(track.getTimmings() == null || track.getTimmings().isEmpty()))
    			new PodcastScheduler().setupAudioSchedules(track, mediaPlayer);
    		
        } catch (Exception e) {
            Log.i("error : ", e.getLocalizedMessage());
            e.printStackTrace();
        }
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
    
    class MyBroadcastReceiver extends BroadcastReceiver {

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
	    			"volume changed saw by broadcast receiver -> (new:" + volume + ") - (old:" + oldVolume + ")" +
	    			"current position : " + currentPosition 
	    		);
    			playAndSchedule(currentPosition);

    			oldVolume = volume;
            }
    	}
    }
}
