package com.soundbrowser.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SearchResultsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.i("soundbrowser", "onCreate");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("soundbrowser", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) 
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
        {
            String query = "ola_" + intent.getStringExtra(SearchManager.QUERY) + "_alo";
    		Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            Log.i("soundbrowser", query);
        }
    }
}