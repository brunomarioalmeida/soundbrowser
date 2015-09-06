package com.soundbrowser.activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.soundbrowser.R;

public class AndroidTabAndListView extends TabActivity {
    
    private static final String PODCAST_TITLE = "Podcast";
    private static final String TODO_TITLE = "TODO";
    
//    WakeLock wakeLock;
     
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_list);
         
//        Settings.System.putInt(
//        	getContentResolver(), 
//        	Settings.System.WIFI_SLEEP_POLICY, 
//      		Settings.System.WIFI_SLEEP_POLICY_NEVER
//      	);
//        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
//        wakeLock.acquire();
          	
        TabHost tabHost = getTabHost();

        TabSpec podcastSpec = tabHost.newTabSpec(PODCAST_TITLE);
        podcastSpec.setIndicator(PODCAST_TITLE, getResources().getDrawable(R.drawable.icon_inbox));
        podcastSpec.setContent(new Intent(this, PodcastActivity.class));
         
        TabSpec todoSpec = tabHost.newTabSpec(TODO_TITLE);
        todoSpec.setIndicator(TODO_TITLE, getResources().getDrawable(R.drawable.icon_outbox));
        todoSpec.setContent(new Intent(this, OutboxActivity.class));
         
        tabHost.addTab(podcastSpec); 
        tabHost.addTab(todoSpec); 
    }
    
//    @Override
//    protected void onDestroy() {
//    	// TODO Auto-generated method stub
//    	super.onDestroy();
//    	
//    	wakeLock.release();
//    }
    
    
    // TODO - not ready yet to use android search
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) 
//    {
//        getMenuInflater().inflate(
//        	R.menu.options_menu, menu
//        );
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(
//        	Context.SEARCH_SERVICE
//        );
//        SearchView searchView = (SearchView) menu.
//        	findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//        	searchManager.getSearchableInfo(getComponentName())
//        );
//        
//        return true;
//    }
 }