package com.soundbrowser.converters;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.beans.BeanUtils;


import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Track;

public class ItemConverter {

	public static List<Item> convertFromClientItem2ModelItem(
		com.soundbrowser.client.model.Item[] clientItens) 
	{
		List<Item> modelItensLst = new ArrayList<Item>();
		for (final com.soundbrowser.client.model.Item item : clientItens) 
		{
		    Item localItem = new Item(){{setTrack(new Track());}};

		    localItem.setTitle(item.getTitle());
		    localItem.getTrack().setUrl(item.getTrack().getUrl());
//			BeanUtils.copyProperties(item, localItem);
//			BeanUtils.copyProperties(item.getTrack(), localItem.getTrack());
			localItem.setPubDate("" + item.getPubDate());
			localItem.getTrack().setSize("" + item.getTrack().getSizeTrack());
			localItem.getTrack().setItem(localItem);			
			
//				i.setItem(item.getItens());
			modelItensLst.add(localItem);
		}
		return modelItensLst;
	}
}
