package com.soundbrowser.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.res.Resources;

import com.soundbrowser.persistence.model.Timming;

public class GeneralUtils {

    public static int convertDpToPixel(float dp, Resources resources) {
   		return (int) (dp * (resources.getDisplayMetrics().densityDpi / 160f));
    }
    
	public static void calculateAccumulatedTimmings(List<Timming> timmingsLst) 
	{
		int previousMiliSeconds = 0; //getTimeMiliseconds(timmingsLst.get(0).getPivotEnd());
        for (Timming timming : timmingsLst) 
        {
        	int miliSeconds = getTimeMiliseconds(timming.getPivotEnd()) + previousMiliSeconds;
        	timming.setAcumulatedMiliSeconds(miliSeconds);
        	previousMiliSeconds = miliSeconds;
        }
	}
	
    public static int getTimeMiliseconds(String time)
    {
        StringTokenizer sT = new StringTokenizer(time, ":");
        int minutos = Integer.parseInt(sT.nextToken());
        int segundos = Integer.parseInt(sT.nextToken());
        return (60*minutos + segundos)*1000;
    }
    
	public static com.soundbrowser.persistence.model.Item buildItemPathToRoot() 
	{
		final com.soundbrowser.persistence.model.Item provaOralItem = 
			new com.soundbrowser.persistence.model.Item();
		provaOralItem.setTitle("PROVA ORAL");

		final com.soundbrowser.persistence.model.Item antena3Item = 
			new com.soundbrowser.persistence.model.Item();
		antena3Item.setTitle("Antena 3");
		antena3Item.setItemLst(
			new ArrayList<com.soundbrowser.persistence.model.Item>(){{
				add(provaOralItem);
			}}
		);

		com.soundbrowser.persistence.model.Item rtpItem = 
			new com.soundbrowser.persistence.model.Item();
		rtpItem.setTitle("RTP");
		rtpItem.setItemLst(
			new ArrayList<com.soundbrowser.persistence.model.Item>(){{
				add(antena3Item);
			}}
		);
	
		return rtpItem;
	}
}
