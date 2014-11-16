package com.soundbrowser;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soundbrowser.adapter.CustomAdapter;
import com.soundbrowser.model.Item;
import com.soundbrowser.model.SoundSource;
import com.soundbrowser.model.Track;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class EntryActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private AudioManager am;

    private ListView listView;
    private  ArrayList<Item> listViewArr = new ArrayList<Item>();
    private int currentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);

        currentPosition = 0;

        am = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();

        listView = (ListView) findViewById(R.id.list);

        /*
        audiopathList = new ArrayList<String>(){{
            add("file://mnt/sdcard/Recording/record20141103222917.3gpp");
            add("file://mnt/sdcard/Recording/record20141103222947.3gpp");

            // To test in tablet
            //add("file://sdcard/recording1831815422.amr");
            //add("file://sdcard/recording726735500.amr");
        }};
        */

        try {
            Reader reader = new InputStreamReader(getAssets().open("sounds.json"), "UTF-8");
            Gson gson = new GsonBuilder().create();
            SoundSource p = gson.fromJson(reader, SoundSource.class);

            for (Item itemIn : p.getSource().getSource().getItem())
            {
                Track t = new Track();
                t.setUrl(itemIn.getTrack().getUrl());
                t.setDuration(itemIn.getTrack().getDuration());

                Item item = new Item();
                item.setTitle(itemIn.getTitle());
                item.setTrack(t);

                listViewArr.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter( this, listViewArr, getResources() );
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        killMediaPlayer();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP ||  keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            try {
                if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                    currentPosition++;
                if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                    currentPosition--;

                playAndSchedule(currentPosition);

            } catch (Exception e) {
                Log.i("error : ", e.getLocalizedMessage());
                e.printStackTrace();
            }
            return true;
        } else
            return super.dispatchKeyEvent(event);
    }

    // This function used by adapter
    public void onItemClick(int mPosition)
    {
        listView.getChildAt(mPosition).setBackgroundColor(
            getResources().getColor(android.R.color.holo_orange_dark)
        );

        // Show Alert
        Toast.makeText(
            getApplicationContext(),
            " Position : " + mPosition +
            " ListItem : " + ((TextView)listView.getChildAt(mPosition)).getContentDescription(),
            Toast.LENGTH_LONG
        ).show();

        playAndSchedule(mPosition);
    }

    private void playAndSchedule(int position)
    {
        try {
            playAudio((( Item ) listViewArr.get(position)).getTrack().getUrl());
        } catch (Exception e) {
            Log.i("error : ", e.getLocalizedMessage());
            e.printStackTrace();
        }

        /* TODO use this later if needed
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run()
            {
                mediaPlayer.stop();

                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

                try {
                    playAudio(audiopathList.get(1));
                } catch (Exception e) {
                    Log.i("error : ", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(doAsynchronousTask, 19000);
        */
    }

    private void playAudio(String url)
      throws Exception
    {
        killMediaPlayer();

        //if(mediaPlayer == null)     //TODO check why is null at this time!!!!
            mediaPlayer = new MediaPlayer();

        // TODO improve the datasource with the following ...
        //AssetFileDescriptor fileDesc = getResources().openRawResourceFd(R.raw.music_file);
        //mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc.getStartOffset(), fileDesc.getLength());
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
}
