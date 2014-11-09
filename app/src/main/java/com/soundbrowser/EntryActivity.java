package com.soundbrowser;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soundbrowser.model.SoundSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class EntryActivity extends Activity {

    List<String> audiopathList;
    private int playbackPosition = 0;

    private MediaPlayer mediaPlayer;
    private AudioManager am;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        am = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();

        audiopathList = new ArrayList<String>(){{
            add("http://users.skynet.be/fa046054/home/P22/track06.mp3");
            add("http://www.noiseaddicts.com/samples/2558.mp3");
            add("http://www.noiseaddicts.com/samples/2537.mp3");
            add("http://www.noiseaddicts.com/samples/2540.mp3");
        }};

        audiopathList = new ArrayList<String>(){{
            add("file://mnt/sdcard/Recording/record20141103222917.3gpp");
            //add("file://sdcard/recording1831815422.amr");

            add("file://mnt/sdcard/Recording/record20141103222947.3gpp");
            //add("file://sdcard/recording726735500.amr");
        }};

        try {
            Reader reader = new InputStreamReader(getAssets().open("sounds.json"), "UTF-8");
            Gson gson = new GsonBuilder().create();
            SoundSource p = gson.fromJson(reader, SoundSource.class);
            audiopathList.add(p.getSource().getSource().getItem().get(0).getTrack().getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_entry);
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
                playAudio(audiopathList.get(2));
            } catch (Exception e) {
                Log.i("error : ", e.getLocalizedMessage());
                e.printStackTrace();
            }
            return true;
        } else
            return super.dispatchKeyEvent(event);
    }

    public void doClick(View view)
    {
        switch(view.getId()) {
            case R.id.start:
                playAndSchedule();
                break;
/*
            case R.id.pausePlayerBtn:
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
                break;
            case R.id.restartPlayerBtn:
                if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(playbackPosition);
                    mediaPlayer.start();
                }
                break;
  */
            case R.id.stop:
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    playbackPosition = 0;
                }
                break;
        }
    }

    private void playAndSchedule()
    {
        try {
            playAudio(audiopathList.get(0));
        } catch (Exception e) {
            Log.i("error : ", e.getLocalizedMessage());
            e.printStackTrace();
        }

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
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
