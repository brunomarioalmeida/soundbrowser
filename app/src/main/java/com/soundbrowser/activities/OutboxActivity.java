package com.soundbrowser.activities;

import android.app.ListActivity;
import android.os.Bundle;

import com.soundbrowser.R;

public class OutboxActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outbox_list);
    }
}