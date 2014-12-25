package com.soundbrowser.services;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.util.Log;

import com.soundbrowser.model.Timming;
import com.soundbrowser.model.Track;

public class PodcastScheduler {

    public void setupAudioSchedules(Track track, MediaPlayer mediaPlayer) 
    {
        List<Timming> timmingsLst = track.getTimmings();
    	ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
    		Executors.newScheduledThreadPool(timmingsLst.size());
        
        // TODO - Refactor
    	int previousMiliSeconds = getTimeMiliseconds(timmingsLst.get(0).getPivotStart());
        for (Timming timming : timmingsLst) 
        {
        	int miliSeconds = getTimeMiliseconds(timming.getPivotStart()) + previousMiliSeconds;
        	timming.setAcumulatedMiliSeconds(miliSeconds);
        	previousMiliSeconds = miliSeconds;
        }
        
//      sch.schedule(getRunnable("01:00"), 5, TimeUnit.SECONDS);
        for (Timming timming : timmingsLst) 
        	sch.schedule(
        		getRunnable(timming.getPivotStart(), mediaPlayer), 
        		timming.getAcumulatedMiliSeconds(), 
        		TimeUnit.MILLISECONDS
        	);
        
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

    // TODO Refactor
    private int getTimeMiliseconds(String time)
    {
        StringTokenizer sT = new StringTokenizer(time, ":");
        int minutos = Integer.parseInt(sT.nextToken());
        int segundos = Integer.parseInt(sT.nextToken());
        return (60*minutos + segundos)*1000;
    }

    private Runnable getRunnable(final String time, final MediaPlayer mediaPlayer){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                int timeMs = getTimeMiliseconds(time);
                mediaPlayer.seekTo(timeMs);
                Log.i("time : ", time + " - " + timeMs);
            }
        };

        return runnable;
    }
}
