package com.soundbrowser.services;

import java.util.List;

import com.soundbrowser.model.Item;

public class PodcastService {

	public static Item getItemBaseForPodcastList(Item item) {
		return getItemBaseForPodcastItensList(item).
			getParent().getParent().getItem().get(0).getParent();
	}
	
	public static Item getItemBaseForPodcastItensList(Item item) {
		return getItemWithUrl(item).getParent();
	}
	
	public static Item recursiveListUp(Item item) 
	{
		Item parentItem = item.getParent();
		if(parentItem.getTitle() != null)
			return parentItem;
		else
			return recursiveListUp(parentItem);
	}
	
	public static Item getItemWithUrl(Item item) 
	{
		List<Item> itemLst = item.getItem();
		for (Item child : itemLst) {
			if(child.getTrack() != null && child.getTrack().getUrl() != null)
				return child;
			else
				return getItemWithUrl(child);
		}
		return null;
	}
}
