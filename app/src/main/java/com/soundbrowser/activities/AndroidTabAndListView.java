package com.soundbrowser.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.soundbrowser.R;

public class AndroidTabAndListView extends TabActivity {
    
    private static final String PODCAST_TITLE = "Podcast";
    private static final String TODO_TITLE = "TODO";
     
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_list);
         
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
}