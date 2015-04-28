package com.soundbrowser.services;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.util.Log;

import com.soundbrowser.persistence.model.Timming;
import com.soundbrowser.persistence.model.Track;
import com.soundbrowser.utils.GeneralUtils;

public class PodcastScheduler {

    public void setupAudioSchedules(Track track, MediaPlayer mediaPlayer) 
    {
        List<Timming> timmingsLst = track.getTimmings();
    	ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
    		Executors.newScheduledThreadPool(timmingsLst.size());
        
    	GeneralUtils.calculateAccumulatedTimmings(timmingsLst);
        
//    	sch.schedule(getRunnable("01:00"), 5, TimeUnit.SECONDS);
        for (int i = 0; i < timmingsLst.size(); i++) 
        	sch.schedule(
        		getRunnable(timmingsLst.get(i+1).getPivotStart(), mediaPlayer), 
        		timmingsLst.get(i).getAcumulatedMiliSeconds(), 
        		TimeUnit.MILLISECONDS
        	);
        for (int i = 1; i < timmingsLst.size()+1; i++) 
        	sch.scheduleWithFixedDelay(
        		getRunnable(timmingsLst.get(i).getPivotStart(), mediaPlayer),
        		30*i,
        		30*i, 
        		TimeUnit.SECONDS
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

    private Runnable getRunnable(final String time, final MediaPlayer mediaPlayer){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                int timeMs = GeneralUtils.getTimeMiliseconds(time);
                mediaPlayer.seekTo(timeMs);
                Log.i("soundbrowser", time + " - " + timeMs);
            }
        };

        return runnable;
    }
}
