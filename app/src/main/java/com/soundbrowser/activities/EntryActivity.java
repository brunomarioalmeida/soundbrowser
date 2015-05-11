package com.soundbrowser.activities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.soundbrowser.R;
import com.soundbrowser.adapter.CustomAdapter;
import com.soundbrowser.converters.JsonToObjectConverter;
import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.SourceRoot;
import com.soundbrowser.persistence.model.Track;
import com.soundbrowser.receivers.DownloadBroadcastReceiver;
import com.soundbrowser.services.PodcastScheduler;
import com.soundbrowser.services.PodcastService;
//import com.soundbrowser.model.Item;

@Deprecated
public class EntryActivity extends Activity {

    private MediaPlayer mediaPlayer;
//    private AudioManager am;
//    private long enqueue;
//    private DownloadManager dm;

    private ListView listView;
    private List<Item> currentListItem;
    private SourceRoot sourceData;
    private int currentPosition;
    private int oldVolume;
    private EntryActivity.MyBroadcastReceiver receiver;
    private DownloadBroadcastReceiver dReceiver;

    private CustomAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        RestTemplate restTemplate = new RestTemplate();
/*        com.soundbrowser.client.model.Item item = restTemplate.getForObject(
        	"http://localhost:8080/items/title/PROVA ORAL", com.soundbrowser.client.model.Item.class
        );
        */
//        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        com.soundbrowser.client.model.Item item = restTemplate.getForObject(
//            "http://192.168.1.80:8080/items/title/PROVA ORAL", Item.class
            "http://localhost:8080/items/title/PROVA ORAL", com.soundbrowser.client.model.Item.class
        );
        Log.i("soundbrowser", "P - " + item);
        
        
        Log.i("soundbrowser", "onCreate");

        setContentView(R.layout.activity_entry);
//        setContentView(R.layout.main);

        dReceiver = new DownloadBroadcastReceiver(
    		(DownloadManager) getSystemService(DOWNLOAD_SERVICE)
    	);
        registerReceiver(
        	dReceiver, 
        	new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
        
//        if(true)
//        	return;
        
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
            ).getItemLst();
            
            listViewArr.addAll(currentListItem);

        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter( this, listViewArr, getResources() );
        listView.setAdapter(adapter);
    }

    public void downloadFile(View view) {
    	
    	//TODO Make this configurable
    	if(true)
    		return;
    	
        dReceiver.downloadFile(
            currentListItem.get(currentPosition).getTrack().getUrl()
        );
    }

//    public void onClick(View view) 
//    {
//        downloadBReceiver.downloadFile(
////          "http://www.vogella.de/img/lars/LarsVogelArticle7.png"
//        	"http://rsspod.rtp.pt/podcasts/at1/1301/2311894_128363-1301250641.mp3"
//        );
//    }
// 
//    public void showDownload(View view) 
//    {
//        Intent i = new Intent();
//        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//        startActivity(i);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        killMediaPlayer();

        unregisterReceiver(receiver);
        unregisterReceiver(dReceiver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        Log.i("soundbrowser", "dispatchKeyEvent");
        
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        if (action == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) 
            {
                Log.i("soundbrowser", "KEYCODE_BACK pressed");
                try {
                    currentListItem = PodcastService.
                    	recursiveListUp(currentListItem.get(0)).
                    		getParent().getParent().getItemLst();
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
            currentListItem = currentListItem.get(mPosition).getItemLst();
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
            String url = playAndSchedule(mPosition);

            // TODO Activate it later on when feature is configurable
//            downloadBReceiver.downloadFile(url);
        }
    }
    
    private String playAndSchedule(int position)
    {
    	currentPosition = position;

        String url = currentListItem.get(position).getTrack().getUrl();
    	try {
			playAudio(url);

			Track track = currentListItem.get(position).getTrack();
    		if(!(track.getTimmingsLst() == null || track.getTimmingsLst().isEmpty()))
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
