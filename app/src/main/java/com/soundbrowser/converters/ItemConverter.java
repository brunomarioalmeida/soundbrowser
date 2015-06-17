package com.soundbrowser.converters;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.beans.BeanUtils;


import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Track;

public class ItemConverter {

	public static Item convertFromClientItem2ModelItem(
		com.soundbrowser.client.model.Item item) 
	{
	    Item localItem = new Item(){{setTrack(new Track());}};

//		BeanUtils.copyProperties(item, localItem);
//		BeanUtils.copyProperties(item.getTrack(), localItem.getTrack());
	    localItem.setTitle(item.getTitle());
	    localItem.setImage(item.getImage());
	    localItem.setSummary(item.getSummary());
	    localItem.setAuthor(item.getAuthor());
	    localItem.setLink(item.getLink());
		localItem.setPubDate("" + item.getPubDate());

		if(item.getTrack() != null) {
			localItem.getTrack().setUrl(item.getTrack().getUrl());
			localItem.getTrack().setSize("" + item.getTrack().getSizeTrack());
			localItem.getTrack().setItem(localItem);
		}
		
		return localItem;
	}
	
	public static List<Item> convertFromClientItens2ModelItens(
		com.soundbrowser.client.model.Item[] clientItens) 
	{
		List<Item> modelItensLst = new ArrayList<Item>();
		for (final com.soundbrowser.client.model.Item item : clientItens) 
		{
//		    Item localItem = new Item(){{setTrack(new Track());}};
//
////			BeanUtils.copyProperties(item, localItem);
////			BeanUtils.copyProperties(item.getTrack(), localItem.getTrack());
//		    localItem.setTitle(item.getTitle());
//		    localItem.setImage(item.getImage());
//		    localItem.setSummary(item.getSummary());
//		    localItem.setAuthor(item.getAuthor());
//		    localItem.setLink(item.getLink());
//			localItem.setPubDate("" + item.getPubDate());
//		    localItem.getTrack().setUrl(item.getTrack().getUrl());
//			localItem.getTrack().setSize("" + item.getTrack().getSizeTrack());
//			localItem.getTrack().setItem(localItem);			
//			
//				i.setItem(item.getItens());
			modelItensLst.add(convertFromClientItem2ModelItem(item));
		}
		return modelItensLst;
	}
}
