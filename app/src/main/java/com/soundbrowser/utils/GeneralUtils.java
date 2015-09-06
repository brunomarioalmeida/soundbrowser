package com.soundbrowser.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.res.Resources;

import com.soundbrowser.persistence.model.Item;
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
    
	public static com.soundbrowser.persistence.model.Item buildItemPathToRoot(final String[] pathToRoot) 
	{
		com.soundbrowser.persistence.model.Item rootItem = null; 
		for (int i = 0; i < pathToRoot.length; i++) 
		{
			final com.soundbrowser.persistence.model.Item subItem = 
				new com.soundbrowser.persistence.model.Item();
			subItem.setTitle(pathToRoot[i]);
				
			if(i == 0)
				rootItem = subItem; 
			if(i == pathToRoot.length - 1)
				continue;
			
			final com.soundbrowser.persistence.model.Item subItemChild = 
				new com.soundbrowser.persistence.model.Item();
			subItemChild.setTitle(pathToRoot[i+1]);
				
			subItem.setItemLst(
				new ArrayList<com.soundbrowser.persistence.model.Item>(){{
					add(subItemChild);
				}}
			);
		}
		
		return rootItem;
	}
	
	public static com.soundbrowser.persistence.model.Item buildItemPathToRoot(String parentTitle) 
	{
		com.soundbrowser.persistence.model.Item parentItem = 
			new com.soundbrowser.persistence.model.Item();
		parentItem.setTitle(parentTitle);
		
		return parentItem;
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
	
	static int depth = -1;
	public static void printRecursiveItem(Item item) 
	{
		List<Item> itens = item.getItemLst();
		if(itens == null || itens.isEmpty())
			return;
		else
			depth++;
		
		for (Item i : itens) {
			System.out.println(getTabs(depth) + i.getTitle());
			printRecursiveItem(i);
		}
	}
//	List<com.soundbrowser.persistence.model.Item> v = itemBD.getItemLst();
//	for (com.soundbrowser.persistence.model.Item item : v) {
//		System.out.println(item.getTitle());
//		List<com.soundbrowser.persistence.model.Item> v1 = item.getItemLst();
//		for (com.soundbrowser.persistence.model.Item item2 : v1) {
//			System.out.println("\t" + item2.getTitle());
//			List<com.soundbrowser.persistence.model.Item> v2 = item2.getItemLst();
//			for (com.soundbrowser.persistence.model.Item item3 : v2) {
//				System.out.println("\t\t" + item3.getTitle());
//				List<com.soundbrowser.persistence.model.Item> v3 = item3.getItemLst();
//				for (com.soundbrowser.persistence.model.Item item4 : v3) {
//					System.out.println("\t\t\t" + item4.getTitle());
//				}
//			}
//		}
//	}
//	System.out.println(ToStringBuilder.reflectionToString(itemBD));
	
	static String getTabs(int depth) {
		String aStr = "";
		for (int i = 0; i < depth; i++) 
			aStr += "\t";
		return aStr;
	}
	
	private static RestTemplate template;
	public static RestTemplate buildRestTemplate() 
	{
		if(template == null)
		{
			template = new RestTemplate(){{
				getMessageConverters().add(
					new MappingJacksonHttpMessageConverter()
				);
			}};
		}

		return template;
	}
	
}
